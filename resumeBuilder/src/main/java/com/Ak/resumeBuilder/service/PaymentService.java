package com.Ak.resumeBuilder.service;

import com.Ak.resumeBuilder.document.Payment;
import com.Ak.resumeBuilder.document.User;
import com.Ak.resumeBuilder.dtos.AuthResponse;
import com.Ak.resumeBuilder.repository.PaymentRepository;
import com.Ak.resumeBuilder.repository.UserRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor

public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final AuthService authService;
    private  final UserRepository userRepository;

    @Value("{razorpay.key.id}")
    private String razorPayKeyId;
    @Value("{razorpay.key.secret}")
    private String  razorPayKeySecret;

    public Payment createOrder(Object principal, String planType) throws RazorpayException {
        AuthResponse response= authService.getProfile(principal);
        RazorpayClient razorpayClient=new RazorpayClient(razorPayKeyId,razorPayKeySecret);

        int amount = 99900; // it is in paise

        String currency = "INR";
        String receipt="premium"+'_'+ UUID.randomUUID().toString().substring(0,8);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount",amount);
        orderRequest.put("currency",currency);
        orderRequest.put("receipt",receipt);

        Order razorpayOrder= razorpayClient.orders.create(orderRequest);

       Payment newPayment= Payment.builder()
                .userId(response.getId())
                .razorpayOrderId(razorpayOrder.get("id"))
                .amount(amount)
                .currency(currency)
                .planType(planType)
                .status("created")
                .receipt(receipt)
                .build();

       return  paymentRepository.save(newPayment);
    }

    public boolean verifyPayment(String razorpayOrderId, String razorpayPaymentId, String razorpaySignature) throws RazorpayException {
        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id",razorpayOrderId);
        attributes.put("razorpay_payment_id",razorpayPaymentId);
        attributes.put("razorpay_signature",razorpaySignature);
       boolean isValidSignature= Utils.verifyPaymentSignature(attributes,razorPayKeySecret);
        try{
            if(isValidSignature){
                Payment payment= paymentRepository.findByRazorpayOrderId(razorpayOrderId)
                        .orElseThrow(()->new RuntimeException("payment not found"));
                payment.setRazorpayPaymentId(razorpayPaymentId);
                payment.setRazorpaySignature(razorpaySignature);
                payment.setStatus("paid");
                paymentRepository.save(payment);
                //upgrade the user subscription
                upgradeUserSubscription(payment.getUserId(),payment.getPlanType());
                return true ;
            }
            return false;

        }catch (Exception e){
                log.error("Eror verifying the payment ",e);
            return false;

        }

    }

    private void upgradeUserSubscription(String userId, String planType) {
        User existingUser=userRepository.findById(userId)
                        .orElseThrow(()->new UsernameNotFoundException("user not found"));
                existingUser.setSubscriptionPlan(planType);
                userRepository.save(existingUser);

        log.info("USER {} UPGRADE TO {} PLAN",userId,planType);
    }

    public List<Payment> getUserPayments(Object principal) {
        AuthResponse authResponse=authService.getProfile(principal);
      return  paymentRepository.findByUserIdOrderByCreatedAtDesc(authResponse.getId());
    }

    public Payment getPaymentDetails(String orderId) {
       return paymentRepository.findByRazorpayOrderId(orderId)
                .orElseThrow(()-> new RuntimeException("Order not found"));
    }
}

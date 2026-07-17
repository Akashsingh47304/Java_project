package com.Ak.resumeBuilder.controller;

import com.Ak.resumeBuilder.document.Payment;
import com.Ak.resumeBuilder.service.PaymentService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@RequestBody Map<String,String> request,
                                         Authentication authentication) throws RazorpayException {
        // step 1: call  the service method
           String planType= request.get("planType");
           if(!"premium".equalsIgnoreCase(planType)){
               return ResponseEntity.badRequest().body(Map.of("message","Invalid planType"));
           }
        Payment payment=paymentService.createOrder(authentication.getPrincipal(),planType);

           Map<String ,Object> response=Map.of(
                   "orderId",payment.getRazorpayOrderId(),
                   "amount",payment.getAmount(),
                   "currency",payment.getCurrency(),
                   "receipt",payment.getReceipt()
           );

           return ResponseEntity.ok(response);

    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyPayment(@RequestBody Map<String,String> request) throws RazorpayException {
      String razorpayOrderId= request.get("razorpay_order_id");
      String razorpayPaymentId= request.get("razorpay_payment_id");
      String razorpaySignature= request.get("razorpay_signature");
        if (razorpayOrderId == null || razorpayOrderId.isBlank()
                || razorpayPaymentId == null || razorpayPaymentId.isBlank()
                || razorpaySignature == null || razorpaySignature.isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "Missing payment details."
                    )
            );
        }

        boolean isValid =paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature
        );

       if(isValid){
           return ResponseEntity.ok(
                   Map.of(
                           "success", true,
                           "message", "Payment verified successfully."
                   )
           );
       }else{
           return ResponseEntity.badRequest().body(Map.of("message ","payment request is failed"));
       }
    }
    @GetMapping("/history")
    public ResponseEntity<?> getPaymentHistory(Authentication authentication){
        List<Payment> payments=paymentService.getUserPayments(authentication.getPrincipal());
        return ResponseEntity.ok(payments);

    }
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderDetails(@PathVariable String orderId){
        Payment paymentDetails= paymentService.getPaymentDetails(orderId);

        return ResponseEntity.ok(paymentDetails);
    }

}

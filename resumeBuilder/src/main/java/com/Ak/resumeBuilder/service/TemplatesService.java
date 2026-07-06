package com.Ak.resumeBuilder.service;


import com.Ak.resumeBuilder.dtos.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TemplatesService {
    private static final String PREMIUM="premium";
    private final AuthService authService;
    public Map<String,Object> getTemplates(Object principal){

        AuthResponse response= authService.getProfile(principal);
        List<String> allTemplates= Arrays.asList("01","02","03");
        List<String> availableTemplates;
        Boolean isPremimum=PREMIUM.equalsIgnoreCase(response.getSubscriptionPlan());
        if(isPremimum){
            availableTemplates=allTemplates;

        }else{
            availableTemplates=List.of("01");
        }
        Map<String,Object> restrictions=new HashMap<>();
        restrictions.put("availableTemplates",availableTemplates);
        restrictions.put("allTemplates",allTemplates);
        restrictions.put("subscriptionPlan",response.getSubscriptionPlan());
        restrictions.put("isPremium",isPremimum);
        return restrictions;

    }
}

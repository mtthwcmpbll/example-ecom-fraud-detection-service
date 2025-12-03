package com.example.ecom.fraud.controller;

import com.example.ecom.common.dto.ApiResponse;
import com.example.ecom.fraud.client.KycClient;
import com.example.ecom.fraud.client.RiskScoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/fraud")
public class FraudCheckController {

    @Autowired
    private KycClient kycClient;

    @Autowired
    private RiskScoreClient riskScoreClient;

    @PostMapping("/check")
    public ApiResponse<FraudCheckResponse> checkFraud(@RequestBody FraudCheckRequest request) {
        // 1. Check KYC
        ApiResponse<KycClient.KycResponse> kycResponse = kycClient
                .checkCustomer(new KycClient.KycRequest(request.getCustomerId()));
        if (kycResponse.getData() == null || !"ALLOW".equals(kycResponse.getData().getStatus())) {
            return ApiResponse.success(new FraudCheckResponse("BLOCKED", "KYC Failed"));
        }

        // 2. Check Risk Score
        ApiResponse<RiskScoreClient.RiskCheckRequest> riskResponse = riskScoreClient
                .assessRisk(new RiskScoreClient.RiskCheckRequest(request.getAmount()));
        if (riskResponse.getData() != null && "LOW".equals(riskResponse.getData().getRiskScore())) {
            return ApiResponse.success(new FraudCheckResponse("BLOCKED", "High Risk Transaction"));
        }

        return ApiResponse.success(new FraudCheckResponse("ALLOWED", "Checks Passed"));
    }

    public static class FraudCheckRequest {
        private Long customerId;
        private BigDecimal amount;

        public Long getCustomerId() {
            return customerId;
        }

        public void setCustomerId(Long customerId) {
            this.customerId = customerId;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }

    public static class FraudCheckResponse {
        private String status;
        private String reason;

        public FraudCheckResponse(String status, String reason) {
            this.status = status;
            this.reason = reason;
        }

        public String getStatus() {
            return status;
        }

        public String getReason() {
            return reason;
        }
    }
}

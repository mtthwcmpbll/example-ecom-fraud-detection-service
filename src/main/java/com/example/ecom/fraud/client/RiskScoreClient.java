package com.example.ecom.fraud.client;

import com.example.ecom.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;

@FeignClient(name = "risk-score-service", url = "${risk-score-service.url:http://localhost:8086}", configuration = com.example.ecom.client.config.FeignClientConfig.class)
public interface RiskScoreClient {

    @PostMapping("/api/risk/assess")
    ApiResponse<RiskCheckRequest> assessRisk(@RequestBody RiskCheckRequest request);

    class RiskCheckRequest {
        private BigDecimal amount;
        private String riskScore;

        public RiskCheckRequest() {
        }

        public RiskCheckRequest(BigDecimal amount) {
            this.amount = amount;
        }

        public BigDecimal getAmount() {
            return amount;
        }

        public String getRiskScore() {
            return riskScore;
        }

        public void setRiskScore(String riskScore) {
            this.riskScore = riskScore;
        }
    }
}

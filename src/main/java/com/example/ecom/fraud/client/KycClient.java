package com.example.ecom.fraud.client;

import com.example.ecom.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "kyc-service", url = "${kyc-service.url:http://localhost:8085}", configuration = com.example.ecom.client.config.FeignClientConfig.class)
public interface KycClient {

    @PostMapping("/api/kyc/check")
    ApiResponse<KycResponse> checkCustomer(@RequestBody KycRequest request);

    class KycRequest {
        private Long customerId;

        public KycRequest() {
        }

        public KycRequest(Long customerId) {
            this.customerId = customerId;
        }

        public Long getCustomerId() {
            return customerId;
        }
    }

    class KycResponse {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}

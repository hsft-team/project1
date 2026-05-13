package com.attendance.backend.service;

import com.attendance.backend.config.PlatformPolicyClientProperties;
import com.attendance.backend.dto.platform.PlatformCompanyPolicyResponse;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class PlatformPolicyService {

    private static final Logger log = LoggerFactory.getLogger(PlatformPolicyService.class);

    private final PlatformPolicyClientProperties properties;

    public PlatformPolicyService(PlatformPolicyClientProperties properties) {
        this.properties = properties;
    }

    public Optional<PlatformCompanyPolicyResponse> getCompanyPolicy(Long companyId) {
        if (!properties.isEnabled()) {
            return Optional.empty();
        }

        try {
            PlatformCompanyPolicyResponse response = RestClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("X-Internal-Api-Key", properties.getInternalApiKey())
                .build()
                .get()
                .uri("/api/internal/platform/policies/{companyId}", companyId)
                .retrieve()
                .body(PlatformCompanyPolicyResponse.class);

            return Optional.ofNullable(response);
        } catch (RestClientResponseException exception) {
            if (exception.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            log.warn("Failed to fetch platform policy. companyId={}, status={}, body={}",
                companyId,
                exception.getStatusCode().value(),
                exception.getResponseBodyAsString());
            return Optional.empty();
        } catch (RestClientException exception) {
            log.warn("Failed to fetch platform policy. companyId={}", companyId, exception);
            return Optional.empty();
        }
    }
}

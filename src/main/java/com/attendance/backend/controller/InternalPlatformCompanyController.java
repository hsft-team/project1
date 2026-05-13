package com.attendance.backend.controller;

import com.attendance.backend.config.InternalApiProperties;
import com.attendance.backend.dto.internal.InternalCompanyDetailResponse;
import com.attendance.backend.dto.internal.InternalCompanySummaryResponse;
import com.attendance.backend.dto.internal.InternalCompanyUpdateRequest;
import com.attendance.backend.exception.BusinessException;
import com.attendance.backend.service.AdminService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/internal/platform/companies")
public class InternalPlatformCompanyController {

    private static final String API_KEY_HEADER = "X-Internal-Api-Key";

    private final AdminService adminService;
    private final InternalApiProperties internalApiProperties;

    public InternalPlatformCompanyController(AdminService adminService, InternalApiProperties internalApiProperties) {
        this.adminService = adminService;
        this.internalApiProperties = internalApiProperties;
    }

    @GetMapping
    public ResponseEntity<List<InternalCompanySummaryResponse>> getCompanies(
        @RequestHeader(API_KEY_HEADER) String apiKey
    ) {
        validateApiKey(apiKey);
        return ResponseEntity.ok(adminService.getCompanySummaries());
    }

    @GetMapping("/{companyId}")
    public ResponseEntity<InternalCompanyDetailResponse> getCompany(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @PathVariable("companyId") Long companyId
    ) {
        validateApiKey(apiKey);
        return ResponseEntity.ok(adminService.getCompanyDetail(companyId));
    }

    @PatchMapping("/{companyId}")
    public ResponseEntity<InternalCompanyDetailResponse> updateCompany(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @PathVariable("companyId") Long companyId,
        @RequestBody InternalCompanyUpdateRequest request
    ) {
        validateApiKey(apiKey);
        return ResponseEntity.ok(adminService.updateCompanyForPlatform(companyId, request));
    }

    private void validateApiKey(String apiKey) {
        if (!StringUtils.hasText(internalApiProperties.getKey()) || !internalApiProperties.getKey().equals(apiKey)) {
            throw new BusinessException("내부 API 인증에 실패했습니다.");
        }
    }
}

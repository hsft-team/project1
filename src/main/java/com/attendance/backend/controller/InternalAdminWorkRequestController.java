package com.attendance.backend.controller;

import com.attendance.backend.config.InternalApiProperties;
import com.attendance.backend.dto.internal.InternalWorkRequestCreateRequest;
import com.attendance.backend.dto.internal.InternalWorkRequestListResponse;
import com.attendance.backend.dto.internal.InternalWorkRequestReviewRequest;
import com.attendance.backend.dto.internal.InternalWorkRequestResponse;
import com.attendance.backend.dto.internal.InternalWorkRequestUploadResponse;
import com.attendance.backend.exception.BusinessException;
import com.attendance.backend.service.WorkRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/internal/admin/work-requests")
public class InternalAdminWorkRequestController {

    private static final String API_KEY_HEADER = "X-Internal-Api-Key";
    private static final String ADMIN_CODE_HEADER = "X-Admin-Employee-Code";

    private final WorkRequestService workRequestService;
    private final InternalApiProperties internalApiProperties;

    public InternalAdminWorkRequestController(WorkRequestService workRequestService, InternalApiProperties internalApiProperties) {
        this.workRequestService = workRequestService;
        this.internalApiProperties = internalApiProperties;
    }

    @GetMapping
    public ResponseEntity<InternalWorkRequestListResponse> getRequests(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @RequestHeader(ADMIN_CODE_HEADER) String adminEmployeeCode
    ) {
        validateHeaders(apiKey, adminEmployeeCode);
        return ResponseEntity.ok(workRequestService.getRequestsForAdmin(adminEmployeeCode));
    }

    @PostMapping
    public ResponseEntity<InternalWorkRequestResponse> create(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @RequestHeader(ADMIN_CODE_HEADER) String adminEmployeeCode,
        @RequestBody InternalWorkRequestCreateRequest request
    ) {
        validateHeaders(apiKey, adminEmployeeCode);
        return ResponseEntity.ok(workRequestService.createRequestForAdmin(adminEmployeeCode, request));
    }

    @PostMapping("/upload")
    public ResponseEntity<InternalWorkRequestUploadResponse> upload(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @RequestHeader(ADMIN_CODE_HEADER) String adminEmployeeCode,
        @RequestPart("workRequestFile") MultipartFile workRequestFile
    ) {
        validateHeaders(apiKey, adminEmployeeCode);
        return ResponseEntity.ok(workRequestService.uploadRequestsForAdmin(adminEmployeeCode, workRequestFile));
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<InternalWorkRequestResponse> approve(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @RequestHeader(ADMIN_CODE_HEADER) String adminEmployeeCode,
        @PathVariable Long requestId,
        @RequestBody(required = false) InternalWorkRequestReviewRequest request
    ) {
        validateHeaders(apiKey, adminEmployeeCode);
        String reviewNote = request == null ? null : request.getReviewNote();
        return ResponseEntity.ok(workRequestService.approveRequest(adminEmployeeCode, requestId, reviewNote));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<InternalWorkRequestResponse> reject(
        @RequestHeader(API_KEY_HEADER) String apiKey,
        @RequestHeader(ADMIN_CODE_HEADER) String adminEmployeeCode,
        @PathVariable Long requestId,
        @RequestBody(required = false) InternalWorkRequestReviewRequest request
    ) {
        validateHeaders(apiKey, adminEmployeeCode);
        String reviewNote = request == null ? null : request.getReviewNote();
        return ResponseEntity.ok(workRequestService.rejectRequest(adminEmployeeCode, requestId, reviewNote));
    }

    private void validateHeaders(String apiKey, String adminEmployeeCode) {
        if (!StringUtils.hasText(internalApiProperties.getKey()) || !internalApiProperties.getKey().equals(apiKey)) {
            throw new BusinessException("내부 API 인증에 실패했습니다.");
        }
        if (!StringUtils.hasText(adminEmployeeCode)) {
            throw new BusinessException("관리자 식별자가 필요합니다.");
        }
    }
}

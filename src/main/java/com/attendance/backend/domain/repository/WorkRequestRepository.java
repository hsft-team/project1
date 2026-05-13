package com.attendance.backend.domain.repository;

import com.attendance.backend.domain.entity.WorkRequest;
import com.attendance.backend.domain.entity.WorkRequestStatus;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkRequestRepository extends JpaRepository<WorkRequest, Long> {

    @EntityGraph(attributePaths = {"employee", "employee.company", "employee.workplace", "reviewedBy"})
    List<WorkRequest> findAllByEmployeeIdOrderByRequestDateDescCreatedAtDesc(Long employeeId);

    @EntityGraph(attributePaths = {"employee", "employee.company", "employee.workplace", "reviewedBy"})
    Optional<WorkRequest> findByIdAndEmployeeId(Long id, Long employeeId);

    @EntityGraph(attributePaths = {"employee", "employee.company", "employee.workplace", "reviewedBy"})
    List<WorkRequest> findAllByCompanyIdOrderByStatusAscRequestDateDescCreatedAtDesc(Long companyId);

    @EntityGraph(attributePaths = {"employee", "employee.company", "employee.workplace", "reviewedBy"})
    List<WorkRequest> findAllByCompanyIdAndEmployeeWorkplaceIdOrderByStatusAscRequestDateDescCreatedAtDesc(
        Long companyId,
        Long workplaceId
    );

    boolean existsByEmployeeIdAndRequestDateAndStatusIn(Long employeeId, LocalDate requestDate, List<WorkRequestStatus> statuses);
}

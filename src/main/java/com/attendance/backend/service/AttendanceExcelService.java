package com.attendance.backend.service;

import com.attendance.backend.domain.entity.AttendanceRecord;
import com.attendance.backend.domain.entity.Employee;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

@Service
public class AttendanceExcelService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public byte[] createMonthlyAttendanceWorkbook(
        YearMonth yearMonth,
        List<Employee> employees,
        List<AttendanceRecord> records
    ) {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            CellStyle headerStyle = createHeaderStyle(workbook);

            createSummarySheet(workbook, headerStyle, employees, records);
            createDetailSheet(workbook, headerStyle, yearMonth, records);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException ex) {
            throw new IllegalStateException("엑셀 파일 생성 중 오류가 발생했습니다.", ex);
        }
    }

    private void createSummarySheet(
        XSSFWorkbook workbook,
        CellStyle headerStyle,
        List<Employee> employees,
        List<AttendanceRecord> records
    ) {
        XSSFSheet sheet = workbook.createSheet("Summary");
        Row headerRow = sheet.createRow(0);
        createCell(headerRow, 0, "직원명", headerStyle);
        createCell(headerRow, 1, "사번", headerStyle);
        createCell(headerRow, 2, "총 근무시간", headerStyle);
        createCell(headerRow, 3, "총 근무분", headerStyle);
        createCell(headerRow, 4, "출근일수", headerStyle);

        Map<Long, List<AttendanceRecord>> recordsByEmployeeId = records.stream()
            .collect(Collectors.groupingBy(record -> record.getEmployee().getId()));

        int rowIndex = 1;
        for (Employee employee : employees) {
            List<AttendanceRecord> employeeRecords = recordsByEmployeeId.getOrDefault(employee.getId(), List.of());
            long totalMinutes = employeeRecords.stream()
                .mapToLong(this::calculateWorkedMinutes)
                .sum();

            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(employee.getName());
            row.createCell(1).setCellValue(employee.getEmployeeCode());
            row.createCell(2).setCellValue(formatMinutes(totalMinutes));
            row.createCell(3).setCellValue(totalMinutes);
            row.createCell(4).setCellValue(employeeRecords.size());
        }

        autoSizeColumns(sheet, 5);
    }

    private void createDetailSheet(
        XSSFWorkbook workbook,
        CellStyle headerStyle,
        YearMonth yearMonth,
        List<AttendanceRecord> records
    ) {
        XSSFSheet sheet = workbook.createSheet("Details");
        Row headerRow = sheet.createRow(0);
        createCell(headerRow, 0, "날짜", headerStyle);
        createCell(headerRow, 1, "직원명", headerStyle);
        createCell(headerRow, 2, "사번", headerStyle);
        createCell(headerRow, 3, "출근 시간", headerStyle);
        createCell(headerRow, 4, "퇴근 시간", headerStyle);
        createCell(headerRow, 5, "근무시간", headerStyle);
        createCell(headerRow, 6, "지각 여부", headerStyle);
        createCell(headerRow, 7, "상태", headerStyle);

        List<AttendanceRecord> sortedRecords = records.stream()
            .sorted(Comparator
                .comparing(AttendanceRecord::getAttendanceDate)
                .thenComparing(record -> record.getEmployee().getName()))
            .toList();

        int rowIndex = 1;
        for (AttendanceRecord record : sortedRecords) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(formatDate(record.getAttendanceDate()));
            row.createCell(1).setCellValue(record.getEmployee().getName());
            row.createCell(2).setCellValue(record.getEmployee().getEmployeeCode());
            row.createCell(3).setCellValue(formatDateTime(record.getCheckInTime()));
            row.createCell(4).setCellValue(formatDateTime(record.getCheckOutTime()));
            row.createCell(5).setCellValue(formatMinutes(calculateWorkedMinutes(record)));
            row.createCell(6).setCellValue(record.isLate() ? "Y" : "N");
            row.createCell(7).setCellValue(record.getStatus().name());
        }

        if (sortedRecords.isEmpty()) {
            Row row = sheet.createRow(1);
            row.createCell(0).setCellValue(yearMonth + " 데이터가 없습니다.");
        }

        autoSizeColumns(sheet, 8);
    }

    private long calculateWorkedMinutes(AttendanceRecord record) {
        LocalDateTime checkInTime = record.getCheckInTime();
        LocalDateTime checkOutTime = record.getCheckOutTime();
        if (checkInTime == null || checkOutTime == null) {
            return 0;
        }
        return Duration.between(checkInTime, checkOutTime).toMinutes();
    }

    private String formatMinutes(long totalMinutes) {
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        return String.format("%02d:%02d", hours, minutes);
    }

    private String formatDate(LocalDate date) {
        return date == null ? "" : DATE_FORMATTER.format(date);
    }

    private String formatDateTime(LocalDateTime dateTime) {
        return dateTime == null ? "" : DATE_TIME_FORMATTER.format(dateTime);
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private void createCell(Row row, int columnIndex, String value, CellStyle style) {
        row.createCell(columnIndex).setCellValue(value);
        row.getCell(columnIndex).setCellStyle(style);
    }

    private void autoSizeColumns(XSSFSheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
}

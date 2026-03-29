# DB Table Specification

SQL 리포트 작성용으로 현재 출퇴근 시스템 PostgreSQL 테이블 구조를 정리한 문서입니다.

기준 소스:

- backend JPA entity
- admin-web SQL 리포트

주의:

- 현재 스키마는 `backend` 의 JPA 엔티티 기준입니다.
- `backend/src/main/resources/application.yml` 기준 `spring.jpa.hibernate.ddl-auto=update` 이므로, 엔티티 변경 시 실제 테이블도 함께 바뀔 수 있습니다.

## 전체 관계

```text
companies 1 --- N employees
companies 1 --- N workplaces
companies 1 --- 1 company_settings

workplaces 1 --- N employees

employees 1 --- N attendance_records
employees 1 --- N attendance_action_logs
```

## 공통 컬럼

아래 테이블들은 모두 공통으로 다음 컬럼을 가집니다.

- `created_at` timestamp, not null
- `updated_at` timestamp, not null

기준: `BaseTimeEntity`

## 1. companies

회사 기본 정보 테이블입니다.

| 컬럼명 | 타입 | 제약 | 설명 |
| --- | --- | --- | --- |
| `id` | bigint | PK, auto increment | 회사 ID |
| `name` | varchar(100) | not null, unique | 회사명 |
| `latitude` | double precision | not null | 회사 기본 위도 |
| `longitude` | double precision | not null | 회사 기본 경도 |
| `created_at` | timestamp | not null | 생성일시 |
| `updated_at` | timestamp | not null | 수정일시 |

자주 쓰는 조인:

- `employees.company_id = companies.id`
- `workplaces.company_id = companies.id`
- `company_settings.company_id = companies.id`

## 2. company_settings

회사별 출근 설정 테이블입니다.

| 컬럼명 | 타입 | 제약 | 설명 |
| --- | --- | --- | --- |
| `id` | bigint | PK, auto increment | 설정 ID |
| `company_id` | bigint | FK, not null, unique | 회사 ID |
| `allowed_radius_meters` | integer | not null | 허용 반경(m) |
| `late_after_time` | time | not null | 지각 기준 시간 |
| `notice_message` | varchar(1000) | nullable | 모바일 공지 |
| `enforce_single_device_login` | boolean | not null, default true | 단말 1대 제한 여부 |
| `created_at` | timestamp | not null | 생성일시 |
| `updated_at` | timestamp | not null | 수정일시 |

관계:

- `company_settings.company_id` -> `companies.id`
- 회사당 1개 설정만 존재

## 3. workplaces

사업장 정보 테이블입니다.

| 컬럼명 | 타입 | 제약 | 설명 |
| --- | --- | --- | --- |
| `id` | bigint | PK, auto increment | 사업장 ID |
| `company_id` | bigint | FK, not null | 회사 ID |
| `name` | varchar(100) | not null | 사업장명 |
| `latitude` | double precision | not null | 사업장 위도 |
| `longitude` | double precision | not null | 사업장 경도 |
| `allowed_radius_meters` | integer | not null | 사업장 허용 반경(m) |
| `notice_message` | varchar(1000) | nullable | 사업장 공지 |
| `created_at` | timestamp | not null | 생성일시 |
| `updated_at` | timestamp | not null | 수정일시 |

자주 쓰는 조인:

- `workplaces.company_id = companies.id`
- `employees.workplace_id = workplaces.id`

참고:

- `workplace_id` 가 `null` 인 직원은 본사 소속처럼 처리됩니다.

## 4. employees

직원 및 관리자 계정 테이블입니다.

| 컬럼명 | 타입 | 제약 | 설명 |
| --- | --- | --- | --- |
| `id` | bigint | PK, auto increment | 직원 ID |
| `employee_code` | varchar(50) | not null, unique | 사번 |
| `name` | varchar(100) | not null | 이름 |
| `password` | varchar | not null | 암호화 비밀번호 |
| `active` | boolean | not null, default true | 사용 여부 |
| `deleted` | boolean | not null, default false | 삭제 여부 |
| `password_change_required` | boolean | not null, default true | 비밀번호 변경 필요 여부 |
| `role` | varchar(20) | not null | 권한 |
| `company_id` | bigint | FK, not null | 회사 ID |
| `workplace_id` | bigint | FK, nullable | 사업장 ID |
| `work_start_time` | time | nullable | 개인 출근 기준 시간 |
| `work_end_time` | time | nullable | 개인 퇴근 기준 시간 |
| `registered_device_id` | varchar(120) | nullable | 등록 단말 ID |
| `registered_device_name` | varchar(200) | nullable | 등록 단말명 |
| `device_registered_at` | timestamp | nullable | 단말 등록 시각 |
| `created_at` | timestamp | not null | 생성일시 |
| `updated_at` | timestamp | not null | 수정일시 |

`role` 값:

- `ADMIN`
- `WORKPLACE_ADMIN`
- `EMPLOYEE`

자주 쓰는 조인:

- `employees.company_id = companies.id`
- `employees.workplace_id = workplaces.id`
- `attendance_records.employee_id = employees.id`
- `attendance_action_logs.employee_id = employees.id`

실무 필터 예시:

- 활성 직원만: `active = true and deleted = false`
- 특정 사업장 직원만: `workplace_id = ?`
- 본사 직원만: `workplace_id is null`

## 5. attendance_records

실제 출근/퇴근 결과 테이블입니다.

| 컬럼명 | 타입 | 제약 | 설명 |
| --- | --- | --- | --- |
| `id` | bigint | PK, auto increment | 출근 기록 ID |
| `employee_id` | bigint | FK, not null | 직원 ID |
| `attendance_date` | date | not null | 출근일 |
| `check_in_time` | timestamp | not null | 출근 시각 |
| `check_out_time` | timestamp | nullable | 퇴근 시각 |
| `check_in_latitude` | double precision | not null | 출근 위도 |
| `check_in_longitude` | double precision | not null | 출근 경도 |
| `check_out_latitude` | double precision | nullable | 퇴근 위도 |
| `check_out_longitude` | double precision | nullable | 퇴근 경도 |
| `late` | boolean | not null | 지각 여부 |
| `status` | varchar(20) | not null | 출근 상태 |
| `created_at` | timestamp | not null | 생성일시 |
| `updated_at` | timestamp | not null | 수정일시 |

유니크 제약:

- `(employee_id, attendance_date)`
- 직원당 하루 1건만 존재

`status` 값:

- `CHECKED_IN`
- `CHECKED_OUT`

자주 쓰는 조인:

- `attendance_records.employee_id = employees.id`

실무 필터 예시:

- 월별 조회:
  `attendance_date >= date_trunc('month', current_date)::date`
- 기간 조회:
  `attendance_date between date '2026-03-01' and date '2026-03-31'`

## 6. attendance_action_logs

출근/퇴근 시도 로그 테이블입니다. 성공/실패 모두 남길 수 있습니다.

| 컬럼명 | 타입 | 제약 | 설명 |
| --- | --- | --- | --- |
| `id` | bigint | PK, auto increment | 로그 ID |
| `employee_id` | bigint | FK, not null | 직원 ID |
| `action_type` | varchar(20) | not null | 시도 유형 |
| `attendance_date` | date | not null | 대상 출근일 |
| `latitude` | double precision | not null | 요청 위도 |
| `longitude` | double precision | not null | 요청 경도 |
| `accuracy_meters` | double precision | not null | 위치 정확도 |
| `captured_at` | timestamp | not null | 클라이언트 캡처 시각 |
| `distance_meters` | double precision | nullable | 기준 위치와 거리 |
| `success` | boolean | not null | 성공 여부 |
| `message` | varchar(500) | not null | 처리 결과 메시지 |
| `created_at` | timestamp | not null | 생성일시 |
| `updated_at` | timestamp | not null | 수정일시 |

`action_type` 값:

- `CHECK_IN`
- `CHECK_OUT`

자주 쓰는 조인:

- `attendance_action_logs.employee_id = employees.id`

## SQL 리포트 작성 시 자주 쓰는 조인 패턴

### 출근 기록 + 직원 + 사업장

```sql
select
    ar.attendance_date,
    e.employee_code,
    e.name,
    coalesce(w.name, '본사') as workplace_name,
    ar.check_in_time,
    ar.check_out_time,
    ar.late,
    ar.status
from attendance_records ar
join employees e on e.id = ar.employee_id
left join workplaces w on w.id = e.workplace_id
order by ar.attendance_date desc, e.name asc;
```

### 직원 + 회사 설정

```sql
select
    e.employee_code,
    e.name,
    c.name as company_name,
    cs.allowed_radius_meters,
    cs.late_after_time
from employees e
join companies c on c.id = e.company_id
join company_settings cs on cs.company_id = c.id;
```

## SQL 리포트 권한 주의

admin-web SQL 리포트 기준:

- `ADMIN`: 원본 테이블 조회 가능
- `WORKPLACE_ADMIN`: `scoped_employees`, `scoped_attendance_records`, `scoped_workplace` 만 사용 가능

즉, 사업장 관리자는 이 문서의 원본 테이블명 대신 SQL 리포트 화면에서 제공하는 `scoped_*` 이름으로 조회해야 합니다.

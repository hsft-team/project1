# AWS Deployment Guide

이 프로젝트는 `GitHub Actions + Amazon ECR + AWS App Runner + Amazon RDS for PostgreSQL` 조합으로 운영 배포하는 것을 기준으로 합니다.

## 권장 구성

- 애플리케이션: AWS App Runner
- 컨테이너 이미지 저장소: Amazon ECR
- 데이터베이스: Amazon RDS for PostgreSQL
- 비밀값 관리: AWS Secrets Manager 또는 App Runner 환경 변수

## 1. AWS 리소스 준비

### 1-1. ECR 리포지토리 생성

예시 이름:

- `attendance-backend`

### 1-2. RDS PostgreSQL 생성

필수 정보:

- DB 이름: `attendance_db`
- 사용자명: 예) `attendance_app`
- 비밀번호: 강한 비밀번호 사용

연결 문자열 예시:

```text
jdbc:postgresql://<rds-endpoint>:5432/attendance_db
```

### 1-3. App Runner 서비스 생성

소스 설정:

- Source type: `Container registry`
- Provider: `Amazon ECR`
- Image URI: `<account-id>.dkr.ecr.<region>.amazonaws.com/attendance-backend:latest`

포트:

- `8080`

환경 변수:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `JWT_SECRET`
- `SERVER_PORT=8080`

중요 설정:

- `Automatic deployment` 활성화

이 옵션을 켜두면 GitHub Actions가 ECR에 새 이미지를 push할 때 App Runner가 자동으로 새 버전을 배포합니다.

## 2. GitHub 설정

### 2-1. GitHub Actions Variables

Repository Settings -> Secrets and variables -> Actions -> Variables

- `AWS_REGION`
- `ECR_REPOSITORY`

예시:

- `AWS_REGION=ap-northeast-2`
- `ECR_REPOSITORY=attendance-backend`

### 2-2. GitHub Actions Secret

Repository Settings -> Secrets and variables -> Actions -> Secrets

- `AWS_ROLE_TO_ASSUME`

이 값은 GitHub OIDC로 Assume할 IAM Role ARN 입니다.

예시:

```text
arn:aws:iam::<account-id>:role/github-actions-ecr-deploy-role
```

## 3. IAM 권한

GitHub Actions에서 Assume하는 IAM Role에는 최소한 아래 권한이 필요합니다.

- ECR 로그인
- ECR 이미지 push

대표적으로 필요한 액션:

- `ecr:GetAuthorizationToken`
- `ecr:BatchCheckLayerAvailability`
- `ecr:CompleteLayerUpload`
- `ecr:InitiateLayerUpload`
- `ecr:PutImage`
- `ecr:UploadLayerPart`

## 4. 배포 방식

`.github/workflows/deploy.yml` 은 아래 시점에 동작합니다.

- `main` 브랜치 push
- 수동 실행

동작 순서:

1. 소스 체크아웃
2. AWS OIDC 인증
3. ECR 로그인
4. Docker 이미지 빌드
5. ECR에 `${GITHUB_SHA}` 태그 push
6. ECR에 `latest` 태그 push
7. App Runner 자동 배포

## 5. 운영 전 권장 사항

- `spring.jpa.hibernate.ddl-auto=update` 는 장기적으로 Flyway/Liquibase로 교체 권장
- JWT secret은 충분히 긴 랜덤 문자열 사용
- RDS는 퍼블릭 오픈보다 VPC 내부 연결 권장
- App Runner와 RDS 연결 시 VPC Connector 사용 권장
- CloudWatch 알람 설정 권장

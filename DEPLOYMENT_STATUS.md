# RunOn Project - Deployment Status

## 현재 상황 (2024-09-08)

### ✅ 완료된 작업
- [x] GitHub 저장소 생성: https://github.com/ncyj1290/runon
- [x] Docker 멀티스테이지 빌드 Dockerfile 작성
- [x] GCP Cloud Build 설정 (cloudbuild.yaml)
- [x] GitHub Actions CI/CD 파이프라인 설정
- [x] Cloud Run 자동 배포 성공
- [x] GCP 프로젝트 설정: `noted-episode-471308-t2`
- [x] 서비스 계정 및 권한 설정 완료

### ❌ 해결 필요한 문제
- [ ] **403 Forbidden 에러** 
  - URL: https://runon-service-43029381843.asia-northeast3.run.app/
  - 원인: JSP ViewResolver 또는 Tomcat 설정 문제
  - 해결 방법: Spring MVC 설정 점검 또는 Thymeleaf로 전환

### 🚧 다음에 할 일
- [ ] 403 에러 해결
- [ ] VM 배포 설정 (Compute Engine)
- [ ] 팀 프로젝트용 배포 환경 구성

## GCP 설정 정보
- **프로젝트 ID**: noted-episode-471308-t2
- **리전**: asia-northeast3 (서울)
- **Cloud Run 서비스**: runon-service
- **서비스 계정**: github-actions-418@noted-episode-471308-t2.iam.gserviceaccount.com

## GitHub 정보
- **저장소**: https://github.com/ncyj1290/runon
- **자동 배포**: main 브랜치 push시 Cloud Run에 자동 배포

## 학원에서 이어하기 위한 설정
1. `git clone https://github.com/ncyj1290/runon.git`
2. Docker Desktop 설치
3. `gcloud auth login` 및 `gcloud config set project noted-episode-471308-t2`
4. GitHub CLI 설치 (선택사항)

## 문제 해결을 위한 체크리스트
- [ ] servlet-context.xml ViewResolver 설정 확인
- [ ] JSP 의존성 확인 (tomcat-embed-jasper 등)
- [ ] web.xml servlet mapping 확인
- [ ] Tomcat 로그 확인
- [ ] 간단한 @RestController로 테스트

## 연락처
- GitHub: ncyj1290
- 프로젝트: Spring MVC + Docker + GCP 자동화 배포
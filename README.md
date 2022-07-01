# 장소 검색 서비스 (Search Local Service)

## 기능

### 1. 장소 검색
`카카오 검색 API`, `네이버 검색 API`를 통해 각각 최대 5개씩, 총 10개의 키워드 관련 장소를 검색
- 동일 장소 조건: `장소 이름`이 같거나 `주소`가 일치 할 때
- 동일 장소 이름 조건: `공백 제거`, `태그를 제거`한 문자열이 일치 할 때
- 동일 주소 조건: 공백 기준으로 단어를 구분하여 다음 규칙에 맞게 Set으로 반환. 크기가 작은 Set 기준으로 동일한 문자열이 일치 할 때
  - 특수문자: 포함시 단어 전체 반환
  - 숫자: 포함시 단어 전체 반환
  - 문자: 앞에서 부터 2글자만 반환
  ```bash
  EX) 작은 Set 기준으로 모두 일치하므로 두 주소는 동일
    서울 중구 다동 155 => {서울, 중구, 다동, 155} 
    서울특별시 중구 다동 155 2층 206~209호 => {서울, 중구, 다동, 155, 2층, 206~209호}
  ```
    
- 구현: SearchLocalService.java > search

### 2. 검색 키워드 Top10 목록
사용자들이 많이 검색한 순서대로, `최대 10개의` 검색 키워드 및 검색 횟수 목록을 제공
- h2 저장소를 사용하여 사용자가 `장소 검색` Action이 발생할 때마다 키워드 데이터 `추가` 또는 `count 갱신`
- 이때, 동시성 이슈가 발생할 수 있는 부분을 염두하여 `@Transactional`, `@PersistenceContext & EntityManager` 사용
- JPA를 사용하여 검색 키워드 Top10 목록 조회
- 참고: 서비스 재구동시 h2 저장소는 초기화
- 구현: KeywordRepositoryCustomImpl.java > save, KeywordService.java > searchTopList

### 3. 공통
- 요청 API에 대한 `Validation` 및 `Exception` 처리
- 검색 API 장애 발생 상황에 대한 `Exception` 처리
- `DDD 계층 구조`로 새로운 검색 API 제공자의 추가에 대한 `확장성` 및 `가용성` 고려
- 각 검색 API 연결 정보 변경 사항에 대한 `유연성` 고려 (application.yml)
  ```
  application:
    client:
      kakao:
        token: 
        url: 'https://dapi.kakao.com/'
      naver:
        clientId: 
        clientSecret: 
        url: 'https://openapi.naver.com/'
  ```
- `JPA`를 사용함으로서 간단한 CRUD 작업으로 `생산성` 고려
- 각 서비스에 대하여 `테스트 코드` 작성하여 `유지보수` 고려
- Spring Docs를 이용한 요청 RESTful API 명세하여 `생산성` 및 `유지보수` 고려
  ([index.adoc](/src/docs/asciidoc/index.adoc))
##  API 테스트
```bash
/api-requests.http
```

## 사용 라이브러리
- retrofit2: 검색 API에 대하여 http 통신 하기 위해 사용한 라이브러리
- modelmapper: 객체간 매핑을 위한 라이브러리
- spring-boot-starter-validation: 요청 API 요청 변수 유효성 검증을 위해 사용한 라이브러리
- spring-boot-starter-data-jpa: 자바표준 ORM 프레임워크 라이브러리 
- spring-restdocs-mockmvc: RESTful API 명세 문서 자동화
- h2: 검색 키워드 Top10 목록 조회 서비스를 위해 키워드별 count를 저장할 인메모리 DB

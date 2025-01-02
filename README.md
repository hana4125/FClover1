# OAuth 사용자 정보 등록
[https://velog.io/@yso8296/Spring-Security%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%E[…]EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84   ](https://velog.io/@yso8296/Spring-Security%EB%A5%BC-%EC%9D%B4%EC%9A%A9%ED%95%9C-%ED%86%B5%ED%95%A9-OAuth2-%EC%86%8C%EC%85%9C-%EB%A1%9C%EA%B7%B8%EC%9D%B8-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84)

위 링크 보고 구글 CLIENT_ID, CLIENT_SECRET / 네이버 CLIENT_ID, CLIENT_SECRET 받아오기

프로젝트 루트 디렉토리에 .env 파일 만들기 (pom.xml과 같은 위치)

```
GOOGLE_CLIENT_ID=받아온 아이디
GOOGLE_CLIENT_SECRET=받아온 시크릿
NAVER_CLIENT_ID=받아온 아이디
NAVER_CLIENT_SECRET=받아온 시크릿
```
env 파일에 내용 넣기


<결제/배송관련 정책>
1. 부분취소 케이스는 고려하지 않음
2. 별도의 입고로직은 고려하지 않음. default 재고 100개로 넣어줄 예정.


# Git 명령어

```shell
$ git clone # 프로젝트 소스 불러오기

$ git fetch # 프로젝트 최신화 

$ git checkout -b dev origin/dev # dev 브랜치 생성 및 이동

$ git branch # 브랜치 확인 ( dev, prod 두 개의 브랜치가 있어야 함 )

$ git add . #  Git에서 현재 디렉토리와 하위 디렉토리에 있는 변경된 모든 파일을 스테이징 영역에 추가하는 명령

$ git commit -m "커밋메시지" # 깃 커밋

$ git push -u origin 작업중인브랜치명 # 깃 push

```

# Commit Message Convention


```shell
1. feat
- ex). “[user_payments] feat : BO 판매자 정보조회 페이지 구현”

2. fix

3. chore
- ex). “[CJH] chore : 카테고리 버튼 추가”

4. test

5. refac

6. css
- ex). “[user_payments] css : 주문상세 css 수정”

7. js

```

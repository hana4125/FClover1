#!/usr/bin/env bash

REPOSITORY=/home/ubuntu/fclover
cd $REPOSITORY

APP_NAME=java
#JAR_NAME=$(ls $REPOSITORY/target/ | grep 'SNAPSHOT.jar' | tail -n 1)
JAR_NAME=$(find $REPOSITORY/target/ -name "*.*-SNAPSHOT.jar" | sort | tail -n 1)
JAR_PATH=$REPOSITORY/target/$JAR_NAME

CURRENT_PID=$(pgrep -f $APP_NAME)

if [ -z "$CURRENT_PID" ];
then
  echo "> 종료할 애플리케이션이 없습니다."
else
  echo "> kill -9 $CURRENT_PID"
  kill -15 "$CURRENT_PID"
  sleep 5
fi



echo "> Deploy - $JAR_NAME "

# .env 파일을 읽고 환경 변수 설정
if [ -f .env ]; then
  export $(cat .env | xargs)
fi

# .env 파일에서 JASYPT_ENCRYPTOR_PASSWORD가 설정되었는지 확인
echo "> Using JASYPT_ENCRYPTOR_PASSWORD: $JASYPT_ENCRYPTOR_PASSWORD"

#
#source ~/.bashrc
#
##nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
##java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
##nohup java -jar $JAR_PATH > /home/ubuntu/deploy.log 2>&1 &
##nohup java -jar $JAR_NAME > /home/ubuntu/deploy.log 2>&1 &
nohup java -jar $JAR_NAME > /home/ubuntu/deploy.log 2>&1 &

echo "> 배포 완료"

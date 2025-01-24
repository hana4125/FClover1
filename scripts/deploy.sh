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
#nohup java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
#java -jar $JAR_PATH > /dev/null 2> /dev/null < /dev/null &
#nohup java -jar $JAR_PATH > /home/ubuntu/deploy.log 2>&1 &
#nohup java -jar $JAR_NAME > /home/ubuntu/deploy.log 2>&1 &
nohup java -jar $JAR_NAME > /home/ubuntu/deploy.log 2>&1 &

#!/bin/bash

#VARIABLES
KEY="@key@"
SERVICE_NAME="@serviceName@"
FOLDER="@folder@"
LOGS_FOLDER="@logsFolder@"
USERDEPLOYER="@user@"
JAVA_PATH="@javaPath@"
MAIN_CLASS="@mainClass@"
JVM_OPTS_STRING="@jvmOpts@"
CLASSPATH="@classpath@"

. $FOLDER/bootstrap/utils.sh

function splitJvmOpts() {
    JVM_OPTS=("$@")
}
eval splitJvmOpts $JVM_OPTS_STRING

#CHECKING SYSTEM STATUS
processPID=$(getProcessPID)

if [ $processPID ]; then
  echo -n "$SERVICE_NAME already is running. Stop it!"
  echo_failure
  echo
  exit
fi

if [[ `/usr/bin/whoami` != $USERDEPLOYER ]]; then
    echo -n "Permission denied. User must be $USERDEPLOYER!"
    echo_failure
    echo
    exit
fi

pushd . >> /dev/null # saving current directory
echo -n "Starting $SERVICE_NAME: "
cd $FOLDER/bootstrap
nohup $JAVA_PATH ${JVM_OPTS[@]} -classpath "$CLASSPATH" $MAIN_CLASS $KEY "$@" >> $LOGS_FOLDER/stdout.log 2>> $LOGS_FOLDER/stderr.log &
sleep 2

processPID=$(getProcessPID)

if [ -n "$processPID" ] && [ "$processPID" != "" ]
then
    echo_success
    echo
else
    echo_failure
    echo
fi

popd >> /dev/null


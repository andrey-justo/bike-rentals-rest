#!/bin/bash

#VARIABLES
KEY="@key@"
SERVICE_NAME="@serviceName@"
FOLDER="@folder@"
USERDEPLOYER="@user@"

. $FOLDER/bootstrap/utils.sh

if [[ `/usr/bin/whoami` != $USERDEPLOYER ]]; then
    echo -n "Permission denied. User must be $USERDEPLOYER!"
    echo_failure
    echo
    exit
fi

pushd . >> /dev/null # saving current directory
echo -n "Stopping $SERVICE_NAME: "

for i in {1..15}
do
   processPID=$(getProcessPID)
   if [ -z $processPID ]; then
       echo_success
       echo
       exit
   fi
   kill $processPID
   sleep 1
done

processPID=$(getProcessPID)
if [ -n "$processPID" ] && [ "$processPID" != "" ]; then
    kill -9 $processPID
    echo_success
    echo
    echo "(Killed using SIGKILL)"
else
    echo_success
    echo
fi

popd >> /dev/null

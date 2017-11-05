#!/bin/sh

SERVICE_NAME="@serviceName@"
FOLDER="@folder@/bootstrap"
SERVICE_USER="@user@"

start() {

if [[ `/usr/bin/whoami` == $SERVICE_USER ]]
then
 cd $FOLDER
 ./start.sh

#NOT USER deployer
else
 cd $FOLDER
 su $SERVICE_USER ./start.sh
fi
}

stop() {

 cd $FOLDER
 su $SERVICE_USER ./stop.sh
}

#Body main
case "$1" in
    start)
        start
        ;;
    stop)
        stop
        ;;
    restart)
        stop
	    sleep 1
	    start
	    ;;
    *)
        echo $"Usage: $0 {start|stop|restart}"
        exit 1
esac
exit 0

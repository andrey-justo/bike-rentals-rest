#!/bin/bash

RES_COL=60
MOVE_TO_COL="\\033[${RES_COL}G"
SETCOLOR_SUCCESS="\\033[0;32m"
SETCOLOR_FAILURE="\\033[0;31m"
SETCOLOR_WARNING="\\033[0;33m"
SETCOLOR_NORMAL="\\033[0;39m"

echo_success() {
  echo -en "$MOVE_TO_COL"
  echo -n "["
  echo -en "$SETCOLOR_SUCCESS"
  echo -n "  OK  "
  echo -en "$SETCOLOR_NORMAL"
  echo -n "]"
  echo -ne "\r"
  return 0
}

echo_failure() {
  echo -en "$MOVE_TO_COL"
  echo -n "["
  echo -en "$SETCOLOR_FAILURE"
  echo -n "FAILED"
  echo -en "$SETCOLOR_NORMAL"
  echo -n "]"
  echo -ne "\r"
  return 1
}

function getProcessPID() {
    echo $(ps -ef | grep $KEY | grep -v grep | awk -F" " '{ print $2 }')
}
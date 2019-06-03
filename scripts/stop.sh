#!/bin/bash

stop-yarn.sh

hadoop fs -rm -r -f /task1 /task2 /task3

stop-dfs.sh

sed -i s/$HOSTNAME/master/ $HADOOP_CONF_DIR/core-site.xml
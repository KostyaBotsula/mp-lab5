#!/bin/bash

chmod +x /java/lab5-1.0.jar

sed -i s/master/$HOSTNAME/ $HADOOP_CONF_DIR/core-site.xml

${HADOOP_HOME}/sbin/start-all.sh

cd /java && \
    wget ftp://ita.ee.lbl.gov/traces/NASA_access_log_Jul95.gz && \
    gzip -d NASA_access_log_Jul95.gz

hadoop fs -mkdir /logs
hadoop fs -put /java/NASA_access_log_Jul95 /logs
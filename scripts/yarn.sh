#!/bin/bash

sh start.sh

start-yarn.sh

spark-submit --master yarn \
             --deploy-mode client \
             --class com.botsula.Application \
             /java/lab5-1.0.jar master /logs
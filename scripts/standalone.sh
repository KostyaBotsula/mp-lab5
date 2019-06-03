#!/bin/bash

sh start.sh

spark-submit --master spark://master:7077 \
             --class com.botsula.Application /java/lab5-1.0.jar master /logs
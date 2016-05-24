#!/bin/bash

#
# Additional options:
# -DcompanyId=<name> - eg. APPL, INTC, MS
# -Dbrokers=<list_of_brokers> - eg. localhost:9092
# -DstockThreshold=<threshold> - eg. 30.5
#
# ./subscriber-start.sh -DcompanyId=INTC -Dbrokers="localhost:9092,localhost:9093" -DstockTreshold=30.5
#

java $@ -jar consumer/target/scala-2.11/consumer-assembly-0.1-SNAPSHOT.jar

#!/bin/bash

#
# -DcompanyId=<company_id> - id of the company, e.g. INTC
# -DinitialValue=<value> - initial stock value
# -Dbrokers="localhost:9092"
#
# ./publisher-start.sh -DcompanyId=INTC -Dbrokers="localhost:9092,localhos:9093" -DinitialValue=30
#

java $@ -jar producer/target/scala-2.11/producer-assembly-0.1-SNAPSHOT.jar

# kafka-intc-feeds
Kafka playgroud with a little demo app playing around reactive-kafka (https://github.com/akka/reactive-kafka)

#### Building
In project root directory:
```bash
sbt assembly
```

#### Launching producer
e.g.
```bash
./publisher-start.sh -DcompanyId=INTC -Dbrokers="localhost:9092,localhost:9093" -DinitialValue=30
```

#### Launching consumer
e.g.
```bash
./subscriber-start.sh -DcompanyId=INTC -Dbrokers="localhost:9092,localhost:9093" -DstockTreshold=30.5
```

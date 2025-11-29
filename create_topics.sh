docker exec -it shokoku-board-kafka \
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic shokoku-board-article --replication-factor 1 --partitions 3

docker exec -it shokoku-board-kafka \
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic shokoku-board-comment --replication-factor 1 --partitions 3

docker exec -it shokoku-board-kafka \
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic shokoku-board-like --replication-factor 1 --partitions 3

docker exec -it shokoku-board-kafka \
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 \
  --create --topic shokoku-board-view --replication-factor 1 --partitions 3



docker exec -it shokoku-board-kafka \
  /opt/kafka/bin/kafka-topics.sh --bootstrap-server localhost:9092 --list

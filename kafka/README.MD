### Setup Zookeeper and Kafka Server
* Start Zookeeper: bin/zookeeper-server-start.sh config/zookeeper.properties
* Start Kafka: bin/kafka-server-start.sh config/server.properties
* Create a Topic: bin/kafka-topics.sh --create --topic test-topic --bootstrap-server localhost:9092 --partitions 1 --replication-factor 1
* List Topics: bin/kafka-topics.sh --list --bootstrap-server localhost:9092
* Produce Messages: bin/kafka-console-producer.sh --topic test-topic --bootstrap-server localhost:9092 
"Type messages in the terminal to produce them."
* Consume Messages: Open another terminal and run:
bin/kafka-console-consumer.sh --topic test-topic --from-beginning --bootstrap-server localhost:9092
* Stop Kafka: bin/kafka-server-stop.sh
* Stop Zookeeper: bin/zookeeper-server-stop.sh

### Kafka System Workflow:
* Producer sends messages to a topic (specific partitions).
* The broker receives and stores the message.
* Messages are replicated across brokers for fault tolerance.
* Consumer groups subscribe to topics and consume messages in parallel.
* Kafka maintains the offset for each consumer.

### Kafka Components:
1. Kafka Broker: A server that stores and delivers messages.
"Think of it as a post office: it keeps letters (messages) until someone (a consumer) picks them up."
2. Topics : A category or "mailbox" where messages are sent.
"Example: A topic might be named "orders" where all order-related messages are stored."
3. Partitions : A topic is divided into smaller chunks (partitions) for better performance.
"Like splitting your "orders" mailbox into multiple sections (e.g., based on regions)."
4. Producers : Applications that send messages to Kafka.
"Like people sending letters to the mailbox (topic)."
5. Consumers : Applications that read messages from Kafka.
"Like people picking up letters from the mailbox (topic)."
6. Consumer Groups : A group of consumers working together to read messages.
"Like multiple workers sharing the workload of reading letters from the mailbox."
7. Partition Offset : A unique number for each message in a partition.
"Think of it as the "page number" of a book so you know where you left off."
8. Replication : Copies of partitions stored on other brokers for safety.
"Like making duplicates of important letters to avoid losing them."
9. Kafka Connect : A tool to link Kafka with other systems (e.g., databases).
"Like a conveyor belt moving data between Kafka and other places."
10. Kafka Streams : A tool for processing data directly in Kafka.
"Like sorting and analyzing letters while they're still in the mailbox."
11. Schema Registry : A system to manage the format (schema) of messages.
"Like setting a rule: 'All letters must be written in English.' "
12. Zookeeper (Legacy): A tool Kafka used to manage its cluster (now being replaced).
"Like a manager who keeps track of all post offices and ensures everything runs smoothly."
13. Admin Client : A way to manage Kafka (create/delete topics, check cluster info).
"Like being able to open and organize new mailboxes."
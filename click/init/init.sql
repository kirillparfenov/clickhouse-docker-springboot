CREATE DATABASE curdb;
USE curdb;

CREATE TABLE IF NOT EXISTS kafka_queue
(
    message String
) ENGINE = Kafka SETTINGS kafka_broker_list = 'kafka-1:9092,kafka-2:9093,kafka-3:9094',
    kafka_topic_list = 'clickhouse_topic',
    kafka_group_name = 'clickhouse_group',
    kafka_format = 'CSV',
    kafka_max_block_size = 1048576,
    kafka_commit_every_batch = 0,
    kafka_num_consumers = 10;

CREATE TABLE IF NOT EXISTS kafka_queue_data
(
    message String
) Engine = MergeTree()
      ORDER BY message;

CREATE MATERIALIZED VIEW kafka_queue_materialized TO kafka_queue_data AS
SELECT message
FROM kafka_queue;
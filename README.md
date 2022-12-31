## Clickhouse springboot example with Kafka

*Задача:*
- Есть Kafka топик **clickhouse_topic**. Средствами Clickhouse нужно сохранять сообщения из этого топика в таблицу **kafka_queue_data**. 
#
*Реализация:*
1) В ./click/init/init.sql для Clickhouse'а создаем прослушку топика  ->  **kafka_queue** и таблицу, в которую эти сообщения будут сохраняться -> **kafka_queue_data**;
2) Для автоматической перекачки сообщений нужно создать *MATERIALIZED VIEW* -> **kafka_queue_materialized**;
3) Запускаем **docker-compose up**;
4) После развертывания контейнеров **app** будет всаживать сообщения в топик **clickhouse_topic**, а Clickhouse автоматом сохранять их в kafka_queue_data;
5) Подключаемся к Clickhouse'у и смотрим, как постепенно сохраняются 100_000_000 сообщений в **kafka_queue_data**.

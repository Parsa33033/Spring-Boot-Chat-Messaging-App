# Spring-Boot-Chat-Messaging-App
A chat messaging app with Spring boot, RabbitMQ, Apache Cassandra, Redis, MySQL, Hibernate. Containing a minimal user interface.

## How to install
1) Install the following
* Maven
* RabbitMQ
* Apache Cassandra 
* Redis
* MySQL

after installing the services mentioned, set username and password for MySQL and RabbitMQ and configure the passwords in application.properties file in spring boot.

2) Install Docker

The docker-compose.yml file in ' src/main/resources ' contains:
* RabbitMQ
* Apache Cassandra 
* Redis
* MySQL

by running the command:

'''
[Spring-Boot-Chat-Messaging-App folder]/src/main/resources> Docker-compose up
'''

you can install all the services mentioned

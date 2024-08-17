<h1>Payment Service</h1>

### Tech Stack

<a href="#"><img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java"/></a>
<a href="https://spring.io"><img src="https://img.shields.io/badge/Spring_Boot-6cb52d?style=for-the-badge&logo=Spring&logoColor=white" alt="Spring"/></a>
<a href="https://redis.io"><img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white" alt="Redis"/></a>
  <a href="https://kafka.apache.org"><img src="https://img.shields.io/badge/Apache_Kafka-231F20?style=for-the-badge&logo=apache-kafka&logoColor=white" alt="Apache Kafka"/></a>
<a href="https://swagger.io"><img src="https://img.shields.io/badge/swagger-6cb52d?style=for-the-badge&logo=swagger&logoColor=white&titleColor=white" alt="Swagger"/></a>
<a href="https://www.postgresql.org"><img src="https://img.shields.io/badge/postgresql-4169e1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL"/></a>
<a href="https://www.docker.com"><img src="https://img.shields.io/badge/docker-257bd6?style=for-the-badge&logo=docker&logoColor=white" alt="Docker, Docker Compose"/></a>

### Description

This microservice, which is part of the Linq project, is responsible for managing user payments. Its main task is to process and integrate payments using the LiqPay service from PrivatBank.

#### The main functions of the microservice are:
#### 1. Requesting the shipping cost:
 - Receives order data, including amount, shipping and delivery address.
 - Performs a request to the delivery service to calculate the cost.
 - Adds the shipping cost to the order amount.
#### 2. Checkout Formation:
 - Based on the order data and shipping calculation, forms Checkout according to “LiqPay” service documentation.
#### 3. Payment notification:
 - After successful payment, the “LiqPay” service notifies the microservice.
 - The microservice publishes an Event to the Kafka topic, signaling the successful payment.
#### 4. Processing the delivery status:
 - Listens to the Kafka topic that receives messages from another “Deliveries” microservice.
 - As soon as it receives information that the recipient has picked up the order from the post office or refused it, the microservice performs the necessary payment or refund actions.

### Contact me:

<a href="https://www.linkedin.com/in/danyazero/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="Me in linkedIn"/></a>
<a href="mailto:danyamozzhukhin@gmail.com"><img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email"/></p>

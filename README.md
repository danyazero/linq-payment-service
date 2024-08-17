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

This microservice is part of the Linq project – and it's responsible for managing user payments. The main task is to process and integrate payments using the LiqPay service from PrivatBank.

1. The shipping cost is requested when the order is placed.
 - Requests delivery cost from delivery service.
 - Adds shipping cost to order total.
#### 2. Checkout Formation:
- Checkout is formed based on the order data and shipping calculation.
#### 3. Payment notification:
- After payment, the LiqPay service notifies the microservice.
 The microservice sends a notification to Kafka when a payment is successful.
#### 4. Processing the delivery status:
- Listens to Kafka for messages from another "Deliveries" microservice.
- As soon as it receives information about the order status, the microservice performs the necessary payment or refund actions.
### Contact me:

<a href="https://www.linkedin.com/in/danyazero/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" alt="Me in linkedIn"/></a>
<a href="mailto:danyamozzhukhin@gmail.com"><img src="https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white" alt="Email"/></p>

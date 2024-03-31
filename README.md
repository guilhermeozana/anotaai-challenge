# Catalog API
## AnotaAi Backend Challenge

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![MongoDB](https://img.shields.io/badge/MongoDB-%234ea94b.svg?style=for-the-badge&logo=mongodb&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)
[![Deploy Catalog API](https://github.com/guilhermeozana/anotaai-challenge/actions/workflows/prod.yml/badge.svg?branch=main)](https://github.com/guilhermeozana/anotaai-challenge/actions/workflows/prod.yml)

This project is an API built using **Spring Boot, MongoDB, AWS (EC2, SQS, SNS, Lambda and S3), Docker and CI/CD (Github Actions).**

The Microservice was developed to solve [AnotaAi Backend Challenge](https://github.com/githubanotaai/new-test-backend-nodejs).

## How to access
To access the endpoints of this project I provided the [Swagger UI](http://18.207.212.126:8080/swagger-ui/index.html) API documentation

## Application Metrics
In this project I used Spring Boot Actuator to exposure the metrics of the application and Prometheus with Grafana to show them. The dashboards can be accessed [here](http://18.207.212.126:3000/d/OS7-NUiGz/spring-boot-and-endpoint-metrics-2-0?orgId=1&refresh=10s&from=1711915599162&to=1711917399162). 

## Project Structure

![Diagrama sem nome drawio (3)](https://github.com/guilhermeozana/anotaai-challenge/assets/69025200/04c3df9c-b6b4-4fbb-a317-dede1f946093)






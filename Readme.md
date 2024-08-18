# LDAP Service

LDAP Service is a Spring Boot microservice designed to handle LDAP operations. This repository contains the source code, Docker configuration, and instructions on how to build and deploy the service using Docker.

## Prerequisites

Before you begin, ensure you have met the following requirements:

- **Java Development Kit (JDK)**: Version 17 or higher
- **Apache Maven**: For building the project
- **Docker**: Installed and running

## Getting Started

Follow these instructions to get a copy of the project up and running on your local machine.

### Cloning the Repository

First, clone the repository to your local machine:

```bash
git clone https://github.com/your-username/ldap-service.git
cd ldap-service
```
### Verify Java Version

Ensure that you have JDK 17 (or the version specified for this project) installed. You can check the version by running:

```bash
java -version
```
### Verify Maven Installation

```bash
mvn -v
mvn clean install
```
### Docker version

You can deploy the application as a Docker container by following these steps:
Before proceeding, ensure that Docker is installed and running on your machine. 
You can verify this by running:

```bash
docker --version
```

### Build the Docker Image
```bash
docker build -t ldap-service .
```

### Run the Docker Container
```bash
docker build -t ldap-service .
```
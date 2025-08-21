# SCAAS - Supply Chain Alert & Automation System

This repository contains the **SCAAS (Supply Chain Alert & Automation System)**, a Java-based backend service designed to automate the monitoring of and response to supply chain disruptions. It addresses key inefficiencies in logistics management by replacing manual, time-consuming tracking processes with an intelligent, event-driven system.

This service is the first component of a larger enterprise technology stack.

## Key Features

  * **Automated Shipment Monitoring**: Periodically polls simulated third-party logistics APIs to fetch the latest shipment statuses.
  * **Intelligent Alerting**: Detects critical disruptions (e.g., "Delayed," "Held at Customs") and triggers real-time email notifications.
  * **Automated Solution Workflow**: When a disruption is detected, the system automatically queries other logistics partners for alternative quotes, ranks them, and saves the best options for review.
  * **Idempotent Event Processing**: Uses Redis to ensure that the same shipment status update is never processed more than once, preventing duplicate alerts.
  * **REST API**: Provides endpoints for registering new shipments and retrieving generated solutions.

-----

## Tech Stack

  * **Backend**: Java 21, Spring Boot 3
  * **Data**: Spring Data JPA, PostgreSQL, Flyway Migrations
  * **Caching**: Spring Data Redis, Redis
  * **Scheduling & Events**: Spring Scheduler, Spring Application Events
  * **Build Tool**: Maven
  * **Containerization**: Docker & Docker Compose
  * **Local Email Testing**: MailHog

-----

## Getting Started

Follow these steps to get the SCAAS service running on your local machine.

### Prerequisites

  * Java 21
  * Maven
  * Docker and Docker Compose

### 1\. Clone the Repository

If you haven't already, clone the project to your local machine.

```bash
git clone <your-repository-url>
cd enterprise-stack
```

### 2\. Create the Environment File

The application requires a `.env` file in the root `enterprise-stack` directory to connect to the Docker services. Create a file named `.env` and paste the following content into it:

```
DB_URL=jdbc:postgresql://localhost:5432/enterprise
DB_USER=app
DB_PASS=app
MAIL_HOST=localhost
MAIL_PORT=1025
REDIS_HOST=localhost
REDIS_PORT=73
```

### 3\. Start the Infrastructure

This command will start the PostgreSQL database, Redis cache, and MailHog email server in the background.

```bash
docker compose up -d
```

### 4\. Run the Application

Use Maven to start the SCAAS service. The application will be available on `http://localhost:8080`.

```bash
mvn -pl scaas-service spring-boot:run
```

-----

## API Usage & Testing Flow

Once the application is running, you can use the following `curl` commands in a **new terminal** to test the end-to-end flow.

#### 1\. Register a New Shipment

This creates a new shipment with our "mock" carrier (which has an ID of 1). Note the `id` that is returned in the response.

```bash
curl -X POST http://localhost:8080/api/v1/shipments \
-H 'Content-Type: application/json' \
-d '{
  "trackingNumber": "ABC123XYZ",
  "carrierId": 1,
  "origin": "BLR",
  "destination": "SFO"
}'
```

#### 2\. Simulate a Delay

Use the `id` from the previous step to trigger the disruption workflow.

```bash
# Replace '1' with the ID you received
curl -X POST http://localhost:8080/api/v1/shipments/test/simulate-delay/1
```

#### 3\. Retrieve Automated Solutions

This action created an `Event` (with `id: 1`). Use this command to fetch the ranked list of alternative shipping options that were automatically generated.

```bash
curl http://localhost:8080/api/v1/events/1/solutions
```

You should see a JSON array containing quotes from the "beta" and "gamma" carriers.

#### 4\. Check for the Email Alert

Navigate to the MailHog dashboard in your browser to see the email alert that was sent.

**[http://localhost:8025](https://www.google.com/search?q=http://localhost:8025)**

# SHIELD Sleep Scoring Engine - Backend API

This repository contains the backend component for the SHIELD Sleep Scoring Engine, designed to calculate a SHIELD Sleep Score and biological age delta based on user-provided sleep data.

## Part 1: Backend - SHIELD Sleep Scoring Engine API (Java Spring Boot)

This is a REST API endpoint that calculates a SHIELD Sleep Score and biological age delta based on specified sleep parameters and rules.

### Architecture

* **Technology Stack:** Java 17, Spring Boot 3.2.5, Maven, Lombok.

* **API Design:** A RESTful API with a single POST endpoint for sleep score calculation.

* **Layered Architecture:**

    * **Controller (`SleepController.java`):** Handles incoming HTTP requests, performs basic input validation, and delegates business logic to the service layer. Includes CORS configuration for frontend integration.

    * **Service (`SleepService.java`):** Contains the core business logic for applying the scoring rules and calculating the biological age delta.

    * **Models (`SleepData.java`, `SleepScoreResponse.java`):** Plain Old Java Objects (POJOs) acting as Data Transfer Objects (DTOs) for request and response payloads.

* **In-Memory Database (H2):** While the current scoring logic is stateless and doesn't persist data, `spring-boot-starter-data-jpa` and `h2` are included in `pom.xml` to fulfill the requirement for an in-memory database setup, typical for Spring Boot applications. The H2 database is not actively used for persistence in this specific calculation.

* **Logging & Exception Handling:** Implemented using SLF4J and Logback (Spring Boot's default logging) to provide observability and graceful error handling.

### Assumptions

* **Stateless Calculation:** The scoring engine is designed as a stateless calculation. No historical sleep data is stored or retrieved from a database for the scoring itself.

* **Simplified Bio-Age Delta:** The biological age delta calculation is a simplified derivation based on the final SHIELD score for demonstration purposes. In a real-world scenario, this would involve complex biological models.

* **Fixed Scoring Rules:** The initial scoring rules are hardcoded as per the assignment.

* **CORS:** The backend is configured to allow requests from `http://localhost:3000` for local development. This origin should be updated for production deployments.

* **No Authentication/Authorization:** The API currently does not implement user authentication or authorization, as it's a standalone scoring engine for demonstration. This would be critical for a production system.

* **No Advanced Validation:** Basic input validation is present. For a production system, more robust validation (e.g., using Spring's `@Valid` and custom validators) would be necessary.

### How to Run and Test the System (Backend)

**Prerequisites:**

* Java Development Kit (JDK) 17 or higher

* Apache Maven

**Steps:**

1.  **Clone the repository:**

    ```
    git clone <your-backend-repo-url>
    cd shield-sleep-api
    ```

2.  **Build the project:**

    ```
    mvn clean install
    ```

3.  **Run the application:**

    ```
    mvn spring-boot:run
    ```

    The API will typically start on `http://localhost:8080`.

**Testing the API (using `curl`):**

Once the backend is running, you can test the endpoint using `curl` or a tool like Postman/Insomnia.

**Endpoint:** `POST http://localhost:8080/api/sleep/score`

**Example Request Body:**

```json
{
    "totalSleepHours": 5.5,
    "sleepEfficiency": 80.0,
    "remPercentage": 12.0,
    "age": 55,
    "sex": "male"
}
```
Example `curl` command:

```Bash

curl -X POST -H "Content-Type: application/json" -d '{ "totalSleepHours": 5.5, "sleepEfficiency": 80.0, "remPercentage": 12.0, "age": 55, "sex": "male" }' http://localhost:8080/api/sleep/score
```
**Expected Successful Response:**

```JSON

{
  "shieldScore": 70,
  "bioAgeDelta": "+2.5",
  "alerts": [
    "Insufficient total sleep hours",
    "Low sleep efficiency",
    "Low REM sleep percentage",
    "Age-related insufficient sleep"
  ],
  "suggestions": [
    "Aim for 7-9 hours of sleep per night for optimal health.",
    "Improve sleep efficiency by maintaining a consistent sleep schedule and creating a conducive sleep environment.",
    "To increase REM sleep, prioritize consistent sleep, reduce alcohol intake before bed, and manage stress.",
    "Older adults may require slightly less sleep, but consistently less than 6 hours can still be detrimental. Consult a doctor if sleep issues persist."
  ]
}
```

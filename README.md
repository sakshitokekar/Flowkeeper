# Flowkeeper

Flowkeeper is a Spring Boot break reminder app.
It serves a break page, opens it on a schedule, and triggers system beeps during the final seconds of the timer.

## Demo

![Flowkeeper demo](assets/FlowKeeperDemo.gif)

## Project Layout

- `server/`: Spring Boot backend + static `break.html`
- `client/`: currently empty

## Prerequisites

- Java 21+ (project target is Java 21)
- Maven Wrapper (`./mvnw`, already included)

## Run the App

From the project root:

```bash
cd server
./mvnw spring-boot:run
```

App URL:

- `http://localhost:8080/break.html`

## API

Endpoint:

- `POST /api/sound/schedule-last-five?durationSeconds=<seconds>`

Example:

```bash
curl -X POST "http://localhost:8080/api/sound/schedule-last-five?durationSeconds=10"
```

## How It Works

- `FlowkeeperService` opens the break page on a fixed schedule.
- `break.html` starts a countdown and calls the sound endpoint.
- The backend schedules beeps for the last 5 seconds (or less for short durations).

## Troubleshooting

### Port 8080 already in use

Find what is using port 8080:

```bash
lsof -nP -iTCP:8080 -sTCP:LISTEN
```

Stop the process:

```bash
kill <PID>
```

If needed:

```bash
kill -9 <PID>
```

Or run Flowkeeper on another port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### `mvn test` crashes on Java 22

If tests crash with JVM abort / Mockito self-attach warnings, try Java 21 for test runs:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./mvnw test
```

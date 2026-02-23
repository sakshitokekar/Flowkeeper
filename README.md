# Flowkeeper

**"Sitting is the new smoking."**

Flowkeeper is a productivity-focused Spring Boot application that automatically initiates timed micro-breaks, opens a guided break screen, and schedules end-of-break audio cues.

It was built to demonstrate practical backend engineering: scheduling, async execution, API design, static asset delivery, and resilient fallbacks for local desktop environments.

## Demo

<img src="assets/FlowkeeperDemo.gif" alt="Flowkeeper demo" width="100%" />

## Why This Project Stands Out

- Designed around a real workflow problem: avoiding long, uninterrupted focus sessions.
- Combines backend scheduling + frontend UX in one deployable service.
- Includes failure-tolerant behavior (desktop/audio fallback paths).
- Shows clean separation of concerns between controller, service, and static UI.

## Tech Stack

- Java 21 target
- Spring Boot 4 (Web MVC, Scheduling)
- Maven Wrapper
- Vanilla HTML/CSS/JavaScript frontend served from Spring static resources

## Architecture

```text
Scheduler (@Scheduled)
      |
      v
FlowkeeperService -----------------> Opens break page in browser
      |
      +-----> schedules async beeps (last N seconds)
                       |
                       v
                 System audio / terminal bell fallback

break.html (static UI) ---> POST /api/sound/schedule-last-five
```

## Core Features

- Auto-opens a break page on a fixed cadence.
- Displays a visual countdown with animated UI and wellness prompts.
- Calls backend endpoint to schedule last-five-second beep alerts.
- Safely handles short durations by clamping values.
- Shuts down executor resources gracefully on app termination.

## API

`POST /api/sound/schedule-last-five?durationSeconds=<seconds>`

Example:

```bash
curl -X POST "http://localhost:8080/api/sound/schedule-last-five?durationSeconds=10"
```

Response:

- `202 Accepted` when beep scheduling is queued.

## Local Setup

Prerequisites:

- Java 21+ installed
- Shell with execution rights for `server/mvnw`

Run:

```bash
cd server
./mvnw spring-boot:run
```

Open:

- `http://localhost:8080/break.html`

## Repository Layout

- `server/src/main/java/com/example/Flowkeeper/FlowkeeperApplication.java`: app bootstrap + scheduling enablement
- `server/src/main/java/com/example/Flowkeeper/FlowkeeperService.java`: scheduling, browser open, beep executor
- `server/src/main/java/com/example/Flowkeeper/SoundController.java`: REST endpoint for beep scheduling
- `server/src/main/resources/static/break.html`: break UI, timer, API trigger

## Engineering Notes

- Uses a dedicated `ScheduledExecutorService` for non-blocking beep scheduling.
- Beep logic is bounded (`min(5, duration)`), preventing invalid ranges.
- Browser/audio calls include fallback handling to keep the app running in constrained environments.

## Troubleshooting

### Port `8080` already in use

```bash
lsof -nP -iTCP:8080 -sTCP:LISTEN
kill <PID>
```

Run on another port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=9090
```

### Test crash on Java 22 (Mockito self-attach warning)

Use Java 21 for test runs:

```bash
export JAVA_HOME=$(/usr/libexec/java_home -v 21)
./mvnw test
```

## Next Improvements

- Add configurable break cadence/duration via properties or UI.
- Persist user preferences.
- Add unit tests for scheduler/beep timing logic.
- Package with Docker for one-command onboarding.

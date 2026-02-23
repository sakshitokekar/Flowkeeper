# Flowkeeper

**"Sitting is the new smoking."**

Flowkeeper is a productivity-focused Spring Boot application that automatically initiates timed micro-breaks, opens a guided break screen, and schedules end-of-break audio cues.

It was built to show practical software engineering: turning a simple health idea into a clean, working product.

## Demo

![Flowkeeper demo](assets/FlowkeeperDemo.gif)
## Why This Project Stands Out

- Designed around a real workflow problem: avoiding long, uninterrupted focus sessions.
- Combines backend scheduling + frontend UX in one deployable service.
- Includes failure-tolerant behavior (desktop/audio fallback paths).
- Shows clean separation of concerns between controller, service, and static UI.

## What I Built

- Built automatic break reminders that launch on a fixed schedule.
- Added a visual break screen with countdown, wellness prompts, and a demo-friendly UI.
- Connected frontend and backend with a simple API for audio reminders.
- Added safeguards so the app behaves well with short or unexpected timer values.
- Structured the code cleanly so features can be extended later.

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

## Author

Sakshi Tokekar

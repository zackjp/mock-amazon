---
name: best-practices-android
description: Sets a reasonable bar of best practices in Android development for agents to follow.
metadata:
  author: zackjp
  last-updated: '2026-06-22'
  keywords:
  - android
  - refactor
  - review
  - best practices
  - coding standards
  - coding guidelines
  - implementation
---

## Overview

This skill sets a reasonable bar of best practices in Android development for agents to follow.

---

### Guidelines

- Expose any `StateFlow`s to the UI via `stateIn()` so that they unsubscribe after the UI is inactive for more than 5 seconds
- Prefer that data access classes are singleton-scoped so that the application has a single source of truth

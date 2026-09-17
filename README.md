CodeAlpha\_JavaGradleApp

# Endpoint Monitor
===

# 

# \[!\[CI](https://github.com/arjunpatilakp04-cell/CodeAlpha\_JavaGradleApp/actions/workflows/ci.yml/badge.svg)](https://github.com/arjunpatilakp04-cell/CodeAlpha\_JavaGradleApp/actions/workflows/ci.yml)

# \[!\[CD](https://github.com/arjunpatilakp04-cell/CodeAlpha\_JavaGradleApp/actions/workflows/cd.yml/badge.svg)](https://github.com/arjunpatilakp04-cell/CodeAlpha\_JavaGradleApp/actions/workflows/cd.yml)

# \[!\[Java](https://img.shields.io/badge/Java-21-orange)](https://openjdk.org/)

# \[!\[Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen)](https://spring.io/projects/spring-boot)

# \[!\[Gradle](https://img.shields.io/badge/build-Gradle-blue)](https://gradle.org/)

# 

# > A Spring Boot REST service, containerized with Docker, built and deployed through a fully automated GitHub Actions CI/CD pipeline to GitHub Container Registry (GHCR), with a full Prometheus + Grafana monitoring stack.

# 

# > \*\*Note:\*\* CI/CD badges only render for external viewers once this repo's visibility is set to public (Settings → General → Danger Zone → Change visibility).

# 

# \---

# 

# \## Overview

# 

# This project demonstrates a complete, real-world DevOps workflow around a Java web application:

# 

# \- \*\*Build automation\*\* with Gradle

# \- \*\*Automated testing\*\* on every push (JUnit 5 + Testcontainers against a real Postgres instance)

# \- \*\*Containerization\*\* with Docker

# \- \*\*Continuous Integration\*\* — tests run on every push

# \- \*\*Continuous Delivery\*\* — Docker images built and pushed to GHCR automatically on every merge to `main`

# \- \*\*Observability\*\* — live application metrics exposed via Spring Boot Actuator, scraped by Prometheus, and visualized in a Grafana dashboard

# 

# \## Architecture

# 

# ```mermaid

# flowchart LR

# &#x20;   Dev\[Developer] -->|git push| GH\[GitHub Repo]

# &#x20;   GH -->|triggers| CI\[CI Workflow]

# &#x20;   CI -->|on merge to main| CD\[CD Workflow]

# &#x20;   CD -->|docker build \& push| GHCR\[(GHCR<br/>ghcr.io)]

# &#x20;   GHCR -->|docker pull| Deploy\[Running Container]

# 

# &#x20;   subgraph "Local / Deployed Stack"

# &#x20;       App\[Spring Boot App :8080]

# &#x20;       DB\[(PostgreSQL :5432)]

# &#x20;       App --> DB

# &#x20;       App -->|/actuator/prometheus| Prom\[Prometheus :9090]

# &#x20;       Prom --> Graf\[Grafana :3000]

# &#x20;   end

# 

# &#x20;   Deploy --> App

# ```

# 

# \## Tech Stack

# 

# | Layer            | Technology                                        |

# |-------------------|----------------------------------------------------|

# | Language           | Java 21                                             |

# | Framework          | Spring Boot (Web, Data JPA, Validation, Actuator)   |

# | Database            | PostgreSQL                                          |

# | Build tool          | Gradle                                              |

# | Containerization    | Docker \& Docker Compose                             |

# | CI/CD               | GitHub Actions                                      |

# | Image registry      | GitHub Container Registry (GHCR)                    |

# | Metrics             | Micrometer + Prometheus                             |

# | Dashboards          | Grafana (Spring Boot APM dashboard)                 |

# | Testing             | JUnit 5, Testcontainers                             |

# 

# \## CI/CD Pipeline

# 

# \*\*CI\*\* (`.github/workflows/ci.yml`) — runs on every push/PR: compiles the project and runs the full test suite.

# 

# \*\*CD\*\* (`.github/workflows/cd.yml`) — runs on every push to `main`: re-runs the build, logs into GHCR, and builds + pushes a Docker image tagged both `latest` and with the short commit SHA.

# 

# Pull the latest image:

# 

# ```bash

# docker pull ghcr.io/arjunpatilakp04-cell/codealpha\_javagradleapp:latest

# ```

# 

# \## Running Locally

# 

# Spins up the app, Postgres, Prometheus, and Grafana together:

# 

# ```bash

# docker compose up -d --build

# ```

# 

# | Service      | URL                                     |

# |--------------|-------------------------------------------|

# | App           | http://localhost:8080                      |

# | Health check  | http://localhost:8080/actuator/health       |

# | Metrics (raw) | http://localhost:8080/actuator/prometheus   |

# | Prometheus    | http://localhost:9090                       |

# | Grafana       | http://localhost:3000 (login: `admin` / `admin`) |

# 

# \## Monitoring \& Metrics

# 

# The app exposes Prometheus-formatted metrics at `/actuator/prometheus` (JVM heap/non-heap memory, CPU usage, load average, HikariCP connection pool stats, HTTP request metrics, and more, via Micrometer).

# 

# Prometheus scrapes this endpoint every 15s (see `monitoring/prometheus.yml`) and is automatically wired into Grafana as a datasource (see `monitoring/grafana/provisioning/datasources/datasource.yml`).

# 

# \*\*To view the dashboard:\*\*

# 1\. Open Grafana at `localhost:3000`, log in with `admin` / `admin`

# 2\. Go to \*\*Dashboards → New → Import\*\*

# 3\. Enter dashboard ID \*\*`12900`\*\* ("SpringBoot APM Dashboard") → Load

# 4\. Select \*\*Prometheus\*\* as the data source → Import

# 

# You'll get live panels for uptime, heap/non-heap usage, CPU usage, load average, open files, and more.

# 

# \## Project Structure

# 

# ```

# .

# ├── src/                                     # Application source + tests

# ├── monitoring/

# │   ├── prometheus.yml                       # Prometheus scrape config

# │   └── grafana/provisioning/

# │       └── datasources/datasource.yml       # Grafana → Prometheus datasource

# ├── docker-compose.yml                       # Full local stack (app + db + monitoring)

# ├── Dockerfile                               # App container image

# ├── build.gradle.kts                         # Build config \& dependencies

# └── .github/workflows/

# &#x20;   ├── ci.yml                               # Test automation

# &#x20;   └── cd.yml                               # Build \& push to GHCR

# ```

# 

# \## License

# 

# MIT


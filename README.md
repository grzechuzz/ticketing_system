 # 🎟️ eTicket 🎟️

**A ticketing system with intelligent seat validation powered by a SAT solver.**

Built as a Software Engineering course project. The goal was to design and implement a slice of a ticketing system where the core feature is a mechanism that prevents single-seat gaps from forming in rows. 
The frontend serves as a demo UI to visualize the validation logic, while the primary focus was on the backend.

The full Software Requirements Specification is included in the repository: [`eTicket_SRS.md`](eTicket_SRS.md) (in Polish). This project implements only a subset of the features described there.

## Implemented Features

**Seat validation (SAT solver)**
- Single-seat gap detection in rows
- Alternative seat suggestions from current and neighboring rows
- Automatic acceptance when no better options exist

**Authentication and authorization**
- Registration and login with JWT
- Refresh token rotation with SHA-256 hashing
- Roles: customer, organizer, administrator

**Event management**
- Event listing
- Event details with sector map (seated/standing)
- Seat map per sector with occupied seat indicators

**Purchase flow**
- Shopping cart with timed reservation (20 minutes)
- Adding seated tickets (with SAT validation)
- Adding standing tickets (with capacity management)
- Removing items from cart
- Checkout with ticket generation

**Tickets**
- PDF generation with QR code

## SAT Solver - Seat Validation

**Problem**: When buying tickets, customers tend to leave single empty seats (e.g. buying seats 1-2 and 4-5, leaving a gap at seat 3). These seats are really hard to sell afterwards.

**Solution**: Seat selection is validated as a Boolean Satisfiability Problem (SAT). Each seat in a row is a boolean variable (occupied/free). The rule is expressed as a CNF formula:

```
For every three adjacent seats (left, middle, right): (-left OR middle OR -right)
```

This clause is unsatisfied only when left=occupied, middle=free, right=occupied - exactly when a single-seat gap exists. The solver checks whether the entire formula remains satisfiable after adding the selected seats.

If validation fails, the system searches the current and neighboring rows and returns alternative seat suggestions that don't create gaps.

**Implementation**:
- `SeatGapValidator.java` - generates CNF clauses from sector layout, runs validation, searches for alternatives
- `SATSolver.java` - DPLL solver with backtracking (based on Software Archetypes)
- `Clause.java` - CNF clause representation (based on Software Archetypes)

Software Archetypes: [Software Archetypes - SAT](https://github.com/Software-Archetypes/archetypes/tree/main/configurator/src/main/java/softwarearchetypes/sat)

**And yeah... we realize that a simple for loop `O(n)` could solve this. But where's the fun in that? :D**

## Quick Start
```bash
cp .env.example .env
docker compose up -d --build
```

Frontend: http://localhost:3000

## Screenshots
<img width="1914" height="946" alt="image" src="https://github.com/user-attachments/assets/521eb600-efa4-43b2-b25d-3dd36bd7d374" />
<img width="1913" height="944" alt="image" src="https://github.com/user-attachments/assets/663c2f84-9ee4-42f3-9c4c-9d9973628762" />
<img width="1898" height="945" alt="image" src="https://github.com/user-attachments/assets/3352594a-4b49-4e33-85ef-46cd3d7ebec5" />
<img width="1898" height="942" alt="image" src="https://github.com/user-attachments/assets/3f27acd7-29dd-4621-a9ae-2df16c383486" />
<img width="1910" height="943" alt="image" src="https://github.com/user-attachments/assets/8fda498d-618f-4e10-9cd4-40f8e254c18d" />
<img width="1911" height="942" alt="image" src="https://github.com/user-attachments/assets/2503283e-e065-433b-b839-ab748ceff397" />
<img width="783" height="885" alt="image" src="https://github.com/user-attachments/assets/0817078f-9962-4f7a-9921-378d6ae618a7" />
<img width="1909" height="942" alt="image" src="https://github.com/user-attachments/assets/f870c1a1-d5bd-4dd2-9482-af00e6c5d363" />

## Tech Stack

**Backend:** Java 21, Spring Boot 4, Spring Security (JWT), JPA/Hibernate, MapStruct, Flyway, PostgreSQL 17

**Frontend:** React, Vite

**Testing:** JUnit

**Infrastructure:** Docker, GitHub Actions CI

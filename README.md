# CICD-1 Coding Test — Review, Improve and Reflect

**Student ID:** G00435140  
**Examination group:** B (OpenFeign)  
**Original examination pull-request URL (`exam-work`):** https://github.com/d4nv/G00435140/pull/1 (branch: controller, used in place of exam-work)  
**New review repository URL:** https://github.com/d4nv/CICD1_Review_G00435140

## 1. My original examination self-assessment

### Task 1 — /40
**My mark:** 18

**Evidence from the original code:** Booking.java had the four fields, both constructors and getters/setters, and I tried validation on every field. But it didn't compile: `string` instead of `String`, a wrong import, and no @Entity, @Id or id field. The validation annotations were wrong (@NotBlank on ints, @Min/@Max on the name) and not imported. No DTOs, and status was never set to ACTIVE.

### Task 2 — /40
**My mark:** 2

**Evidence from the original code:** I started a service and some custom exceptions, but the service was copied from another lab. It had syntax errors, called a method that doesn't exist and had no @Service. The controller was empty, so there was no POST endpoint. No repository, no Feign client, no capacity check, and no port 8081, H2 or Feign settings.

### Task 3 — /20
**My mark:** 0

**Evidence from the original code:** No GET /api/bookings/{id} endpoint. BookingNotFoundException existed but was never used.

**Total self-assessed original examination mark: 20/100**

## 2. Corrections and improvements in my new project

## 2. Corrections and improvements in my new project

**Problem and correction 1:** Getting the project set up and running took about half the exam. I didn't really understand the template, so I never got the app properly configured. application.properties had no port, H2 or Feign settings, and nothing was connected. In the new project I set up application.properties first (server.port=8081, the H2 datasource and the H2 console) and added @EnableFeignClients to BookingServiceApplication. I got it running before writing any other code, so every step after that could actually be tested.

**Problem and correction 2:** Booking.java didn't compile and the validation was in the wrong place. I wrote `string` instead of `String`, had no @Entity or @Id, put @NotBlank on ints and had no DTOs. Now model/Booking.java is an @Entity with an @Id Long id. dto/BookingRequest has @NotNull on roomId, @NotBlank and @Size(min=2, max=60) on organiserName, and @NotNull and @Positive on attendeeCount. The controller uses @Valid, so bad input gets a 400 with a clear message.

**Problem and correction 3:** There was no endpoint, no repository and no Feign client. My controller was empty, and the service was copied from an old lab, which went about as well as you'd expect. Now RoomClient calls the Room Service on 8082, BookingService checks the room capacity and saves the booking as ACTIVE, and GET /api/bookings/{id} reads from the local database.

## 3. API testing and evidence

**Test 1 — successful booking request:**
- Request / input: POST /api/bookings {"roomId":1, "organiserName":"Aoife Byrne", "attendeeCount":18}
- Expected: 201 Created, status ACTIVE
- Actual HTTP status / body: 201 {"id":1,"organiserName":"Aoife Byrne","attendeeCount":18,"roomId":1,"status":"ACTIVE"}
- Pass / fail: Pass

**Test 2 — invalid input or excessive attendee count:**
- Request / input: POST /api/bookings {"roomId":2, "organiserName":"Aoife Byrne", "attendeeCount":11} (room holds 10)
- Expected: 400 Bad Request
- Actual HTTP status / body: 400 {"error":"Attendee count 11 exceeds room capacity 10"}
- Pass / fail: Pass
- Extra: name "A" and attendeeCount 0 gave 400 with both field errors. Pass

**Test 3 — missing booking or room:**
- Request / input: GET /api/bookings/999
- Expected: 404 Not Found
- Actual HTTP status / body: 404 {"error":"Booking 999 not found"}
- Pass / fail: Pass
- Extra: POST with roomId 99 gave 404 {"error":"Room 99 not found"}. Pass

**Test 4 — requirement specific to my group:**
- Request / input: Room Service on 8082, Booking Service on 8081. The POST got Room 1 (capacity 24) through OpenFeign, then GET /api/bookings/1
- Expected: 200 OK, read from the Booking Service's own H2 database
- Actual HTTP status / body: 200 {"id":1,"organiserName":"Aoife Byrne","attendeeCount":18,"roomId":1,"status":"ACTIVE"} (the log shows a select from the local booking table)
- Pass / fail: Pass

## 4. Reflection

**What did I understand during the examination but struggle to implement?**  
I knew the Booking Service had to store bookings itself and ask the Room Service for room details. The problem was getting there. Setting up the project and getting everything running took about half the exam, and I didn't really understand the template, so I was still fighting the setup when the time ran out.

**Which Spring Boot, JPA, validation, JPQL or OpenFeign concept became clearer after correcting my code? Give a specific example.**  
Validation. It goes on the request DTO, not the entity. @NotBlank is only for text, numbers use @NotNull or @Positive, and none of it runs without @Valid. Sending attendeeCount 0 now gives a 400 with a clear message.

**What will I do differently in my next coding test or API development task?**  
Get the template running in the first few minutes, then build one layer at a time and run it after each step. I'll also practise setting up a Feign client before the next test, so I'm not learning it in the exam again.
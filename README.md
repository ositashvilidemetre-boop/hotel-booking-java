# Hotel Booking (Java)

A console application for searching and booking hotel rooms by city and dates, with switchable pricing policies and data saved between runs. Prog 2 OOP project.

## Prerequisites
- JDK 21 or newer
- Maven 3.9+ (or the Maven bundled with IntelliJ IDEA)

## Build and run
```
mvn clean package
java -cp target/classes com.hotelbooking.Main
```
Or open the project in IntelliJ IDEA and run `Main`.

## Sample usage
```
=== Hotel Booking ===
1. List hotels
2. Search available rooms
3. Book a room
4. List bookings
5. Cancel a booking
6. Change pricing policy
0. Save and exit
```
Example: choose `2`, city `Budapest`, dates `2026-12-20` and `2026-12-23`, sort `1`. Then choose `3`, room `201`, enter a name, email and the same dates. Choose `0` to save; the booking is still there on the next run.

Data is stored in `data.txt` (created automatically, not committed to Git).

## Implemented requirements
| Requirement | Where |
|---|---|
| Encapsulation | Private fields and validating constructors in `Room`, `Hotel`, `Guest`, `Booking` |
| Abstraction | Abstract class `Room`; interfaces `PricingStrategy` and `DataStorage` |
| Inheritance and polymorphism | `StandardRoom`, `DeluxeRoom`, `Suite` override `getNightlyPrice()`; used through `Room` |
| Relationships | `Hotel` *-- `Room` (composition, 1 to 1..*); `Booking` --> `Guest` and `Room` (association); `BookingService` o-- `Hotel`, `Booking` (aggregation) |
| Collections and generics | `List<Room>`, `List<Booking>`, `Map<String, Hotel>`; sorting with `Comparator` and lambdas |
| Exceptions | Custom `RoomNotAvailableException` (checked) and `InvalidInputException` (unchecked) |
| Persistence | `TextFileStorage` reads and writes `data.txt` with try-with-resources |
| Build | Maven project |

## Design pattern: Strategy
**Problem:** the price of a stay can follow different rules (normal, seasonal, long-stay discount), and rules change often. Hard-coding them in `if/else` blocks would force edits to the booking code every time.

**Participants:**
- `PricingStrategy` (interface) declares `calculateTotal` and `getName`
- `NormalPricing`, `SeasonalPricing`, `LongStayDiscountPricing` (concrete strategies)
- `BookingService` (context) holds a `PricingStrategy` and uses it in `book`; `setPricingStrategy` swaps it at runtime (menu option 6)

**Benefit:** a new pricing policy is one new class; `BookingService` does not change.

## Project structure
- `model`: domain classes
- `pricing`: Strategy pattern
- `service`: `BookingService`
- `persistence`: `DataStorage`, `TextFileStorage`
- `ui`: `ConsoleUI`, `InputReader`
- `exception`: custom exceptions
- `docs`: problem statement, user stories, UML diagram

## Design decisions
- Layers are separated: the UI only talks to the service and the storage interface.
- Storage is behind an interface so JSON or a database could replace the text file.
- Checked exception for an expected business case (room taken); unchecked for bad input.

## Known limitations
- Automated tests are not included (optional in the assignment).
- Text storage uses `;` as separator, so names must not contain `;`.
- Room numbers must be unique across hotels.
- No hotel/room management in the menu (sample data is seeded on first run).
- No payments, login, or GUI.

## Contributors
- Demetre Ositashvili
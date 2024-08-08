# Sample Code for Cardinal Financial Code Review

This project simulates a POS system for renting out tools.
Each tool type has an associated daily price and may or may not
exclude weekends and/or holidays (and possibly weekdays, but this scenario is unlikely)
based on the type of tool.

For example, a ladder may be rented out at 1.99 per day,
but may set up to not count observable holidays as rental days.
Whereas, a Chainsaw may be rented out at 1.49 per day, but would not be charged on weekend.

Possible discounts may be provided by the user of the POS system prior to creating the Rental Agreement.

In order for an agreement to be setup, a valid ToolCode, start date, and number of days are required by the customer.
The operator of the POS System may also add an optional discount in the system (0-100%, 0 meaning no discount; 100, well, we're giving it away...)

Once all the necessary information is in place, the POS operator will make the request of the system,
and the system will return a ServiceAgreement,
which will be printed out to the screen (other behavior, such as storing in a database, are
not currently implemented, but could be later on) for the operator and user to see.

## Possible Exceptions
1. IllegalToolCodeException
   1. Thrown if the ToolCode does not exist
2. IllegalDayCountException
   1. Thrown if the number of days to rent given in 0 or less
3. IllegalDiscountException
   1. Thrown if discount percentage amount is not a valid percentage (not within 0-100% range)

## System requirements

### JDK Version: 21+
### Annotation Processing Enabled in IDE

## Running tests
DISCLAIMER: <small>I added additional tests for the calculator test to cover all the exclusion logic.
I also ran the expected JUnit test to cover the end cases provided.
I added a script in the build.gradle file to print out the STD_OUT results of the tests. 
I cannot claim credit for this script, however.  It came from user `Shubham Chaudhary` at 
https://stackoverflow.com/questions/3963708/gradle-how-to-display-test-results-in-the-console-in-real-time </small>

### Windows
* To run all tests, from the root directory, type
  * `gradlew.bat clean test`
* To run just the integration test, type
  * `gradlew.bat clean test --tests 'sample.pos.MainIntegrationTest'`
### MacOS/Linux
* To run all tests, from the root directory, type
  * `./gradlew clean test`
* To run just the integration test, type
  * `./gradlew clean test --tests 'sample.pos.MainIntegrationTest'`



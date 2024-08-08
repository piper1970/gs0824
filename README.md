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

## Ideas For Improvement
The current state of the library is minimal, adhering to the spec.

Here are a number of additions that might make this more robust:
* Add a repo for RentalAgreements
  * Currently, a RentalAgreement is created, but not stored. A repo to hold the agreements would be more realistic
* Handling of check-in logic, which might include early returns and late returns
* Have a way to call this from the outside
  * Called directly, passing in parameters
    * Would require param parsing, especially for date
  * Call via web endpoint
    * _could_ be done by hand, but would be easier with a web framework
* Utilize Dependency Injection
  * Spring and Guice are few that come to mind
* Add service (assuming use of RentalAgreement Repo) to look for late returns
  * Could generate mock emails, add late charges to RentalAgreements, etc.


## Assumptions Made
The following assumptions have been made regarding the logic of the application
1. The checkout date counts as a chargeable day, but not the check-in date
   * If a check-in date lands on a holiday (or weekend), and the tool type does not charge for holidays (or weekends), it will have no effect on the calculated number of charge days.
   * If a checkout date lands on a holiday (or weekend), and the tool type does not charge for holidays (or weekends), it will have an effect on the calculated days, subtracting one day for the holiday.
2. Date formats are in __MM/DD/YY__ format.
   * This was the original spec for the printout, but the example dates for the unit tests parameters were in __M/D/YY__ format. I've chosen to adhere to the former format spec, __MM/DD/YY__.



Feature: U1 As Sarah, I want to register on Gardener’s Grove so that I can use its awesome features.

# Not sure how AC1 would be tested with cucumber tests. It would at least be very different to how
# the rest of the ACs are tested.

  Scenario Outline: AC2 - Registering as a new user with valid details
    Given I am on the registration form and enter first name <firstName> and last name <lastName>
    And I enter email <email>
    And I do not tick the checkbox for no last name
    And I enter password <password> and retype password <password>
    And I enter date of birth <dob>
    When I click the sign-up button
    Then I am successfully registered
    Examples:
      | firstName           | lastName | email               | password   | dob          |
      | "John"              | "Doe"    | "johndoe@gmail.com" | "Pass123!" | "12/12/2000" |
      | "John"              | "Doe"    | "johndoe@gmail.com" | "Pass123!" | ""           |
      | "John McGe'e-Smith" | "Doe"    | "johndoe@gmail.com" | "Pass123!" | "12/12/2000" |
      | "John McGe'e-Smith" | "Doe"    | "johndoe@gmail.com" | "Pass123!" | "12/12/2000" |
      | "Léo"               | "Dęõn"   | "leodeon@gmail.com" | "Pass123!" | "12/12/2000" |

  Scenario: AC3 - Registering as a new user with valid empty surname because I checked the box
    Given I am on the registration form and enter first name "John" and last name ""
    And I enter email "johndoe@gmail.com"
    And I tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am successfully registered

  Scenario Outline: AC4 - Registering as a new user with an invalid first/last name
    Given I am on the registration form and enter first name <firstName> and last name <lastName>
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message <errorMessage>
    Examples:
      | firstName   | lastName   | errorMessage                                                           |
      | ""          | "Doe"      | "First name cannot be empty"                                           |
      | "    "      | "Doe"      | "First name cannot be empty"                                           |
      | "John2"     | "Doe"      | "First name must only include letters, spaces, hyphens or apostrophes" |
      | "John$"     | "Doe"     | "First name must only include letters, spaces, hyphens or apostrophes" |
      | "John"      | ""         | "Last name cannot be empty"                                            |
      | "John"      | "   "      | "Last name cannot be empty"                                            |
      | "John"      | "Doe3"     | "Last name must only include letters, spaces, hyphens or apostrophes"  |
      | "John"      | "Doe%"     | "Last name must only include letters, spaces, hyphens or apostrophes"  |

  Scenario: AC5.1 - Registering as a new user with a valid length first/last name.
    Given I am on the registration form and enter a first name 64 characters long and a last name 64 characters long
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am successfully registered

  Scenario Outline: AC5.2 - Registering as a new user with an invalid length first/last name
    Given I am on the registration form and enter a first name <firstNameLength> characters long and a last name <lastNameLength> characters long
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message <errorMessage>
    Examples:
      | firstNameLength | lastNameLength | errorMessage                                    |
      | 65              | 3              | "First name must be 64 characters long or less" |
      | 4               | 65             | "Last name must be 64 characters long or less" |

  Scenario Outline: AC6 - Registering as a new user with invalid email
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email <email>
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "Email address must be in the form ‘jane@doe.nz’"
    Examples:
      | email                   |
      | "abc@123"               |
      | "bad"                   |
      | "email@email@email.com" |

#  TODO: I don't think it's possible to test this within the scope of .validate(), but we should still test this
#  Scenario: AC7 - Registering as a new user with an email that is already in use

  Scenario Outline: AC8 - Registering as a new user with invalid date format
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth <dob>
    When I click the sign-up button
    Then I am shown the error message "Date is not in valid format, DD/MM/YYYY"
    Examples:
      | dob           |
      | "abc"         |
      | "1/1/2000"    |
      | "05/13/2000"  |
      | "01/01/20000" |
      | "32/01/2000"  |
      | "29/02/2001"  |
      | "01-01-2000"  |

  Scenario: AC9.1 - Registering as a new user when I am old enough
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter a date of birth that means I turn 13 years old in 0 days
    When I click the sign-up button
    Then I am successfully registered

  Scenario: AC9.2 - Registering as a new user before I am old enough
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter a date of birth that means I turn 13 years old in 1 days
    When I click the sign-up button
    Then I am shown the error message "You must be 13 years or older to create an account"

  Scenario: AC10.1 - Registering as a new before I am too old
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter a date of birth that means I turn 121 years old in 1 days
    When I click the sign-up button
    Then I am successfully registered

  Scenario: AC10.2 - Registering as a new user when I am too old
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "Password!123"
    And I enter a date of birth that means I turn 121 years old in 0 days
    When I click the sign-up button
    Then I am shown the error message "The maximum age allowed is 120 years"

  Scenario: AC11 - Registering as a new user with different passwords
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password "Password!123" and retype password "SecondPassword!456"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "Passwords do not match"

  Scenario Outline: AC12 - Registering as a new user with a weak password
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I do not tick the checkbox for no last name
    And I enter password <password> and retype password <password>
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."
    Examples:
      | password              |
      | "abc"                 |
      | "Short1$"             |
      | "MissingANumber%"     |
      | "missing_uppercase1&" |
      | "missingSpecial1"     |
      | "MISSING_LOWERCASE1$" |

Feature: U1 - As Sarah, I want to register on Gardener’s Grove so that I can use its awesome features.

# No way to effectively test AC1

  Scenario Outline: AC2 - Registering as a new user with valid details
    Given I am on the registration form
    And I enter details <firstName>, <lastName>, a unique email <email>, and <dob> on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password <password> and retype password <password> on the register form
    When I click the sign-up button
    Then I am successfully registered
    Examples:
      | firstName            | lastName      | email                      | password   | dob          |
      | "Sarah"              | "Thompson"    | "sarahThompson1@gmail.com" | "Pass123!" | "12/12/2000" |
      | "Sarah"              | "Thompson"    | "sarahThompson2@gmail.com" | "Pass123!" | ""           |
      | "Sarah McGe'e-Smith" | "Thompson"    | "sarahThompson3@gmail.com" | "Pass123!" | "12/12/2000" |
      | "Sarah McGe'e-Smith" | "Thompson"    | "sarahThompson4@gmail.com" | "Pass123!" | "12/12/2000" |
      | "Sàrà"               | "Thómpsón"    | "sarahThompson5@gmail.com" | "Pass123!" | "12/12/2000" |

  Scenario: AC3 - Registering as a new user with valid empty surname because I checked the box
    Given I am on the registration form
    And I enter details "Sarah", "", a unique email "sarahThompson6@gmail.com", and "12/12/2000" on the register form
    And I tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    When I click the sign-up button
    Then I am successfully registered

  Scenario Outline: AC4 - Registering as a new user with an invalid first/last name
    Given I am on the registration form
    And I enter details <firstName>, <lastName>, a unique email "sarahThompson@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    When I click the sign-up button
    Then I am shown the error message <errorMessage> on the register form
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
#
  Scenario: AC5.1 - Registering as a new user with a valid length first/last name.
    Given I am on the registration form
    And I enter details "", "", a unique email "sarahThompson7@gmail.com", and "12/12/2000" on the register form
    And I enter a first name 64 characters long and a last name 64 characters long on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    When I click the sign-up button
    Then I am successfully registered
#
  Scenario Outline: AC5.2 - Registering as a new user with an invalid length first/last name
    Given I am on the registration form
    And I enter details "", "", a unique email "sarahThompson@gmail.com", and "12/12/2000" on the register form
    And I enter a first name <firstNameLength> characters long and a last name <lastNameLength> characters long on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    When I click the sign-up button
    Then I am shown the error message <errorMessage> on the register form
    Examples:
      | firstNameLength | lastNameLength | errorMessage                                    |
      | 65              | 3              | "First name must be 64 characters long or less" |
      | 4               | 65             | "Last name must be 64 characters long or less" |

  Scenario Outline: AC6 - Registering as a new user with invalid email
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email <email>, and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    When I click the sign-up button
    Then I am shown the error message "Email address must be in the form ‘jane@doe.nz’" on the register form
    Examples:
      | email                   |
      | "abc@123"               |
      | "bad"                   |
      | "email@email@email.com" |

  Scenario: AC7 - Cannot register with an existing email
    Given I am on the registration form
    And A user exists with email "benten@gmail.com" on the register form
    And I enter details "Ben", "Ten", a unique email "benten@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    When I click the sign-up button
    Then I am shown the error message "Email address is already in use" on the register form

  Scenario Outline: AC8 - Registering as a new user with invalid date format
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson@gmail.com", and <dob> on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    When I click the sign-up button
    Then I am shown the error message "Date is not in valid format, DD/MM/YYYY" on the register form
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
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson8@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    And I enter a date of birth that means I turn 13 years old in 0 days on the register form
    When I click the sign-up button
    Then I am successfully registered

  Scenario: AC9.2 - Registering as a new user before I am old enough
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    And I enter a date of birth that means I turn 13 years old in 1 days on the register form
    When I click the sign-up button
    Then I am shown the error message "You must be 13 years or older to create an account" on the register form

  Scenario: AC10.1 - Registering as a new before I am too old
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson9@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    And I enter a date of birth that means I turn 121 years old in 1 days on the register form
    When I click the sign-up button
    Then I am successfully registered

  Scenario: AC10.2 - Registering as a new user when I am too old
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "Password!123" on the register form
    And I enter a date of birth that means I turn 121 years old in 0 days on the register form
    When I click the sign-up button
    Then I am shown the error message "The maximum age allowed is 120 years" on the register form

  Scenario: AC11 - Registering as a new user with different passwords
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password "Password!123" and retype password "SecondPassword!456" on the register form
    When I click the sign-up button
    Then I am shown the error message "Passwords do not match" on the register form

  Scenario Outline: AC12 - Registering as a new user with a weak password
    Given I am on the registration form
    And I enter details "Sarah", "Thompson", a unique email "sarahThompson@gmail.com", and "12/12/2000" on the register form
    And I do not tick the checkbox for no last name on the register form
    And I enter password <password> and retype password <password> on the register form
    When I click the sign-up button
    Then I am shown the error message "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character." on the register form
    Examples:
      | password              |
      | "abc"                 |
      | "Short1$"             |
      | "MissingANumber%"     |
      | "missing_uppercase1&" |
      | "missingSpecial1"     |
      | "MISSING_LOWERCASE1$" |
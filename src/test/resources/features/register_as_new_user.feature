Feature: Registering as a new user

  Scenario: Registering as a new user with valid details
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am successfully registered

  Scenario: Registering as a new user with invalid first name
    Given I am on the registration form and enter first name "123!" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "First name must only include letters, spaces, hyphens or apostrophes"

  Scenario: Registering as a new user with invalid email
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "abc@123"
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "Email address must be in the form ‘jane@doe.nz’"

  Scenario: Registering as a new user with different passwords
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "Password!123" and retype password "SecondPassword!456"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "Passwords do not match"

  Scenario: Registering as a new user with a weak password
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "abc" and retype password "abc"
    And I enter date of birth "12/12/2000"
    When I click the sign-up button
    Then I am shown the error message "Your password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one number, and one special character."

  Scenario: Registering as a new user younger than 13
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/2023"
    When I click the sign-up button
    Then I am shown the error message "You must be 13 years or older to create an account"

  Scenario: Registering as a new user older than 120
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "12/12/1800"
    When I click the sign-up button
    Then I am shown the error message "The maximum age allowed is 120 years"

  Scenario: Registering as a new user older with invalid date format
    Given I am on the registration form and enter first name "John" and last name "Doe"
    And I enter email "johndoe@gmail.com"
    And I enter password "Password!123" and retype password "Password!123"
    And I enter date of birth "abc"
    When I click the sign-up button
    Then I am shown the error message "Date is not in valid format, DD/MM/YYYY"

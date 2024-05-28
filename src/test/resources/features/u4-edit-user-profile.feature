Feature: U4 - As Sarah, I want to edit my user profile so that I can keep my details accurate.

  Background:
    Given I am logged in with the name "John" "", email "johndoe@gmail.com", and date of birth "12/12/2000"

  Scenario: AC1 & AC2 - Going to the edit profile form and seeing my prefilled details
    Given I am on the user profile page
    When I click the edit button on my user profile page
    Then I see my details prefilled on the Edit Profile form, "John", "", "johndoe@gmail.com", & "12/12/2000"
    And I see the checkbox for having no last name is checked

  Scenario: AC12 - Cancel takes me back to my profile page and no changes have been made
    Given I am on the edit user profile form
    When I click the cancel button on my edit user profile form
    Then I am taken back to my profile page and my details are still "John" "", "johndoe@gmail.com", and "12/12/2000"

  Scenario Outline: AC3 - Editing my profile with valid details
    Given I am on the edit user profile form
    And I enter details <firstName>, <lastName>, <email>, and <dob> on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then My new details are saved
    Examples:
      | firstName           | lastName | email               | dob          |
      | "John"              | "Doe"    | "johndoe@gmail.com" | "12/12/2000" |
      | "John"              | "Doe"    | "johndoe@gmail.com" | ""           |
      | "John McGe'e-Smith" | "Doe"    | "johndoe@gmail.com" | "12/12/2000" |
      | "John McGe'e-Smith" | "Doe"    | "johndoe@gmail.com" | "12/12/2000" |
      | "Léo"               | "Dęõn"   | "leodeon@gmail.com" | "12/12/2000" |

  Scenario: AC4 - Editing my profile to have no last name
    Given I am on the edit user profile form
    And I enter details "John", "", "johndoe@gmail.com", and "12/12/2000" on the edit user profile form
    And I tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then My new details are saved

  Scenario Outline: AC5 - Editing my profile with an invalid first/last name
    Given I am on the edit user profile form
    And I enter details <firstName>, <lastName>, "johndoe@gmail.com", and "12/12/2000" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message <errorMessage> on the edit user profile form
    Examples:
      | firstName   | lastName   | errorMessage                                                           |
      | ""          | "Doe"      | "First name cannot be empty"                                           |
      | "    "      | "Doe"      | "First name cannot be empty"                                           |
      | "John2"     | "Doe"      | "First name must only include letters, spaces, hyphens or apostrophes" |
      | "John$"     | "Doe"      | "First name must only include letters, spaces, hyphens or apostrophes" |
      | "John"      | ""         | "Last name cannot be empty"                                            |
      | "John"      | "   "      | "Last name cannot be empty"                                            |
      | "John"      | "Doe3"     | "Last name must only include letters, spaces, hyphens or apostrophes"  |
      | "John"      | "Doe%"     | "Last name must only include letters, spaces, hyphens or apostrophes"  |

  Scenario: AC6.1 - Editing my profile with a valid length first/last name.
    Given I am on the edit user profile form
    And I enter details "", "", "johndoe@gmail.com", and "12/12/2000" on the edit user profile form
    And I enter a first name 64 characters long and a last name 64 characters long
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then My new details are saved

  Scenario Outline: AC6.2 - Editing my profile with an invalid length first/last name
    Given I am on the edit user profile form
    And I enter details "", "", "johndoe@gmail.com", and "12/12/2000" on the edit user profile form
    And I enter a first name <firstNameLength> characters long and a last name <lastNameLength> characters long
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message <errorMessage> on the edit user profile form
    Examples:
      | firstNameLength | lastNameLength | errorMessage                                    |
      | 65              | 3              | "First name must be 64 characters long or less" |
      | 4               | 65             | "Last name must be 64 characters long or less" |

  Scenario Outline: AC7 - Editing my profile with an invalid email
    Given I am on the edit user profile form
    And I enter details "John", "Doe", <email>, and "12/12/2000" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message "Email address must be in the form ‘jane@doe.nz’" on the edit user profile form
    Examples:
      | email                   |
      | "abc@123"               |
      | "bad"                   |
      | "email@email@email.com" |

  Scenario: AC8 - Cannot change email to an existing one
    Given I am on the edit user profile form
    And A user exists with email "ben@gmail.com"
    And I enter details "John", "Doe", "ben@gmail.com", and "12/12/2000" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message "Email address is already in use" on the edit user profile form

  Scenario Outline: AC9 - Editing my profile with invalid date format
    Given I am on the edit user profile form
    And I enter details "John", "Doe", "johndoe@gmail.com", and <dob> on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message "Date is not in valid format, DD/MM/YYYY" on the edit user profile form
    Examples:
      | dob           |
      | "abc"         |
      | "1/1/2000"    |
      | "05/13/2000"  |
      | "01/01/20000" |
      | "32/01/2000"  |
      | "29/02/2001"  |
      | "01-01-2000"  |

  Scenario: AC10.1 - Editing my profile and changing my date of birth to make me just old enough for the website
    Given I am on the edit user profile form
    And I enter details "John", "Doe", "johndoe@gmail.com", and "" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    And I change my date of birth to mean that I turn 13 years old in 0 days on the edit user profile form
    When I click the edit profile button
    Then My new details are saved

  Scenario: AC10.2 - Editing my profile and changing my date of birth to make me too young for the website
    Given I am on the edit user profile form
    And I enter details "John", "Doe", "johndoe@gmail.com", and "" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    And I change my date of birth to mean that I turn 13 years old in 1 days on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message "You must be 13 years or older to create an account" on the edit user profile form

  Scenario: AC11.1 - Editing my profile and changing my date of birth to make me just young enough for the website
    Given I am on the edit user profile form
    And I enter details "John", "Doe", "johndoe@gmail.com", and "" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    And I change my date of birth to mean that I turn 121 years old in 1 days on the edit user profile form
    When I click the edit profile button
    Then My new details are saved

  Scenario: AC11.2 - Editing my profile and changing my date of birth to make me too old for the website
    Given I am on the edit user profile form
    And I enter details "John", "Doe", "johndoe@gmail.com", and "" on the edit user profile form
    And I do not tick the checkbox for no last name on the edit user profile form
    And I change my date of birth to mean that I turn 121 years old in 0 days on the edit user profile form
    When I click the edit profile button
    Then I am shown the error message "The maximum age allowed is 120 years" on the edit user profile form
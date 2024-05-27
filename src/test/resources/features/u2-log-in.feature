Feature: U2 - As Sarah, I want to log into the system so that I can have a personalised experience with it and enjoy its features

  Background:
    Given A user exists with email "gabian.filson@email.fr" and password "Ilovebelgianwaffles1!"
  # AC1: I don't see a way to write this AC in cucumber test

  Scenario: AC2 - Logging in as a registered user
    Given I am on the log in form and enter the email "gabian.filson@email.fr" and password "Ilovebelgianwaffles1!"
    When I click the Sign In button
    Then I successfully log in

  # AC3 - I also don't see a way to write this AC in cucumber test

  # AC4 - I also don't see a way to write this AC in cucumber test

  Scenario Outline: AC5 - Trying to log in with a malformed or empty email address
    Given I am on the log in form and enter the email <email> and password "Password1234!"
    When I click the Sign In button
    Then I am shown the error message "Email address must be in the form ‘jane@doe.nz’" and I am not logged in
    Examples:
      | email         |
      | "@email.com"  |
      | "e@mail"      |
      | "notanemail"  |
      | "e@mail..com" |

  Scenario Outline: AC6 - Trying to log in with an email unknown to the system
    Given I am on the log in form and enter the email <email> and password "Password1234!"
    When I click the Sign In button
    Then Getting user by email and password returns null
    Examples:
      | email                         |
      | "emaildoesntexist@email.com"  |
      | "newuser@email.com"           |
      | "notauser@email.com"          |

  Scenario Outline: AC7.1 - Trying to log in with a wrong password
    Given I am on the log in form and enter the email "gabian.filson@email.fr" and password <password>
    When I click the Sign In button
    Then Getting user by email and password returns null
    Examples:
      | password        |
      | ""              |
      | "wrongpassword" |

  Scenario Outline: AC7.2 - Trying to log in with an empty password
    Given I am on the log in form and enter the email "gabian.filson@email.fr" and password <password>
    When I click the Sign In button
    Then I am shown the error message "The email address is unknown, or the password is invalid" and I am not logged in
    Examples:
      | password |
      | ""       |
      | " "      |

  # AC8 - I also don't see a way to write this AC in cucumber test
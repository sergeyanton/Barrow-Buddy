Feature: U2 - As Sarah, I want to log into the system so that I can have a personalised experience with it and enjoy its features

  Background:
    Given A user with email "gabian.filson@email.fr" and password "Ilovebelgianwaffles1!"
  # AC1: I don't see a way to write this AC in cucumber test

  Scenario: AC2 - Logging in as a registered user
    Given I am on the log in form and enter the email "gabian.filson@email.fr" and password "Ilovebelgianwaffles1!"
    When I click the Sign In button
    Then I successfully log in

  # AC3 - I also don't see a way to write this AC in cucumber test

  # AC4 - I also don't see a way to write this AC in cucumber test

  Scenario Outline: AC5 - Trying to log in with a malformed or empty email address
    Given I am on the log in form and enter the email <email> and password <password>
    When I click the Sign In button
    Then I am shown the error message "Email address must be in the form ‘jane@doe.nz’"
    Examples:
      | email         | password        |
      | "@email.com"  | "Password1234!" |
      | "e@mail"      | "Password1234!" |
      | "notanemail"  | "Password1234!" |
      | "e@mail..com" | "Password1234!" |

  Scenario Outline: AC6 - Trying to log in with an email unknown to the system
    Given I am on the log in form and enter the email <email> and password <password>
    When I click the Sign In button
    Then I am shown the error "The email address is unknown, or the password is invalid"
    Examples:
      | email                         | password        |
      | "emaildoesntexist@email.com"  | "Password1234!" |
      | "newuser@email.com"           | "Password1234!" |
      | "notauser@email.com"          | "Password1234!" |

  Scenario Outline: AC7 - Trying to log in with an empty or wrong password
    Given I am on the log in form and enter the email <email> and password <password>
    When I click the Sign In button
    Then I am shown the error “The email address is unknown, or the password is invalid"
    Examples:
      | email                    | password        |
      | "gabian.filson@email.fr" | ""              |
      | "gabian.filson@email.fr" | "wrongpassword" |

  # AC8 - I also don't see a way to write this AC in cucumber test
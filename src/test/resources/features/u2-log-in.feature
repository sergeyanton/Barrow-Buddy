Feature: U2 - As Sarah, I want to log into the system so that I can have a personalised experience with it and enjoy its features

  Background:
    Given A user exists with email "gabian.filson@email.fr" and password "Ilovebelgianwaffles1!"
  # AC1: I don't see a way to write this AC in cucumber test

  Scenario: AC2 - Logging in as a registered user
    Given I am on the log in page
    When I log in with a valid user with email "gabian.filson@email.fr" and password "Ilovebelgianwaffles1!"
    Then I successfully log in

  # AC3 - I also don't see a way to write this AC in cucumber test

  # AC4 - I also don't see a way to write this AC in cucumber test

  Scenario Outline: AC5 - Trying to log in with a malformed or empty email address
    Given I am on the log in page
    When I log in with an invalid user with the email <email> and password "Password1234!"
    Then I remain on the log in page and I get an error message in the field "email"
    Examples:
      | email         |
      | "@email.com"  |
      | "e@mail"      |
      | "notanemail"  |
      | "e@mail..com" |

  Scenario Outline: AC6 - Trying to log in with an email unknown to the system
    Given I am on the log in page
    When I log in with an invalid user with the email <email> and password "Password1234!"
    Then I remain on the log in page and I get an error message in the field "password"
    Examples:
      | email                         |
      | "emaildoesntexist@email.com"  |
      | "newuser@email.com"           |
      | "notauser@email.com"          |

  Scenario Outline: AC7.1 - Trying to log in with a wrong password
    Given I am on the log in page
    When I log in with an invalid user with the email "gabian.filson@email.fr" and password <password>
    Then I remain on the log in page and I get an error message in the field "password"
    Examples:
      | password        |
      | ""              |
      | "wrongpassword" |

  Scenario Outline: AC7.2 - Trying to log in with an empty password
    Given I am on the log in page
    When I log in with an invalid user with the email "gabian.filson@email.fr" and password <password>
    Then I remain on the log in page and I get an error message in the field "password"
    Examples:
      | password |
      | ""       |
      | " "      |

  # AC8 - I also don't see a way to write this AC in cucumber test
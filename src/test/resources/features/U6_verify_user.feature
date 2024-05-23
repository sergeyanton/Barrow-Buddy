Feature: Verifying account after registration and before logging in

  Scenario: Logging in with an account that is not verified
    Given I have registered with the first name "John" and last name "Doe", email "a.a@a.com" and password "Hello123!"
    And I don't verify my account
    When I log in
    Then I am redirected to the page with URL "/register/verification"

  Scenario: Register and verify account
    Given I have registered with the first name "Bob" and last name "Doe", email "bob.doe@a.com" and password "Hello123!"
    And I verify my account
    When I log in
    Then I am redirected to the page with URL "/home"


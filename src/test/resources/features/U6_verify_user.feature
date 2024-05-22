Feature: Verifying account after registration and before logging in

  Scenario: Logging in with an account that is not verified
    Given I have registered with the first name "John" and last name "Doe", email "a.a@a.com" and password "Hello123!"
    And I don't verify my account
    When I access "/login"
    Then I am redirected to the page with URL "/register/verification"

  Scenario: Register and verify account
    Given I have registered with the first name "Bob" and last name "Doe", email "bob.doe@a.com" and password "Hello123!"
    When I verify my account
    And I access "/login"

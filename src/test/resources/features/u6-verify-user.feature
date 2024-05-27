Feature: Verifying account after registration and before logging in

  Scenario: Logging in with an account that is not verified
    Given I have registered with the first name "Jason" and last name "Dickens", email "Jason.Dickens@gmail.com" and password "Hello123!"
    When I don't verify my account
    Then I try to log in am redirected to the page with URL "/register/verification"

  Scenario: Register and verify account
    Given I have registered with the first name "Bob" and last name "Doe", email "bob.doe@a.com" and password "Hello123!"
    And I verify my account
    Then I try to log in am redirected to the page with URL "/home"


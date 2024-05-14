Feature: Logging in with an account that is not verified

  Scenario: Logging in with an account that is not verified
    Given I have registered with the first name "John" and last name "Doe", email "a.a@a.com" and password "Hello123!"
    When I don't verify my account and try log in
    Then I am redirected to the page with URL "/register/verification"
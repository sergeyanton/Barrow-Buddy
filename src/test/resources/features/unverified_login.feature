Feature: Logging in with an account that is not verified

  Scenario: Logging in with an account that is not verified
    Given I have registered with the first name "John" and last name "Doe", email "a.a@a.com" and password "Hello123!"
#    When I access log in page without verifying my account
#    Then I am redirected to the page with URL "/register/verification"
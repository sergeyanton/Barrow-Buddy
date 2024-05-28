Feature: Example - Figuring out how to perform log in and allow user to be authenticated and perform actions as if they are logged in.

  Background:
    Given A user exists with email "Jung.Kook@gg.kr" and password "IloveFrance123!"


  Scenario: I press cancel when creating a new garden from the home screen
    Given I am logged in as "Jung.Kook@gg.kr" with password "IloveFrance123!"
    And I am on the home screen
    When I press the Create Garden button

Feature: U8 - As a user, I want to be able to cancel creating a new garden and I am take to the screen where I was before creating the new garden

  Background:
    Given A user exists with email "Jung.Kook@gg.kr" and password "IloveFrance123!"


  Scenario: I press cancel when creating a new garden from the home screen
    Given I am logged in as "Jung.Kook@gg.kr" with password "IloveFrance123!"
    And I am on the home screen
    When I press the Create Garden button
    And I press the Cancel button
    Then I should see the "/home" page

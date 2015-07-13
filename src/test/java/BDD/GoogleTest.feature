@GoogleTest
Feature: Test The Google Easter Eggs

  Scenario: Test how easter egg askew is working
    Given I open Google Page
    When  I search for askew
    Then  I get the easter Egg result

  Scenario: Test how easter Do a barrel roll  is working
    Given I open Google Page
    When  I search for Do a barrel roll
    Then  I get the easter Egg result

  Scenario: Test how easter Google in 1998  is working
    Given I open Google Page
    When  I search for Google in 1998
    Then  I get the easter Egg result


  Scenario: Test how easter zerg rush  is working
    Given I open Google Page
    When  I search for zerg rush
    Then  I get the easter Egg result

  Scenario: Test how easter blink HTML  is working
    Given I open Google Page
    When  I search for blink HTML
    Then  I get the easter Egg result
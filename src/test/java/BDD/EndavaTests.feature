@EndavaSiteTests
Feature: EndavaCareersFeature
  In order to contact Endava Company
  As an user
  I want to be able to see Eastern Europe Location Details

  Scenario: Check Careers Link From Endava Home Page
    Given I have the Endava Home Page
    When I load the page
    Then the careers link should be displayed

  Scenario Outline:
    Given I have the Endava Home Page
    When I click Contact link
    And I click Eastern Europe buton on Contact page
    Then I should see the <Location> and <Contact details>
  Examples:
    | Location  | Contact details                   |
    | Iasi      | 3E Palat St.                      |
    | Chisinau  | 15 Sfatul Tarii St.               |
    | Cluj      | 53-55 Alexandru Vaida Voievod St. |
    | Bucharest | AFI Business Park 1               |
    | Skopje    | Kale Building                     |
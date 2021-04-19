@login
Feature:login feature

  @login1
  Scenario: login test case
    Given I set facebook url
    And I navigate to facebook url
    And I wait for page to load
    And I enter user name "abc"
    And I enter password "abc"
    And I click on login button
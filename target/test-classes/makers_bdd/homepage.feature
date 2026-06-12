Feature: Homepage

  # This is the required scenario and should run properly on *most* machines
  Scenario Outline: Can access subpages from the homepage
    Given I am on the Makers homepage
    When I click the "<button>" link
    Then I should be on the "<button>" page
    Examples:
      | button          |
      | Apprenticeships |
      | Apply           |
      | Careers         |
      | Financing       |
      | FAQ             |

  # This is an additional scenario due to FirefoxDriver not loading pages with forms correctly for me, therefore the homepage test will not run
  Scenario Outline: Can access subpages from the contact page
    Given I am on the Makers contact page
    When I click the "<button>" link
    Then I should be on the "<button>" page
    Examples:
      | button          |
      | Careers         |
      | Financing       |
      | FAQ             |
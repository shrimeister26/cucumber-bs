Feature: Webscrap Elpais opinion titles and translate to english

   Background: Navigate to Elpais home
      Given User navigate to Elpais home page "https://elpais.com/"

   @regression @device_Window_11 @author_shripathi
   Scenario: Webcraping opinion pages along with images
      When user launches elpais opinion page
      Then user navigates to first five topics and translates title to english


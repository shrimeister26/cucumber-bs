Cucumber TestNg - Browserstack exercise

To run in browser stack -> mvn clean test 

To run in local-> BROWSERSTACK_AUTOMATION=false mvn clean test -Dbrowser=chrome (Set Target as local in config)

To view reports -> mvn allure:serve

Images are stored under project folder -> reports & attached to allure report as well.. 
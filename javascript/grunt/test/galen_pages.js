this.SubmitPage = function (driver) {
  GalenPages.extendPage(this, driver, "Submit Page", {
    nameTextfield: "xpath: //*[@id='content']/div/input", // xpath locator
    submitButton: "xpath: //*[@id='content']/div/a" // xpath locator

  });
};


test("Home page test", function () {
  var driver = createDriver("http://samples.galenframework.com/tutorial-color-scheme/tutorial.html", "1400x1100", "firefox");

  var submitPage = new SubmitPage(driver);

  submitPage.nameTextfield.typeText("Something");
  submitPage.submitButton.click();

  driver.close();

});

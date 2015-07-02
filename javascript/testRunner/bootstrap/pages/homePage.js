this.HomePage = function (driver) {
    GalenPages.extendPage(this, driver, {
        goToGitHubButton: "(//*[contains(@class,'bs-docs-featurette')]//a[contains(@class,'btn')])[1]",
        goToExpoButton: "(//*[contains(@class,'bs-docs-featurette')]//a[contains(@class,'btn')])[2]"
    });
};

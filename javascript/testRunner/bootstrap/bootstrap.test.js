load("init.js");
load("pages/homePage.js");

testOnAllDevices("Bootstrap homepage", "/", function (driver, device) {
    var homePage = new HomePage(driver).waitForIt();
    checkLayout(driver, "specs/homePageLayout.gspec", device.tags);
    homePage.goToExpoButton().click();
});

testOnDeviceAndBrowsers(devices.mobile, "Bootstrap CSS page", "/css/", function (driver, device, browser) {
    checkLayout(driver, "specs/cssPageLayout.gspec", device.tags);
});

testOnAllDevicesAndBrowsers("Bootstrap JS page ", "/javascript/", function (driver, device, browser) {
    checkLayout(driver, "specs/javascriptPageLayout.gspec", device.tags);
});

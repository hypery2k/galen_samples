load('../gl.js');

forAll(config.getDevices(), function (device) {
  test('Example page on ' + device.deviceName, function () {
    gl.openPage(device, config.getProjectPage());

    gl.runSpecFile(device, './test/example-page.gspec');
  });
});

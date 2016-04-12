/*
 * Galen JavaScript API
 */

/*
 * Global public API
 *
 * @global config
 * @global gl
 * @global elements
 */
var config;
var gl;
var elements;

/**
 * config module describes the configuration of the testing
 * suite. It includes information about:
 * - project name and url address
 * - testing devices
 * - selenium grid settings
 *
 * Useful public interface methods:
 * - getProjectName: returns the project codename
 * - getProjectPage: returns project url address
 * - getProjectSubpage: returns project url address with appended subdir
 *
 * @returns {Array|Object} public interface methods
 */
config = function () {
  /*
   * @private
   *
   * Default project configuration
   */
  var _project = {
    name: 'Project',
    protocol: 'http',
    host: '127.0.0.1',
    port: 80
  };

  /*
   * @private
   *
   * Default Selenium grid configuration
   */
  var _slGrid = {
    enabled: false,
    url: ''
  };

  /*
   * @private
   */
  var _devices = {};
  var _singleDevice = false;

  /**
   * Set configuration.
   * @param {Object} configuration
   */
  function set(data) {
    if (!data.project) {
      data.project = {
        name: null,
        url: null
      };
    }

    config.setProjectName(data.project.name || 'Project');
    config.setProjectUrl(data.project.url || data.url || 'http://127.0.0.1:80');
    config.setDevices(data.devices || {});

    if (data.seleniumGrid) {
      config.setSeleniumGrid(data.seleniumGrid.url);
    }
  };

  /**
   * Enable Selenium grid.
   * @param {String} url Selenium Grid HUB url
   */
  function setSeleniumGrid(url) {
    _slGrid.enabled = true;
    _slGrid.url = url;
  };

  /**
   * Selenium Grid getter.
   * @returns {Object} Selenium Grid configuration
   */
  function getSeleniumGrid() {
    return _slGrid;
  };

  /**
   * Single device getter.
   * @returns {Boolean} true, if only one device is necessary
   */
  function getSingleDeviceState() {
    return _singleDevice;
  };

  /**
   * Set devices to be tested on, in form of an Object
   * container. Configurations depend on the target environment,
   * but two most useful docs for that are:
   * - http://galenframework.com/docs/reference-javascript-tests-guide/#CreatingdriverinSeleniumGrid
   * - https://docs.saucelabs.com/reference/test-configuration/
   *
   * Also, call singleBrowserTests() to determine if only one device type is sufficient.
   *
   * @param {Object} devices devices container
   */
  function setDevices(devices) {
    _devices = devices || {};

    _singleDevice = singleBrowserTests();
  };

  /**
   * Devices getter.
   * @returns {Object} devices or empty object
   */
  function getDevices() {
    return _devices;
  };

  /**
   * Set project codename, available for tests via the public
   * methods interface.
   * @param {String} name project name
   */
  function setProjectName(name) {
    _project.name = name;
  };

  /**
   * Project name getter.
   * @returns {String} non-null project name
   */
  function getProjectName() {
    return _project.name;
  };

  /**
   * Set project url, available for tests via the public
   * methods interface.
   * Safest form would be:
   * {protocol}://{host}:{port}/
   * Other forms' behaviour may vary.
   *
   * @param {String} url project url
   */
  function setProjectUrl(url) {
    var _url = (url.split(/(:\/{2})|(:)|(\/{1}.*)/gm) || []).filter(Boolean);

    if (_url.length < 3) {
      throw new Error('Invalid project url');
    }

    _project.protocol = _url[0];
    _project.host = _url[2];
    _project.port = _url.length >= 4 ? _url[4] : 80;
  };

  /**
   * Getter for project base url address, available for tests via the public
   * methods interface.
   * @returns {String} url
   */
  function getProjectPage() {
    return [
      _project.protocol,
      '://',
      _project.host,
      ':',
      _project.port
    ].join('');
  };

  /**
   * Getter for project subpage url. Transforms the base url
   * by appending a proper suffix, ex.:
   * getProjectSubpage('#login')  =>  http://myproj.com/#login
   *
   * Available via the public methods interface.
   * @param   {String} url subpage
   * @returns {String} url
   */
  function getProjectSubpage(url) {
    return [
      getProjectPage(),
      url.indexOf('/') !== 0 ? url : url.substr(1)
    ].join('/');
  };

  /**
   * Determine whether tests are run on one or more browsers.
   * This allows a simple optimisation for local testing.
   *
   * @returns {Boolean} true, if only one browser is needed
   */
  function singleBrowserTests() {
    var devices = getDevices();
    var singleDevice = true;
    var priorType;

    forAll(devices, function (device) {
      if (!priorType) {
        priorType = device.browser;
      } else {
        if (device.browser !== priorType) {
          singleDevice = false;
        }
      }
    });
    return singleDevice;
  };

  /*
   * @public
   *
   * Public API
   */
  return {
    set: set,
    setDevices: setDevices,
    setProjectName: setProjectName,
    setProjectUrl: setProjectUrl,
    setSeleniumGrid: setSeleniumGrid,

    getDevices: getDevices,
    getProjectName: getProjectName,
    getProjectPage: getProjectPage,
    getProjectSubpage: getProjectSubpage,
    getSeleniumGrid: getSeleniumGrid,
    getSingleDeviceState: getSingleDeviceState
  };
}();

/*
 * @public
 *
 * Cached elements on the page.
 */
elements = {};

/**
 * gl module serves a bunch of useful and fast implementations
 * of original Galen JavaScript API.
 * @returns {Object} public interface methods
 */
gl = function () {
  /*
   * @private
   *
   * Available webdrivers (one for every view type).
   */
  var _drivers = {};

  /**
   * @private
   * @class
   *
   * GalenPages forces us to create a page class.
   * It is created here as an uniformal scheme and
   * later used for every page initialized by tests.
   * @param {Object} driver           target webdriver
   * @param {Object} name             name of a page to be used in reports
   * @param {Object} primaryFields    set of key/values pairs containing page elements and functions that will be in-built into the page
   * @param {Object} secondaryFields  same as primaryFields but the elements defined inside this structure will not be used in waitForIt function
   */
  function GalenPageCls(driver, name, primaryFields, secondaryFields) {
    GalenPages.extendPage(this, driver, name, primaryFields || {}, secondaryFields || {});
  }

  /**
   * This method shortcuts both driver and page creation.
   * We open a page in a target driver and ask it to select
   * necessary elements before it proceeds to tests.
   *
   * Depending on the test files and options, this method behaves differently.
   *
   * If tests are run only on a single browser (which is ofter a case for local tests
   * on Firefox webdriver), only one instance is created and reinitialized for every new
   * test. This extremely speeds up the testing process, but is only available for local
   * testing. (whether only one device is needed determines #getSingleDeviceState)
   *
   * @see getSingleDeviceState
   *
   * If multiple browsers are present, all tests are run separately to preserve proper
   * execution.
   *
   * If tests are run remotely on Selenium Grid, all have to be run separately. This is due
   * to a timeout errors of Selenium Grids (ex. on SauceLabs if test exceeds a preset
   * execution time, test is aborted). For remote testing `concat` option in suggested
   * to minify the amount of instances created on Selenium Grid.
   *
   * @param {Object} device           device specification
   * @param {String} url              tested page url
   * @param {Object} name             name of a page to be used in reports
   * @param {Object} primaryFields    set of key/values pairs containing page elements and functions that will be in-built into the page
   * @param {Object} secondaryFields  same as primaryFields but the elements defined inside this structure will not be used in waitForIt function
   */
  function openPage(device, url, name, primaryFields, secondaryFields) {
    var page;
    var slGrid = config.getSeleniumGrid();
    var singleDevice = config.getSingleDeviceState();
    var targetDevice;

    primaryFields = primaryFields || {};
    secondaryFields = secondaryFields || {};
    name = name || url;

    if (slGrid.enabled === false) {
      if (singleDevice === true) {
        targetDevice = getSingleDeviceTarget(device);
      } else {
        targetDevice = getMultiDeviceTarget(device);
      }
    } else {
      targetDevice = getSlGridTarget(device, slGrid);
    }
    page = new GalenPageCls(targetDevice, name, primaryFields, secondaryFields);
    page.open(url);

    if (Object.keys(primaryFields).length > 0) {
      // Legen -
      page.waitForIt();
      // - dary!
    }

    elements = page;
  }

  /**
   * Target device getter for single device tests.
   *
   * @param   {Object} device device specification
   * @returns {Object} target instance
   */
  function getSingleDeviceTarget(device) {
    if (!_drivers[0]) {
      _drivers[0] = createDriver('about:blank', device.size, device.browser);
    } else {
      resize(_drivers[0], device.size);
    }

    return _drivers[0];
  }

  /**
   * Target device getter for multiple device tests.
   *
   * @param   {Object} device device specification
   * @returns {Object} target instance
   */
  function getMultiDeviceTarget(device) {
    if (!_drivers[device.deviceName]) {
      _drivers[device.deviceName] = createDriver('about:blank', device.size, device.browser);
    }

    return _drivers[device.deviceName];
  }

  /**
   * Target device getter for selenium grid tests.
   *
   * This looks unnecessary, but added to keep consistency.
   *
   * @param   {Object} device device specification
   * @param   {Object} slGrid selenium grid specification
   * @returns {Object} target instance
   */
  function getSlGridTarget(device, slGrid) {
    var driver = _drivers[device.deviceName];
    if (!driver) {
      driver = _drivers[device.deviceName] = createGridDriver(slGrid.url, device);
    }

    return driver;
  }

  /**
   * Run a Galen spec file on the current page on a
   * target driver.
   *
   * Argument `specFile` is relative to gl.js, not the test
   * file.
   *
   * @param {Object} device   device specification
   * @param {String} specFile Galen spec file path
   * @param {Array}  tags     (optional) Galen test tags
   */
  function runSpecFile(device, specFile, tags) {
    var useSingleDevice = config.getSingleDeviceState() && !config.getSeleniumGrid().enabled;

    checkLayout(
      _drivers[useSingleDevice ? 0 : device.deviceName],
      specFile,
      tags
    );
  }

  /**
   * Remove all elements stored in cache.
   *
   * Unlikely to be used in tests.
   */
  function cleanCache() {
    elements = {};
  }

  /**
   * Terminate all webdrivers, cleanCache and finish
   * the testing.
   *
   * Unlikely to be used in tests, unless you disable
   * gl#defaultAfterTest method.
   */
  function quit() {
    if (_drivers) {
      forAll(_drivers, function (driver) {
        driver.quit();
        driver = null;
      });

      _drivers = {};
    }
    cleanCache();
  }

  /**
   * After a single test: clean elements cache.
   * After all tests: quit the testing process.
   */
  function defaultAfterTest() {
    var slGrid = config.getSeleniumGrid();

    if (!slGrid.enabled) {
      afterTest(function () {
        gl.cleanCache();
      });
    } else {
      afterTest(function () {
        gl.quit();
      });
    }

    afterTestSuite(function () {
      gl.quit();
    });
  }

  /*
   * @public
   *
   * Public API
   */
  return {
    openPage: openPage,
    runSpecFile: runSpecFile,

    quit: quit,
    cleanCache: cleanCache,

    defaultAfterTest: defaultAfterTest
  };
}();

/*
 * Initialize default pre- and post- test callbacks.
 */
load('gl.config.js');
gl.defaultAfterTest();

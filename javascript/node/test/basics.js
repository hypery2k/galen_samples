/* globals describe, it, xit, before, after */

var assert = require("assert");
var gulp = require("gulp");
var path = require("path");
var es = require("event-stream");
var util = require("util");
var fs = require("fs");
var rimraf = require("rimraf");

var gulpGalen = require('../index.js');

describe("gulp-galen", function () {

    describe("basic functionality", function () {

        this.timeout(100000);

        var options = {
            url: "https://www.google.com",
            size: "800x600",
            galenPath: path.resolve('node_modules', 'galenframework', 'bin', 'galen' + (process.platform === 'win32' ? '.cmd' : ''))
        };

        it("should iterate over some gspecs", function (done) {
            gulp.src("**/specs/google_success?.gspec").pipe(gulpGalen.check(options))
                .pipe(es.writeArray(function (err, arr) {
                    assert(!err, "There where errors present");
                    assert(arr, "Result missing");
                    for (var i = 0; i < 2; i++) {
                        assert(arr[i].path.match(/specs\/google_success.\.gspec$/),
                            "File's path didn't end with specs/google_success?.gspec: '" + arr[i].path + "'");
                    }
                    done();
                }));
        });

        it("should handle failed specs", function (done) {
            try {
                expect(gulp.src("**/specs/google_failing.gspec").pipe(gulpGalen.check(options, function () {
                    //ignore
                }))).toThrow(new Error("Unexpected error!"));
                done();
            } catch (e) {
                done();
            }
        });

    });

    describe("extended functionality", function () {

        this.timeout(30000);

        before(function (done) {
            rimraf("./tmp/test-reports", done);
        });

        xit("should support some variables based upon the current file", function (done) {
            gulp.src("**/specs/google1.gspec").pipe(gulpGalen.check({
                url: "https://www.google.com",
                size: "800x600",
                galenPath: "./node_modules/galenframework/bin/galen",
                testngreport: "./tmp/test-reports/testng-{basename}.xml"
            }, function (error) {
                es.writeArray(function (err, arr) {
                    var fn = "./tmp/test-reports/testng-google_success1.gspec.xml";
                    fs.stat(fn, function (err, stats) {
                        assert(!err, "File not found: " + fn);
                        assert(stats.isFile(), "Is no file: " + fn);
                    });
                });
                done();
            }));
        });

    });

});
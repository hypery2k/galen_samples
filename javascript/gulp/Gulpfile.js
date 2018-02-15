var gulp = require('gulp'),
    gulpGalen = require('gulp-galenframework');

gulp.task("test:galen", function (done) {
    gulp.src('test/specs/**/google*.gspec').pipe(gulpGalen.check({
        url: 'https://www.google.com',
        cwd: 'test/galen/',
        size: '1280x800'
    }, done));
});
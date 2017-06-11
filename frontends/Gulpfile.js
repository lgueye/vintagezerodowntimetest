let gulp = require('gulp'),
    path = require('path'),
    inject = require('gulp-inject'),
    yargs = require('yargs').argv,
    fs = require('fs-extra'),
    clean = require('gulp-clean');

let getApiUrl = function () {
    let apiUrl = 'http://localhost:9000/ws-api';

    if (yargs.apiUrl) {
        apiUrl = yargs.apiUrl;
    }

    return apiUrl;
};

// map of all our paths
let paths = {
    js: path.join(__dirname, 'js/'),
    app: path.join(__dirname, 'js/app.js'),
    html: path.join(__dirname, 'index.html'),
    dist: path.join(__dirname, 'dist/'),
    distJs: path.join(__dirname, 'dist/js'),
    distApp: path.join(__dirname, 'dist/js/app.js')
};

gulp.task('copy', function () {
    //copy images
    gulp.src(paths.js)
        .pipe(gulp.dest(paths.dist));
    gulp.src(paths.app)
        .pipe(gulp.dest(paths.distJs));
    return gulp.src(paths.html)
        .pipe(gulp.dest(paths.dist));

});

gulp.task('config', ['copy'], () => {
    return gulp.src(paths.distApp)
        .pipe(inject(gulp.src('.'), {
            starttag: '/* inject:env */',
            endtag: '/* endinject */',
            transform: () => {
                let apiUrl = getApiUrl();
                return `var apiUrl = '${apiUrl}';`
            }
        }))
        .pipe(clean({force: true}))
        .pipe(gulp.dest(paths.distJs));
});

gulp.task('default', ['copy', 'config']);

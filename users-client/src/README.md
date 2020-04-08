### Publishing to NPM

This module compiles Protobufs of Users library to JavaScript
and publishes them to NPM as a [spine-users](https://www.npmjs.com/package/spine-users) package.

The publishing of the NPM package is performed automatically by Travis. It happens only
after a merge to `master` branch.

The important note is that NPM disallows to publish the same version of a package twice.
So, be sure to advance the version in `package.json` before publishing.

Also, the publishing can be performed manually. To do it:

1. Login to NPM as `spine-event-engine` and generate a new access token.
2. Set the generated token to your `NPM_TOKEN` environment variable.
3. Execute Gradle `publishJs` task from project root:
 ```bash
    ./gradlew publishJs
 ``` 

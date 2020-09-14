fastlane documentation
================
# Installation

Make sure you have the latest version of the Xcode command line tools installed:

```
xcode-select --install
```

Install _fastlane_ using
```
[sudo] gem install fastlane -NV
```
or alternatively using `brew install fastlane`

# Available Actions
## Android
### android check_lint
```
fastlane android check_lint
```
Checking Lint
### android unit_test
```
fastlane android unit_test
```
Run tests with a Emulator
### android build_and_distribute
```
fastlane android build_and_distribute
```
Build App
### android upload_and_distribute
```
fastlane android upload_and_distribute
```
Upload App to Firebase
### android bump
```
fastlane android bump
```
Bump Version
### android push_tag
```
fastlane android push_tag
```
Push Tag
### android sync_to_github
```
fastlane android sync_to_github
```
Sync to Github
### android release_version
```
fastlane android release_version
```
Upload new version to JCenter
### android test
```
fastlane android test
```
Test

----

This README.md is auto-generated and will be re-generated every time [fastlane](https://fastlane.tools) is run.
More information about fastlane can be found on [fastlane.tools](https://fastlane.tools).
The documentation of fastlane can be found on [docs.fastlane.tools](https://docs.fastlane.tools).

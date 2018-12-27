Modules
===============

Library is also distributed via **separate modules** which may be downloaded as standalone parts of
the library in order to decrease dependencies count in Android projects, so only dependencies really
needed in an Android project are included. **However** some modules may depend on another modules
from this library or on modules from other libraries.

## Download ##

### Gradle ###

For **successful resolving** of artifacts for separate modules via **Gradle** add the following snippet
into **build.gradle** script of your desired Android project and use `implementation '...'` declaration
as usually.

    repositories {
        maven {
            url  "http://dl.bintray.com/universum-studios/android"
        }
    }

## Available modules ##
> Following modules are available in the [latest](https://bitbucket.org/android-universum/fragments/src/master/downloads "Downloads page") stable release.

- **[Core](https://bitbucket.org/android-universum/fragments/src/master/library-core)**
- **[Base](https://bitbucket.org/android-universum/fragments/src/master/library-base)**
- **[Common](https://bitbucket.org/android-universum/fragments/src/master/library-common)**
- **[Web](https://bitbucket.org/android-universum/fragments/src/master/library-web)**
- **[@Manage](https://bitbucket.org/android-universum/fragments/src/master/library-manage_group)**
- **[Manage-Core](https://bitbucket.org/android-universum/fragments/src/master/library-manage-core)**
- **[Manage-Base](https://bitbucket.org/android-universum/fragments/src/master/library-manage-base)**
- **[@Transition](https://bitbucket.org/android-universum/fragments/src/master/library-transition_group)**
- **[Transition-Core](https://bitbucket.org/android-universum/fragments/src/master/library-transition-core)**
- **[Transition-Common](https://bitbucket.org/android-universum/fragments/src/master/library-transition-common)**
- **[Transition-Extra](https://bitbucket.org/android-universum/fragments/src/master/library-transition-extra)**

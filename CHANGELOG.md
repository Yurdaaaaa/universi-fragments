Change-Log
===============
> Regular configuration update: _09.03.2020_

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 1.x ##

### [1.5.1](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 24.03.2020

- `FragmentController.create(Fragment)` now properly creates instance with **child fragment manager** of the given `fragment`.

### [1.5.0](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 09.03.2020

- Regular **dependencies update** (mainly artifacts from **Android Jetpack**).
     - Updated `androidx.fragment:fragment` dependency to **1.2.2** version.
- Removed elements that has been **deprecated** in previous versions.

### [1.4.3](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 08.12.2019

- Regular **dependencies update** (mainly to use new artifacts from **Android Jetpack**).

### [1.4.2](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 14.03.2019

- Small updates and improvements.

### [1.4.1](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 22.01.2019

- Using `Lifecycle` in `FragmentController` to safely execute `FragmentRequests` according to the
  current lifecycle state. By default any `FragmentRequest` will be executed only if the current
  Lifecycle's state is **at least** `STARTED` (this is true in cases when `FragmentController` is
  created via constructor taking `Activity` or `Fragment` as argument).

### [1.4.0](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 15.11.2018

- Regular **dependencies update** (mainly to use new artifacts from **Android Jetpack**).
- Small updates and improvements.

### [1.3.4](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 05.11.2018

- Small updates and improvements.

### [1.3.3](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 19.07.2018

- Small updates and code quality improvements.

### [1.3.2](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 27.02.2018

- Stability improvements.

### [1.3.1](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 22.12.2017

- `FragmentController` now does not attach **custom animations** to `FragmentTransaction` also when
  a device is in **power save mode**.
- Removed deprecated elements from previous versions.

### [1.3.0](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 21.08.2017

- **Dropped support** for _Android_ versions **below** _API Level 14_ for both versions of the library.
- Stability improvements.

### [1.2.0](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 09.05.2017

- **Annotations processing via reflection is by default DISABLED**. If desired, may be enabled via
  `FragmentAnnotations.setEnabled(true)`.
- If **Animator duration scale** developer setting is set to **off** the custom fragment animations
  specified via desired `FragmentTransition` are not set to `FragmentTransaction` as such animations
  would not be actually played by the **Android** animation framework. This concerns only **animator**
  based animations used for **not support** library version. This check has been added due to problem
  with **translate** animations. When these animations (animators) are used to transition between
  fragments and the animator duration scale setting is set to off, views of those fragments are just
  not shown/drawn even thought the fragments are properly added/shown.

### [1.1.1](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 03.03.2017

- Deprecated some not properly named methods of `FragmentRequest` and replaced with better named ones.
- Updated implementation of `FragmentController.newRequest(int)` and of `FragmentController.executeRequest(...)`.

### [1.1.0](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 19.01.2017

- `FragmentTransition` interface now extends `Parcelable`, this extension relation has been before
  declared for `BasicFragmentTransition` which implements `FragmentTransition`.

### [1.0.1](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 17.01.2017

- Removed interpolator from **alpha** animations/transitions.
- Updated **JavaDoc** for annotations.

### [1.0.0](https://bitbucket.org/android-universum/fragments/wiki/version/1.x) ###
> 02.01.2017

- First production release.
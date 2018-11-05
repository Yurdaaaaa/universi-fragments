Change-Log
===============
> Regular configuration update: _01.06.2018_

More **detailed changelog** for each respective version may be viewed by pressing on a desired _version's name_.

## Version 1.x ##

### [1.3.4](https://github.com/universum-studios/android_fragments/releases/tag/v1.3.4) ###
> upcoming

- Small updates and improvements.

### [1.3.3](https://github.com/universum-studios/android_fragments/releases/tag/v1.3.3) ###
> 19.07.2018

- Small updates and code quality improvements.

### [1.3.2](https://github.com/universum-studios/android_fragments/releases/tag/v1.3.2) ###
> 27.02.2018

- Stability improvements.

### [1.3.1](https://github.com/universum-studios/android_fragments/releases/tag/v1.3.1) ###
> 22.12.2017

- `FragmentController` now does not attach **custom animations** to `FragmentTransaction` also when
  a device is in **power save mode**.
- Removed deprecated elements from previous versions.

### [1.3.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.3.0) ###
> 21.08.2017

- **Dropped support** for _Android_ versions **below** _API Level 14_ for both versions of the library.
- Fixed [Issue #29](https://github.com/universum-studios/android_fragments/issues/29).

### [1.2.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.2.0) ###
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

### [1.1.1](https://github.com/universum-studios/android_fragments/releases/tag/v1.1.1) ###
> 03.03.2017

- Deprecated some not properly named methods of `FragmentRequest` and replaced with better named ones.
- Updated implementation of `FragmentController.newRequest(int)` and of `FragmentController.executeRequest(...)`.
  See **[Issue #3](https://github.com/universum-studios/android_fragments/issues/3)** for more info.

### [1.1.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.1.0) ###
> 19.01.2017

- `FragmentTransition` interface now extends `Parcelable`, this extension relation has been before
  declared for `BasicFragmentTransition` which implements `FragmentTransition`.

### [1.0.1](https://github.com/universum-studios/android_fragments/releases/tag/v1.0.1) ###
> 17.01.2017

- Removed interpolator from **alpha** animations/transitions.
- Updated **JavaDoc** for annotations.

### [1.0.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.0.0) ###
> 02.01.2017

- First production release.
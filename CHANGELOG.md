Change-Log
===============

## Version 1.x ##

### [1.3.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.3.0) ###
> 21.08.2017

- **Dropped support** for _Android_ versions **below** _API Level 14_ for both versions of the library.
- Fixed [Issue #29](https://github.com/universum-studios/android_fragments/issues/29).

### [1.2.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.2.0) ###
> 09.05.2017

- **Annotations processing via reflection is by default DISABLED**. If desired, may be enabled via
  `FragmentAnnotations.setEnabled(true)`.
- Deprecated `FragmentsConfig` and added `FragmentsLogging` along with `FragmentPolicies` in order
  to control log output of the library or to check what features are available for the Fragments
  API at the current **Android** API level.
- Added `BaseFragment.inflateTransition(int)` which may be used to inflate transitions in a simply way.
- If **Animator duration scale** developer setting is set to **off** the custom fragment animations
  specified via desired `FragmentTransition` are not set to `FragmentTransaction` as such animations
  would not be actually played by the **Android** animation framework. This concerns only **animator**
  based animations used for **not support** library version. This check has been added due to problem
  with **translate** animations. When these animations (animators) are used to transition between
  fragments and the animator duration scale setting is set to off, views of those fragments are just
  not shown/drawn even thought the fragments are properly added/shown.
- Removed deprecated methods of `FragmentRequest` class from the previous release.

### [1.1.1](https://github.com/universum-studios/android_fragments/releases/tag/v1.1.1) ###
> 03.03.2017

- Deprecated some not properly named methods of `FragmentRequest` and replaced with better named ones.
- Updated implementation of `FragmentController.newRequest(int)` and of `FragmentController.executeRequest(...)`.
  See **[#3 Issue](https://github.com/universum-studios/android_fragments/issues/3)** for more info.
- Code quality improvements.

### [1.1.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.1.0) ###
> 19.01.2017

- `FragmentTransition` interface now extends `Parcelable`, this extension relation has been before
  declared for `BasicFragmentTransition` which implements `FragmentTransition`. This is not a concern
  for applications that use only predefined transitions from `FragmentTransitions` or `ExtraFragmentTransitions`
  factory or use custom transitions that extend `BasicFragmentTransition`. All other fragment transitions
  that implement `FragmentTransition` interface directly are now required to meet `Parcelable`
  implementation requirements.

### [1.0.1](https://github.com/universum-studios/android_fragments/releases/tag/v1.0.1) ###
> 17.01.2017

- Removed interpolator from **alpha** animations/transitions.
- Updated **JavaDoc** for annotations.

### [1.0.0](https://github.com/universum-studios/android_fragments/releases/tag/v1.0.0) ###
> 02.01.2017

- First production release.
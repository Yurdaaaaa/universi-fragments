# Keep all fragment annotations.
-keep public @interface universum.studios.android.fragment.annotation.** { *; }
-keep @interface universum.studios.android.fragment.**$** { *; }
# Keep BaseFragment implementation details:
# - public empty constructor for proper working of instantiation process using reflection.
-keepclassmembers class * extends universum.studios.android.fragment.BaseFragment {
    public <init>();
}
# Keep fragment class annotations. We need to specify this rule in case of fragments that have multiple
# annotations presented above theirs class when in such case Proguard just keeps one of them.
-keep @universum.studios.android.fragment.annotation.** class *
# Keep members with @FactoryFragment annotation within fragment factories.
-keepclassmembers class * extends universum.studios.android.fragment.manage.BaseFragmentFactory {
    @universum.studios.android.fragment.annotation.FactoryFragment <fields>;
}
# Keep annotation handlers implementation details:
# - constructor taking Class parameter [always]
-keepclassmembers class * extends universum.studios.android.fragment.annotation.handler.BaseAnnotationHandler {
    public <init>(java.lang.Class);
}
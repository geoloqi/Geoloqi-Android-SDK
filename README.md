This is an example Android application that consumes the Geoloqi
Android SDK. It's a good starting point for anyone interested in
developing with the Geoloqi location APIs.

Installation
============
This app can be compiled and run in the standard fashion using Eclipse or
the command-line Android tools and ant. However, there are a couple of
quick tasks to complete before running the app.

If you have not already installed the [Android SDK][android-sdk]
do so now! It's also a good idea to make sure you're running the latest version.

Constants
---------
After checking out the sample code, you'll need to create a `Constants.java`
file that contains both your Geoloqi API Key and Secret. These are needed
to authenticate your requests with the server. A `ConstantsTemplate.java` file
has been provided for you in `src/com/geoloqi/android/sample/` that you
can rename and update with your key and secret.

Build Target
------------
The sample app targets Android API level 5 (Android 2.0), but you can change
the build target to any modern version. You'll need to use the 
[Android SDK Manager][android-sdk-components] to install the Android 2.0
platform or update the sample app to target a platform you've previously installed.

You can launch the SDK manager by running the `android` command from your
terminal. You can also launch the SDK manager from Eclipse (if you've installed
the Android plugin for Eclipse).

![Alt text](https://raw.github.com/geoloqi/Sample-Android-App/master/docs/images/android-sdk-manager-20.png)

Note that you may have to check the *Obsolete* checkbox to find the Android 2.0
platform listing.

Building
--------
Once you've installed the platform sources you're almost ready to build the
project. If you're using ant you can build the app immediately by executing
the build command in the project directory.

    # Build debug version
    $ ant clean debug

    # Install to device
    $ adb install bin/GeoloqiSampleAndroidApp.apk

Eclipse Build Path
------------------
If you're using Eclipse, you'll need to also add the Geoloqi SDK library
.jar to your project's build path. The .jar is located in the `libs/`
directory in the project root. Simply right-click the .jar and select
*Build Path -> Add to Build Path*.

![Alt text](https://raw.github.com/geoloqi/Sample-Android-App/master/docs/images/eclipse-build-path.png)

**Note for Eclipse users:** One common issue when importing a new Android project
occurs when Eclipse links your project against Java 1.5 instead of Java 1.6. If this
happens you'll see errors generated for all methods with `@Override` annotations.
You can [fix this][stackoverflow-override] by updating your [Eclipse/Project
preferences][eclipse-compiler-image] to ensure the Java compiler level is set to 1.6.


Adding the SDK to an Existing Project
=====================================

Before you begin, you'll need to modify your application's AndroidManifest.xml file to include the following permissions.

```
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

Optionally, you may also want to include the `ACCESS_MOCK_LOCATION` permission if you plan on doing any testing.

    <uses-permission android:name="android.permission.ACCESS_MOCK_LOCATION" />

You'll also need to declare the tracking service in your application's manifest.

```
<application>
    <service
        android:name="com.geoloqi.android.sdk.service.LQService"
        android:exported="false" />
</application>
```

Starting the Service
--------------------

The easiest way to get started is to spin up the tracking service when the user takes some action (such as launching your app). You can start the Geoloqi tracker like starting any other [Android Service](http://developer.android.com/reference/android/app/Service.html).

```
// Start the tracking service
Intent intent = new Intent(this, LQService.class);
intent.setAction(LQService.ACTION_START_WITH_ANONYMOUS_USER);
intent.putExtra(LQService.EXTRA_SDK_ID, Constants.LQ_SDK_ID);
intent.putExtra(LQService.EXTRA_SDK_SECRET, Constants.LQ_SDK_SECRET);
startService(intent);
```

This code will start the background service, create an anonymous user account and start requesting location updates from the system. It's that easy!

License
=======
Copyright 2011 by [Geoloqi.com][geoloqi-site] and contributors.

See LICENSE.

[geoloqi-site]: https://geoloqi.com/
[geoloqi-dev-site]: https://developers.geoloqi.com/
[android-sdk]: http://developer.android.com/sdk/index.html
[android-sdk-components]: http://developer.android.com/sdk/adding-components.html
[stackoverflow-override]: http://stackoverflow.com/a/1678170/772122
[eclipse-compiler-image]: https://raw.github.com/geoloqi/Sample-Android-App/master/docs/images/eclipse-compiler.png

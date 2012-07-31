This is an example Android application that consumes the Geoloqi
Android SDK. It's a good starting point for anyone interested in
developing with the Geoloqi location APIs.

For more information, visit: [developers.geoloqi.com][geoloqi-dev-site]

## Installation

This app can be compiled and run in the standard fashion using Eclipse or
the command-line Android tools and ant. However, there are a couple of
quick tasks to complete before running the app.

If you have not already installed the [Android SDK][android-sdk]
do so now! It's also a good idea to make sure you're running the latest version.

> Note: This documentation assumes the reader is familiar with the Android SDK
> at a basic level and knows how to build a simple Android project.
>
> If you find yourself struggling with the concepts outlined below, you might
> benefit from looking over the [Android Developer Docs][android-docs].

### Credentials

After checking out the sample code, you'll need to create a
`geoloqi.properties` file that contains both your Geoloqi API id
and secret. These are needed to authenticate your requests with the
server. A `geoloqi.properties.sample` file has been provided for you
in the `/assets` directory that you can copy and update with your
id and secret.

If for some reason you cannot provide a properties file, you can also
set your client credentials manually:

    LQSharedPreferences.setClientId("my_client_id");
    LQSharedPreferences.setClientSecret("my_client_secret");
    LQSharedPreferences.setGcmPushAccount("4815162342");
    LQSharedPreferences.setPushIcon("ic_stat_notify");

> Note: Make sure you set these values *before* starting the service!

### Build Target

The sample app targets Android API level 5 (Android 2.0), but you can change
the build target to any modern version. You'll need to use the 
[Android SDK Manager][android-sdk-components] to install the Android 2.0
platform or update the sample app to target a platform you've previously installed.

You can launch the SDK manager by running the `android` command from your
terminal. You can also launch the SDK manager from Eclipse (if you've installed
the Android plugin for Eclipse).

![Alt text](https://raw.github.com/geoloqi/Geoloqi-Android-SDK/master/docs/images/android-sdk-manager-20.png)

Note that you may have to check the *Obsolete* checkbox to find the Android 2.0
platform listing.

### Building

Once you've installed the platform sources you're almost ready to build the
project. If you're using ant you can build the app immediately by executing
the build command in the project directory.

    # Build debug version
    $ ant clean debug

    # Install to device
    $ adb install bin/GeoloqiSampleAndroidApp.apk

**Note for Eclipse users:** One common issue when importing a new Android project
occurs when Eclipse links your project against Java 1.5 instead of Java 1.6. If this
happens you'll see errors generated for all methods with `@Override` annotations.
You can [fix this][stackoverflow-override] by updating your [Eclipse/Project
preferences][eclipse-compiler-image] to ensure the Java compiler level is set to 1.6.

### Eclipse Javadoc

The Geoloqi Android SDK Javadoc is bundled with the sample application as
a jar file in the `libs/` directory. If you're using the latest version of
the Android Developer Tools plugin the docs should be loaded automatically.

## Existing Projects

If you have an existing project and would like to use the Geoloqi Android SDK
you can simply copy the `.jar` files from the sample application's `libs/`
directory to your project's `libs/` directory.

### AndroidManifest.xml

You'll need to make sure your project's `AndroidManifest.xml` declares
certain permissions and services, otherwise the Geoloqi SDK may
not function as expected.

> Note: You can run the included script `tools/generate-manifest` from a
> terminal to generate a new barebone AndroidManifest.xml.

The best way to get started is to copy the `AndroidManifest.xml` from
the sample project and replace any instance of the package name
`com.geoloqi.android.sample` with your own package name.

> Note: Push messaging will not work if you do not update the manifest
> with the correct package name!

### Starting the Tracker

The easiest way to get started is to spin up the tracking service when
the user takes some action (such as launching your app). You can start
the Geoloqi tracker like starting any other [Android Service][android-service].

```java
    // Start the tracking service
    Intent intent = new Intent(this, LQService.class);
    startService(intent);
```

This code will start the background service, create an anonymous user
account and start requesting location updates from the system. It's that easy!

## Push messaging

To set up your Google account as a GCM sender, please follow the
tutorial on the [Geoloqi developer site][push-messaging].

## License

Copyright 2011 by [Geoloqi.com][geoloqi-site] and contributors.

See LICENSE.

[geoloqi-site]: https://geoloqi.com/
[geoloqi-dev-site]: https://developers.geoloqi.com/
[android-docs]: http://developer.android.com/
[android-sdk]: http://developer.android.com/sdk/index.html
[android-sdk-components]: http://developer.android.com/sdk/adding-components.html
[android-service]: http://developer.android.com/reference/android/app/Service.html
[stackoverflow-override]: http://stackoverflow.com/a/1678170/772122
[eclipse-compiler-image]: https://raw.github.com/geoloqi/Geoloqi-Android-SDK/master/docs/images/eclipse-compiler.png
[push-messaging]: https://developers.geoloqi.com/android/push-notifications

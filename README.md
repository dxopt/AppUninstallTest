AppUninstallTest
===========

This app was written to verify a system bug.

When uninstall an app or update an app, there may be some files left in the folder /data/data/&lt;app package name&gt;. If that case happened, you can not install the app again because of the error "INSTALL\_FAILED\_UID\_CHANGED". If your device is rooted, then you can remove the app data folder manually. Otherwise, you have to do factory data reset in order to install the app again.



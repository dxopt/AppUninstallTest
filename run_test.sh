#!/bin/sh

echo ">> Build the app..."
./gradlew -q clean aDebug

echo ">> Prepare for test. Uninstall the app if installed..."
adb uninstall me.ycdev.android.demo.appuninstalltest 1>/dev/null 2>/dev/null

FILES_COUNT=5000
SLEEP_SECONDS=60
MAX_COUNT=60
count=1

while [ $count -le $MAX_COUNT ] ; do
    echo ">> Running test ($count/$MAX_COUNT)..."
    date

    adb install app/build/outputs/apk/app-debug.apk | grep -i "Failure"
    if [ $? -eq 0 ] ; then
        echo "++ Failed to install app. Please check the reason."
        exit 1
    fi

    adb shell am start -n "me.ycdev.android.demo.appuninstalltest/me.ycdev.android.demo.appuninstalltest.MainActivity" --ez test.auto true --ei files.count $FILES_COUNT
    echo "++ Sleeping...$SLEEP_SECONDS seconds"
    sleep $SLEEP_SECONDS

    adb uninstall me.ycdev.android.demo.appuninstalltest | grep -i "Failure"
    if [ $? -eq 0 ] ; then
        echo "++ Failed to uninstall app"
        exit 2
    fi

    count=`expr $count + 1`
done

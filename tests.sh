#!/bin/bash
BUILD_STATUS=0

start_emulator && wait_emulator 

# Clean gradle tasks cache from old builds
./gradlew clean --daemon -PdisablePreDex --stacktrace

./gradlew check --daemon -PdisablePreDex --stacktrace
UNIT_STATUS=$?

./gradlew assembleDebugAndroidTest --daemon -PdisablePreDex --stacktrace

unlock_emulator

# Capture logs from emulator
adb logcat -c
adb logcat > logcat.log &
LOGCAT_PID=$!

# Running integration tests
./gradlew connectedCheck -i --daemon -PdisablePreDex --stacktrace
INTEGRATION_STATUS=$?

kill $LOGCAT_PID

# Uploading artifacts to github pages
git remote add upstream "https://$GITHUB_ACCESS_TOKEN@github.com/Catbag/redux-android-sample.git"
git config --global user.name "Drone CI"
git config --global user.email "developer@catbag.com.br"
git fetch upstream gh-pages
git checkout upstream/gh-pages -m
git add app/build/reports/ -f
git add logcat.log -f
git commit -am "Publish results from test #$DRONE_BUILD_NUMBER"
git checkout -b gh-pages
git push -f upstream gh-pages

# Computing build status
BUILD_STATUS=$((UNIT_STATUS + INTEGRATION_STATUS))

# Returning build status
exit $BUILD_STATUS

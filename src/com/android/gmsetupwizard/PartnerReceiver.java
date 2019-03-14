/* Copyright (C) 2014 Google Inc. All Rights Reserved. */

package com.android.gmsetupwizard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * This is not a real receiver, but only used as a marker interface so that Setup Wizard can resolve
 * this package and fetch resources from here.
 *
 * This example will change the theme of Setup Wizard to dark theme and add AdditionalActivity after
 * showing the Welcome screen. This sample only overrides the owner script and keeps the user script
 * unchanged.
 *
 * To install this sample, first build the APK (mm in the SetupSampleOverlay directory), then run
 * the following commands to push the APK onto the device and reboot.
 *
 *     adb remount
 *     adb push $OUT/system/app/SetupSampleOverlay/SetupSampleOverlay.apk \
 *             /system/app/SetupSampleOverlay.apk
 *     adb reboot
 *
 * @see com.google.android.setupwizard.util.Partner
 */
public class PartnerReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
    }
}

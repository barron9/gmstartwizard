/* Copyright (C) 2016 Google Inc. All Rights Reserved. */

package com.android.gmsetupwizard;

import android.content.Intent;

import com.android.setupwizardlib.util.ResultCodes;

/**
 * Wrapper to launch Google Assistant opt-in screen.
 */
public class OpaWrapper extends SubactivityWrapper {

    static final String OPA_OPT_IN_WRAPPED_ACTION =
            "com.google.android.apps.gsa.opa.OPA_OPT_IN_WRAPPED";

    public interface OpaSubactivity {
        int REQUEST_CODE = 1;

        int RESULT_OPTED_IN = ResultCodes.RESULT_FIRST_SETUP_USER;
        int RESULT_NOT_OPTED_IN = ResultCodes.RESULT_FIRST_SETUP_USER + 1;
    }

    @Override
    protected void onStartSubactivity() {
        final Intent intent = new Intent(OPA_OPT_IN_WRAPPED_ACTION);
        startSubactivity(intent, OpaSubactivity.REQUEST_CODE);
    }

    @Override
    protected void onSubactivityResult(int requestCode, int resultCode, Intent data) {
        super.onSubactivityResult(requestCode, resultCode, data);
        if (resultCode == ResultCodes.RESULT_SKIP) {
            // RESULT_SKIP is should be returned when the OPA screen should not be shown to the user
            // at all, e.g. if the account is not eligible for OPA.
            finish();
        }
    }
}

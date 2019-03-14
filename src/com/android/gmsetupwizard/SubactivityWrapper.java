/* Copyright (C) 2014 Google Inc. All Rights Reserved. */

package com.android.gmsetupwizard;

import static com.android.setupwizardlib.util.ResultCodes.RESULT_ACTIVITY_NOT_FOUND;

import com.android.gmsetupwizard.R;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.setupwizardlib.GlifLayout;
import com.android.setupwizardlib.util.WizardManagerHelper;

/**
 * Handles the business logic surrounding a scripted activity, especially those provided by other
 * packages. Subclasses must provide the intent of the subactivity to be launched and the
 * requestCode that represents it. When the subactivity finishes, the resultCode is processed here;
 * RESULT_CANCELED causes this wrapper to finish as well, while any other resultCode allows the
 * setup flow to advance, keeping this wrapper activity in place on the back stack, should the user
 * need to return later.
 */
public abstract class SubactivityWrapper extends Activity {

    private static final String TAG = "SubactivityWrapper";
    /**
     * Key for storing the request code of the started activity.
     */
    private static final String INSTANCE_STATE_REQUEST_CODE = "request_code";
    private static final int REQUEST_CODE_NEXT = 1000;

    protected boolean mIsSubactivityNotFound = false;

    /**
     * The request code for the most recent activity started by
     * {@link #startSubactivity(android.content.Intent, int)}. This is used to determine whether
     * an activity result comes from the subactivity, in which case
     * {@link #onSubactivityResult(int, int, android.content.Intent)} will be called.
     */
    private int mRequestCode;

    /**
     * Launch our subactivity if we're starting a new screen. Other life cycle management is handled
     * during onActivityResult().
     */
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        if (icicle == null) {
            onStartSubactivity();
        }
        setContentView(R.layout.subactivity_wait);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_STATE_REQUEST_CODE, mRequestCode);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mRequestCode = savedInstanceState.getInt(INSTANCE_STATE_REQUEST_CODE);
    }

    /**
     * Called when a subactivity should be started. Implementations should build the intent and
     * call {@link #startSubactivity(android.content.Intent, int)} to launch the subactivity.
     */
    protected abstract void onStartSubactivity();

    protected void startSubactivity(Intent subactivityIntent, int requestCode) {
        mRequestCode = requestCode;
        final Intent intent = getIntent();
        WizardManagerHelper.copyWizardManagerExtras(intent, subactivityIntent);

        boolean activityForwardsResult =
                (subactivityIntent.getFlags() & Intent.FLAG_ACTIVITY_FORWARD_RESULT) != 0;
        try {
            if (activityForwardsResult) {
                // IMPORTANT: Make sure that the activity launched here matches the current task
                // affinity, otherwise, finishing this wrapper will also force-finish the other
                startActivity(subactivityIntent);
                setResult(RESULT_OK);
                finish();
            } else {
                startActivityForResult(subactivityIntent, requestCode);
            }
            mIsSubactivityNotFound = false;
            overridePendingTransitionToSlideForward();
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "intent has no matching activity; start next screen and finish;"
                    + " intent=" + subactivityIntent);
            if (activityForwardsResult) {
                // If the activity was launched using startActivity() we have to invoke
                // the next activity ourselves, otherwise the wizard will become stuck.
                final Intent nextIntent =
                        WizardManagerHelper.getNextIntent(getIntent(), RESULT_ACTIVITY_NOT_FOUND);
                startActivityForResult(nextIntent, REQUEST_CODE_NEXT);
                finish();
            } else {
                // Otherwise, when the activity is not found onActivityResult(CANCELED) will be
                // called. Store this in a boolean flag and handle it in onSubactivityResult.
                mIsSubactivityNotFound = true;
            }
        }
    }

    /**
     * Process the results for our subactivity or other subsequent activities. For our subactivity,
     * handling is relayed to {@link #onSubactivityResult}. If another activity has canceled, we
     * will relaunch our subactivity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "SubactivityWrapper.onActivityResult(" + requestCode + ", " + resultCode);
        }
        if (requestCode == mRequestCode) {
            // Handle the results of our subactivity
            Log.i(TAG, "subactivity result {" + requestCode + ", " + resultCode + ", "
                    + (data != null ? data.getExtras() : null) + "}");
            onSubactivityResult(requestCode, resultCode, data);
        } else if (resultCode == RESULT_CANCELED) {
            // If coming back from another activity, we need to show our subactivity again
            onStartSubactivity();
            overridePendingTransitionToSlideBackward();
        } else {
            // If another activity has finished, moving forward, pass along the result to
            // BaseActivity for resolution.
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Called when our subactivity exits. Standard behavior is to finish (move back) when the
     * subactivity cancels, or move ahead to the next screen for any other result
     */
    protected void onSubactivityResult(int requestCode, int resultCode, Intent data) {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "SubactivityWrapper.onSubactivityResult("
                    + requestCode + ", " + resultCode
                    + ", " + (data != null ? data.getExtras() : null) + ")");
        }
        // Propagate the results of our subactivity
        if (resultCode == RESULT_CANCELED) {
            if (mIsSubactivityNotFound) {
                overridePendingTransitionToSlideBackward();
                final Intent nextIntent =
                        WizardManagerHelper.getNextIntent(getIntent(), RESULT_ACTIVITY_NOT_FOUND);
                startActivityForResult(nextIntent, REQUEST_CODE_NEXT);
                finish();
            } else {
                overridePendingTransitionToSlideForward();
                setResult(RESULT_CANCELED, data);
                finish();
            }
        } else {
            final Intent nextIntent = WizardManagerHelper.getNextIntent(getIntent(), resultCode);
            startActivityForResult(nextIntent, REQUEST_CODE_NEXT);
        }
    }

    private void overridePendingTransitionToSlideForward() {
        overridePendingTransition(R.anim.suw_slide_next_in, R.anim.suw_slide_next_out);
    }

    private void overridePendingTransitionToSlideBackward() {
        overridePendingTransition(R.anim.suw_slide_back_in, R.anim.suw_slide_back_out);
    }
}

package com.android.gmsetupwizard.Welcome;

import android.content.Intent;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.app.AlertDialog;
import com.android.setupwizardlib.SetupWizardLayout;
import android.content.SharedPreferences;
import android.widget.Switch;
import android.content.Context;

import com.android.gmsetupwizard.R;
import com.android.setupwizardlib.util.WizardManagerHelper;
import com.android.setupwizardlib.view.NavigationBar.NavigationBarListener;

public class GMWelcome extends Activity implements NavigationBarListener {

	private static final String GM_SERVICE_NAME = "gm_service_preferences";
	private static final String IMPROVE_ALLOW_KEY = "improve_allow";
	private static final String MARKETING_ALLOW_KEY = "marketing_allow";
	private static final String SYSTEM_UPDATE_ALLOW_KEY = "system_update_allow";	
	private static final String WIZARD_COMPLETED_KEY = "setup_wizard_finish";
	private static final int RESULT_ACTIVATION = 102;
	private SetupWizardLayout sl;

	private Switch switch1,switch2,switch3;
	private TextView privacy;


	@Override
	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_gm_welcome);
		init();
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
		    getWindow().getDecorView().setSystemUiVisibility(
			    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
				    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
				    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
				    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				    | View.SYSTEM_UI_FLAG_FULLSCREEN
				    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		}
	}

	private void init() {

		sl = (SetupWizardLayout) findViewById(R.id.bip_welcome_layout);
		sl.setHeaderText(getString(R.string.general_mobile_services));
                sl.getNavigationBar().setNavigationBarListener(this);
		switch1 = (Switch) findViewById(R.id.helpSwitch);
		switch2 = (Switch) findViewById(R.id.helpSwitch2);
		switch3 = (Switch) findViewById(R.id.helpSwitch3);
		privacy = (TextView) findViewById(R.id.privacyPolicy);

		privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(GMWelcome.this).setTitle(R.string.privacy_policy_header).setMessage(R.string.privacy_policy).create();
                alertDialog.show();
            }
        });
	}

	@Override
	public void onNavigateNext() {
		SharedPreferences pref = getSharedPreferences(GM_SERVICE_NAME, Context.MODE_MULTI_PROCESS);
		pref.edit().putBoolean(IMPROVE_ALLOW_KEY, switch1.isChecked()).apply();
		pref.edit().putBoolean(MARKETING_ALLOW_KEY, switch2.isChecked()).apply();
		pref.edit().putBoolean(SYSTEM_UPDATE_ALLOW_KEY, switch3.isChecked()).apply();
		pref.edit().putBoolean(WIZARD_COMPLETED_KEY, true).apply();

		Intent nextIntent = WizardManagerHelper.getNextIntent(getIntent(), RESULT_ACTIVATION);
		
		startActivityForResult(nextIntent, 1000);
	}

	@Override
	public void onNavigateBack() {
		onBackPressed();
	}
}

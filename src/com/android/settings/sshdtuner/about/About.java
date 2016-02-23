/*
 * Copyright (C) 2016 The Sungsonic Co
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.sshdtuner.about;

import android.app.Activity;
import android.app.ActivityManagerNative;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Build;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;
import android.preference.Preference.OnPreferenceChangeListener;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.Display;
import android.view.Window;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;
import com.android.settings.Utils;
import com.android.internal.logging.MetricsLogger;    

public class About extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener {
			
public static final String TAG = "About";
    
private static final String SSHD_ROM_SHARE = "share";
    
    Preference mSiteUrl;
    Preference mDownUrl;
	Preference mchangUrl;
    Preference mSourceUrl;
	Preference mDonateUrl;
    Preference mGoogleUrl;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sshd_about);
        PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getContentResolver();
        mSiteUrl = findPreference("sshd_web");
        mDownUrl = findPreference("sshd_down");
        mchangUrl = findPreference("sshd_changelog");
		mSourceUrl = findPreference("sshd_github");
        mDonateUrl = findPreference("sshd_donate");
        mGoogleUrl = findPreference("sshd_gplus");        
        PreferenceGroup devsGroup = (PreferenceGroup) findPreference("devs");
        ArrayList<Preference> devs = new ArrayList<Preference>();
        for (int i = 0; i < devsGroup.getPreferenceCount(); i++) {
            devs.add(devsGroup.getPreference(i));
        }
        devsGroup.removeAll();
        devsGroup.setOrderingAsAdded(false);
        Collections.shuffle(devs);
        for(int i = 0; i < devs.size(); i++) {
            Preference p = devs.get(i);
            p.setOrder(i);

            devsGroup.addPreference(p);
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        return false;
    }
    
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mSiteUrl) {
            launchUrl("http://sungsonic.co/");
        } else if (preference == mDownUrl) {
            launchUrl("http://downloads.sungsonic.co/");
        } else if (preference == mchangUrl) {
            launchUrl("https://sunsonic.co/changelog");
        } else if (preference == mSourceUrl) {
            launchUrl("https://github.com/Sungsonic");
        } else if (preference == mDonateUrl) {
            launchUrl("http://forum.xda-developers.com/donatetome.php?u=4284308");
        } else if (preference == mGoogleUrl) {
            launchUrl("https://plus.google.com/communities/107660411205120447507");
        } else if (preference.getKey().equals(SSHD_ROM_SHARE)) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, String.format(
                    getActivity().getString(R.string.share_message), Build.MODEL));
            startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
            }  else {
                // If not handled, let preferences handle it.
                return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
         return true; 
    }
    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent donate = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(donate);
    }
    protected int getMetricsCategory()
    {
	return MetricsLogger.SSHDTUNER;
    }
}
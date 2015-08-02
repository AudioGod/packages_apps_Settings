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

package com.android.settings.sshdtuner.lockscreen;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import com.android.internal.logging.MetricsLogger;

public class Lockscreen extends SettingsPreferenceFragment {


    public static final int IMAGE_PICK = 1;

    private static final String KEY_WALLPAPER_SET = "lockscreen_wallpaper_set";
    private static final String KEY_WALLPAPER_CLEAR = "lockscreen_wallpaper_clear";

    private Preference mSetWallpaper;
    private Preference mClearWallpaper;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.sshd_lockscreen);

        mSetWallpaper = (Preference) findPreference(KEY_WALLPAPER_SET);
        mClearWallpaper = (Preference) findPreference(KEY_WALLPAPER_CLEAR);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mSetWallpaper) {
            setKeyguardWallpaper();
            return true;
        } else if (preference == mClearWallpaper) {
            clearKeyguardWallpaper();
            return true;
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri uri = data.getData();
                Intent intent = new Intent();
                intent.setClassName("com.android.wallpapercropper", "com.android.wallpapercropper.WallpaperCropActivity");
                intent.putExtra("keyguardMode", "1");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setData(uri);
                startActivity(intent);
            }
        }
    }

    private void setKeyguardWallpaper() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK);
    }

    private void clearKeyguardWallpaper() {
        WallpaperManager wallpaperManager = null;
        wallpaperManager = WallpaperManager.getInstance(getActivity());
        wallpaperManager.clearKeyguardWallpaper();
    }
	
	public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
        @Override
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                    boolean enabled) {
            ArrayList<SearchIndexableResource> result =
                new ArrayList<SearchIndexableResource>();

            SearchIndexableResource sir = new SearchIndexableResource(context);
            sir.xmlResId = R.xml.sshd_lockscreen;
            result.add(sir);

            return result;
        }

        @Override
        public List<String> getNonIndexableKeys(Context context) {
            ArrayList<String> result = new ArrayList<String>();
            return result;
        }
    };
	
	@Override
	protected int getMetricsCategory() {
        return MetricsLogger.SSHDTUNER;
    }

}
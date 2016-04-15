/*
 * This file is part of Adblock Plus <https://adblockplus.org/>,
 * Copyright (C) 2006-2015 Eyeo GmbH
 *
 * Adblock Plus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3 as
 * published by the Free Software Foundation.
 *
 * Adblock Plus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Adblock Plus.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.pl.adblocker;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.regex.Pattern;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.pl.adblocker.core.FilterEngine.ContentType;

public class ADBlocker {
	public static final String ADBLOCKER = "adblocker";

	private static final Pattern RE_JS = Pattern.compile("\\.js$",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern RE_CSS = Pattern.compile("\\.css$",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern RE_IMAGE = Pattern.compile(
			"\\.(?:gif|png|jpe?g|bmp|ico)$", Pattern.CASE_INSENSITIVE);
	private static final Pattern RE_FONT = Pattern.compile("\\.(?:ttf|woff)$",
			Pattern.CASE_INSENSITIVE);
	private static final Pattern RE_HTML = Pattern.compile("\\.html?$",
			Pattern.CASE_INSENSITIVE);

	private static final String ADBLOCK_DIRECTORY = ADBLOCKER;
	private static final String INI_NAME = "patterns.ini";
	private static final String SHARED_ENABLE = "enabled";
	private static final String SHARED_SUBSCRIPTION = "subscription";
	private static final String SUBSCRIPION_URL = "https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt";

	private static final String BACKUP_SUFFIX = "-backup";
	private boolean hasStarted = false;

	private String path;

	/**
	 * Broadcasted when subscription status changes.
	 */
	public static final String BROADCAST_SUBSCRIPTION_STATUS = "org.adblockplus.android.subscription.status";

	/**
	 * Indicates whether filtering is enabled or not.
	 */
	private boolean filteringEnabled = false;

	private ABPEngine abpEngine;

	private static ADBlocker instance;

	private static Application mApplication;

	private final ReferrerMapping referrerMapping = new ReferrerMapping();

	/**
	 * Returns pointer to itself (singleton pattern).
	 */
	public static Application getApplication() {
		return mApplication;
	}

	public static void setApplication(Application application) {
		ADBlocker.mApplication = application;
	}

	public static ADBlocker getInstance() {
		if (instance == null) {
			instance = new ADBlocker();
		}
		return instance;
	}

	private ADBlocker() {
	}

	/**
	 * Adds provided subscription and removes previous subscriptions if any.
	 *
	 * @param url
	 *            URL of subscription to add
	 */
	public void setSubscription(final String url) {
		abpEngine.setSubscription(url);
	}

	/**
	 * Forces subscriptions refresh.
	 */
	public void refreshSubscriptions() {
		abpEngine.refreshSubscriptions();
	}

	/**
	 * Enforces subscription status update.
	 *
	 * @param url
	 *            Subscription url
	 */
	public void updateSubscriptionStatus(final String url) {
		abpEngine.updateSubscriptionStatus(url);
	}

	/**
	 * Enables or disables Acceptable Ads
	 */
	public void setAcceptableAdsEnabled(final boolean enabled) {
		abpEngine.setAcceptableAdsEnabled(enabled);
	}

	/**
	 * Checks if filters match request parameters.
	 *
	 * @param url
	 *            Request URL
	 * @param query
	 *            Request query string
	 * @param referrer
	 *            Request referrer header
	 * @param accept
	 *            Request accept header
	 * @return true if matched filter was found
	 * @throws Exception
	 */
	public boolean matches(final String url, final String query,
			final String referrer, final String accept) {
		final String fullUrl = !TextUtils.isEmpty(query) ? url + "?" + query
				: url;
		if (referrer != null)
			referrerMapping.add(fullUrl, referrer);

		if (!filteringEnabled || !hasStarted)
			return false;

		ContentType contentType = null;

		if (accept != null) {
			if (accept.contains("text/css"))
				contentType = ContentType.STYLESHEET;
			else if (accept.contains("image/*"))
				contentType = ContentType.IMAGE;
			else if (accept.contains("text/html"))
				contentType = ContentType.SUBDOCUMENT;
		}

		if (contentType == null) {
			if (RE_JS.matcher(url).find())
				contentType = ContentType.SCRIPT;
			else if (RE_CSS.matcher(url).find())
				contentType = ContentType.STYLESHEET;
			else if (RE_IMAGE.matcher(url).find())
				contentType = ContentType.IMAGE;
			else if (RE_FONT.matcher(url).find())
				contentType = ContentType.FONT;
			else if (RE_HTML.matcher(url).find())
				contentType = ContentType.SUBDOCUMENT;
		}
		if (contentType == null)
			contentType = ContentType.OTHER;

		final List<String> referrerChain = referrerMapping
				.buildReferrerChain(referrer);
		final String[] referrerChainArray = referrerChain
				.toArray(new String[referrerChain.size()]);
		return abpEngine.matches(fullUrl, contentType, referrerChainArray);
	}

	/**
	 * Checks if filtering is enabled.
	 */
	public boolean isEnabled() {
		return filteringEnabled;
	}

	/**
	 * Enables or disables filtering.
	 */
	public void setEnabled(Application application,
			final boolean enable) {
		if (application == null) {
			application = mApplication;
		}
		if (application == null) {
			return;
		}
		filteringEnabled = enable;
		SharedPreferences sp = application.getSharedPreferences(
				application.getPackageName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putBoolean(SHARED_ENABLE, enable);
		editor.commit();
	}

	/**
	 * Starts ABP engine. It also initiates subscription refresh if it is
	 * enabled in user settings.
	 */
	public void startEngine() throws Throwable {
		if (abpEngine == null) {
			final File basePath = new File(mApplication.getFilesDir()
					.getAbsolutePath() + File.separator + ADBLOCK_DIRECTORY);
			basePath.mkdirs();
			abpEngine = ABPEngine.create(mApplication,
					ABPEngine.generateAppInfo(mApplication),
					basePath.getAbsolutePath());
		}
		hasStarted=true;
	}

	/**
	 * Stops ABP engine.
	 */
	public void stopEngine() {
		hasStarted = false;
	}
	
	public void destroyEngine(){
		if (abpEngine != null) {
			Log.d("AdBlockNew", "destroyEngine start!");
			abpEngine.dispose();
			abpEngine = null;
			Log.d("AdBlockNew", "destroyEngine done!");
		}
		hasStarted = false;
	}

	public void createAndStart(Application application) {
		Log.d("AdBlockNew", "adblock creat start");
		mApplication = application;
		SharedPreferences sp = mApplication.getSharedPreferences(
				mApplication.getPackageName(), Context.MODE_PRIVATE);
		filteringEnabled = sp.getBoolean(SHARED_ENABLE, true);
		if (filteringEnabled) {
			new Thread(new Runnable() { 
				@Override
				public void run() {
					Log.d("AdBlockNew", "adblock creat thread start");
					long start = System.currentTimeMillis();
					try {
						initAdblockPlus();
						deleteBackUpFile();
						hasStarted = true;
					} catch (Throwable e) {
						// TODO: handle exception
						hasStarted = false;
					}
					long end = System.currentTimeMillis();
					Log.d("AdBlockNew", "loadcost=" + (end - start)
							+ "ms,success=" + hasStarted);
				}
			}).start();
		}
	}

	// 删除备份文件，防止files目录过大
	private void deleteBackUpFile() {
		File filterDirectory = new File(path);
		File[] backupFiles = filterDirectory.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.getName().contains(BACKUP_SUFFIX))
					return true;
				else
					return false;
			}
		});
		for (File backupFile : backupFiles) {
			backupFile.delete();
		}
	}

	private void initAdblockPlus() throws Throwable {
		path = getApplication().getFilesDir().getAbsolutePath() + File.separator
				+ ADBLOCK_DIRECTORY;
		File patternsFile = new File(path + File.separator + INI_NAME);
		
		if (abpEngine == null) {
			startEngine();
			if (!patternsFile.exists()) {
				setSubscription(getSubscriptionUrl(mApplication));
				updateSubscriptionStatus(SUBSCRIPION_URL); //若打开，则会自动更新规则文件
				refreshSubscriptions(); //若打开，则会自动更新规则文件
				// setAcceptableAdsEnabled(true); //若打开，则会自动下载acceptable的规则
			}
		}		
	}

	public String getSubscriptionUrl(Application application){
		if (application == null) {
			application = mApplication;
		}
		if (application == null) {
			return SUBSCRIPION_URL;
		}
		SharedPreferences sp = application.getSharedPreferences(
				application.getPackageName(), Context.MODE_PRIVATE);
		return sp.getString(SHARED_SUBSCRIPTION, SUBSCRIPION_URL);
	}
	
	
	public void setSubscriptionUrl(Application application,String url) {
		if (application == null) {
			application = mApplication;
		}
		if (application == null) {
			return;
		}
		SharedPreferences sp = application.getSharedPreferences(
				application.getPackageName(), Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		// editor.putString("subscription",
		// "https://easylist-downloads.adblockplus.org/easylistchina+easylist.txt");
		editor.putString(SHARED_SUBSCRIPTION, SUBSCRIPION_URL);
		editor.commit();
		setSubscription(url);
	}

}

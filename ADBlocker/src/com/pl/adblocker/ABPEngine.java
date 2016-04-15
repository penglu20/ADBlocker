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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.util.Log;

import com.pl.adblocker.core.AppInfo;
import com.pl.adblocker.core.Filter;
import com.pl.adblocker.core.FilterEngine;
import com.pl.adblocker.core.JsEngine;
import com.pl.adblocker.core.Subscription;

import java.util.List;
import java.util.Locale;

public final class ABPEngine {
	private static final String TAG = Utils.getTag(ABPEngine.class);

	private final Context context;
	static {
		try {
			// System.load(AdblockPlus.soPath);
			System.loadLibrary(ADBlocker.ADBLOCKER);
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/*
	 * The fields below are volatile because:
	 * 
	 * I encountered JNI related bugs/crashes caused by JNI backed Java objects.
	 * It seemed that under certain conditions the objects were optimized away
	 * which resulted in crashes when trying to release the object, sometimes
	 * even on access.
	 * 
	 * The only solution that really worked was to declare the variables holding
	 * the references volatile, this seems to prevent the JNI from 'optimizing
	 * away' those objects (as a volatile variable might be changed at any time
	 * from any thread).
	 */
	private volatile JsEngine jsEngine;
	private volatile FilterEngine filterEngine;

	private ABPEngine(final Context context) {
		this.context = context;
	}

	public static AppInfo generateAppInfo(final Context context) {
		final boolean developmentBuild = false;// !context.getResources().getBoolean(R.bool.def_release);
		String version = "0";
		try {
			final PackageInfo info = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			version = info.versionName;
			if (developmentBuild)
				version += "." + info.versionCode;
		} catch (final NameNotFoundException e) {
			Log.e(TAG, "Failed to get the application version number", e);
		}
		final String sdkVersion = String.valueOf(VERSION.SDK_INT);
		final String locale = Locale.getDefault().toString().replace('_', '-');

		return AppInfo.builder().setVersion(version)
				.setApplicationVersion(sdkVersion).setLocale(locale)
				.setDevelopmentBuild(developmentBuild).build();
	}

	public static ABPEngine create(final Context context,
			final AppInfo appInfo, final String basePath) throws Throwable {
		final ABPEngine engine = new ABPEngine(context);

		engine.jsEngine = new JsEngine(appInfo);
		engine.jsEngine.setDefaultFileSystem(basePath);
		engine.filterEngine = new FilterEngine(engine.jsEngine);
		return engine;
	}

	public void dispose() {
		// Safe disposing (just in case)
		if (this.filterEngine != null) {
			this.filterEngine.dispose();
			this.filterEngine = null;
		}

		if (this.jsEngine != null) {
			this.jsEngine.dispose();
			this.jsEngine = null;
		}

	}

	public boolean isFirstRun() {
		return this.filterEngine.isFirstRun();
	}

	private static JSubscription convertJsSubscription(
			final Subscription jsSubscription) {
		final JSubscription subscription = new JSubscription();

		subscription.title = jsSubscription.getProperty("title").toString();
		subscription.url = jsSubscription.getProperty("url").toString();

		return subscription;
	}

	private static JSubscription[] convertJsSubscriptions(
			final List<Subscription> jsSubscriptions) {
		final JSubscription[] subscriptions = new JSubscription[jsSubscriptions
				.size()];

		for (int i = 0; i < subscriptions.length; i++) {
			subscriptions[i] = convertJsSubscription(jsSubscriptions.get(i));
		}

		return subscriptions;
	}

	public void setSubscription(final String url) {
		if (this.filterEngine.getSubscription(url).equals(
				this.filterEngine.getListedSubscriptions().get(0))) {
			return;
		}
		for (final Subscription s : this.filterEngine.getListedSubscriptions()) {
			s.removeFromList();
		}

		final Subscription sub = this.filterEngine.getSubscription(url);
		if (sub != null) {
			sub.addToList();
		}
	}

	public void refreshSubscriptions() {
		for (final Subscription s : this.filterEngine.getListedSubscriptions()) {
			s.updateFilters();
		}
	}

	public void setAcceptableAdsEnabled(final boolean enabled) {
		final String url = this.filterEngine.getPref(
				"subscriptions_exceptionsurl").toString();
		final Subscription sub = this.filterEngine.getSubscription(url);
		if (sub != null) {
			if (enabled) {
				sub.addToList();
			} else {
				sub.removeFromList();
			}
		}
	}

	public String getDocumentationLink() {
		return this.filterEngine.getPref("documentation_link").toString();
	}

	public boolean matches(final String fullUrl,
			final FilterEngine.ContentType contentType,
			final String[] referrerChainArray) {
		final Filter filter = this.filterEngine.matches(fullUrl, contentType,
				referrerChainArray);

		if (filter == null) {
			return false;
		}

		// hack: if there is no referrer, block only if filter is
		// domain-specific
		// (to re-enable in-app ads blocking, proposed on 12.11.2012 Monday
		// meeting)
		// (documentUrls contains the referrers on Android)
		if (referrerChainArray.length == 0
				&& (filter.getProperty("text").toString()).contains("||")) {
			return false;
		}
		boolean res = filter.getType() != Filter.Type.EXCEPTION;
		return res;
	}

	public void updateSubscriptionStatus(final String url) {
		final Subscription sub = this.filterEngine.getSubscription(url);
		if (sub != null) {
			Utils.updateSubscriptionStatus(this.context, sub);
		}
	}
}

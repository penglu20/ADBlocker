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

package com.pl.adblocker.core;

import com.pl.adblocker.ADBlocker;

import java.util.List;

public final class FilterEngine implements Disposable {
	static {
		try {
			registerNatives();
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	private final Disposer disposer;
	protected final long ptr;

	public static enum ContentType {
		OTHER, SCRIPT, IMAGE, STYLESHEET, OBJECT, SUBDOCUMENT, DOCUMENT, XMLHTTPREQUEST, OBJECT_SUBREQUEST, FONT, MEDIA
	}

	public FilterEngine(final JsEngine jsEngine) {
		this.ptr = ctor(jsEngine.ptr);
		this.disposer = new Disposer(this, new DisposeWrapper(this.ptr));
	}

	public boolean isFirstRun() {
		return isFirstRun(this.ptr);
	}

	public Filter getFilter(final String text) {
		return getFilter(this.ptr, text);
	}

	public List<Filter> getListedFilters() {
		return getListedFilters(this.ptr);
	}

	public Subscription getSubscription(final String url) {
		return getSubscription(this.ptr, url);
	}

	public List<Subscription> getListedSubscriptions() {
		return getListedSubscriptions(this.ptr);
	}

	public List<String> getElementHidingSelectors(final String domain) {
		return getElementHidingSelectors(this.ptr, domain);
	}

	public Filter matches(final String url, final ContentType contentType,
			final String documentUrl) {
		return matches(this.ptr, url, contentType, documentUrl);
	}

	public Filter matches(final String url, final ContentType contentType,
			final String[] documentUrls) {
		return matches(this.ptr, url, contentType, documentUrls);
	}

	public JsValue getPref(final String pref) {
		return getPref(this.ptr, pref);
	}

	public void setPref(final String pref, final JsValue value) {
		setPref(this.ptr, pref, value.ptr);
	}

	@Override
	public void dispose() {
		this.disposer.dispose();
	}

	private final static class DisposeWrapper implements Disposable {
		private final long ptr;

		public DisposeWrapper(final long ptr) {
			this.ptr = ptr;
		}

		@Override
		public void dispose() {
			dtor(this.ptr);
		}
	}

	private final static native void registerNatives();

	private final static native long ctor(long jsEnginePtr);

	private final static native boolean isFirstRun(long ptr);

	private final static native Filter getFilter(long ptr, String text);

	private final static native List<Filter> getListedFilters(long ptr);

	private final static native Subscription getSubscription(long ptr,
			String url);

	private final static native List<Subscription> getListedSubscriptions(
			long ptr);

	private final static native List<String> getElementHidingSelectors(
			long ptr, String domain);

	private final static native JsValue getPref(long ptr, String pref);

	private final static native Filter matches(long ptr, String url,
			ContentType contentType, String documentUrl);

	private final static native Filter matches(long ptr, String url,
			ContentType contentType, String[] documentUrls);

	private final static native void setPref(long ptr, String pref,
			long valuePtr);

	private final static native void dtor(long ptr);
}

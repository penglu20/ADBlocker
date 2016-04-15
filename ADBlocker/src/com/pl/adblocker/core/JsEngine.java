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

import java.util.List;

public final class JsEngine implements Disposable {
	private final Disposer disposer;
	protected final long ptr;

	static {
		try {
			registerNatives();
		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public JsEngine(final AppInfo appInfo) {
		this(ctor(appInfo));
	}

	protected JsEngine(final long ptr) {
		this.ptr = ptr;
		this.disposer = new Disposer(this, new DisposeWrapper(ptr));
	}

	public JsValue evaluate(final String source, final String filename) {
		return evaluate(this.ptr, source, filename);
	}

	public JsValue evaluate(final String source) {
		return evaluate(this.ptr, source, "");
	}

	public void triggerEvent(final String eventName, final List<JsValue> params) {
		final long[] args = new long[params.size()];

		for (int i = 0; i < args.length; i++) {
			args[i] = params.get(i).ptr;
		}

		triggerEvent(this.ptr, eventName, args);
	}

	public void triggerEvent(final String eventName) {
		triggerEvent(this.ptr, eventName, null);
	}

	public void setDefaultFileSystem(final String basePath) {
		setDefaultFileSystem(this.ptr, basePath);
	}

	public JsValue newValue(final long value) {
		return newValue(this.ptr, value);
	}

	public JsValue newValue(final boolean value) {
		return newValue(this.ptr, value);
	}

	public JsValue newValue(final String value) {
		return newValue(this.ptr, value);
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

	private final static native long ctor(AppInfo appInfo);

	private final static native JsValue evaluate(long ptr, String source,
			String filename);

	private final static native void triggerEvent(long ptr, String eventName,
			long[] args);

	private final static native void setDefaultFileSystem(long ptr,
			String basePath);

	private final static native JsValue newValue(long ptr, long value);

	private final static native JsValue newValue(long ptr, boolean value);

	private final static native JsValue newValue(long ptr, String value);

	private final static native void dtor(long ptr);
}

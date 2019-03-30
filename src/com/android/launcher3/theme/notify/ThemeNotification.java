package com.android.launcher3.theme.notify;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
//add by zhouerlong prizeTheme add
import android.util.Log;
	//add by zhouerlong prizeTheme add

import com.android.launcher3.IconCache;
import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.theme.tools.ThemeIconTool;

/**
 * 
 * @author liuwei
 * @since 2015-03-26 10:49:16 Thursday
 * @version 1.0.0
 */
public class ThemeNotification {

	public static final String KEY_LQTHEME = "lqtheme";
	public static final String KEY_IS_LQTHEME = "is_lqtheme";
	public static final String KEY_LQTHEME_PATH = "lqtheme_path";
	public static final String RECEIVER_ACTION = "appley_theme_ztefs";
	public static final String RECEIVER_THEME_PATH = "themePath";
	//add by zhouerlong prizeTheme add
	public static final String PRIZELIVESTORE = "PrizeLiveStore/theme";
	//add by zhouerlong prizeTheme add

//add by zhouerlong  lock 
	private  ArrayList<ThemeChangeListener> mThemeChangeListeners = new ArrayList<ThemeChangeListener>();
	Launcher mLauncher =null;
	public ThemeNotification(Launcher launcher) {
		mLauncher = launcher;
	}

//add by zhouerlong  lock 

	/**
	 * 注册主题变化
	 * 
	 * @param target
	 *            监听对象
	 * @param observer
	 *            监听器
	 * @param userObject
	 *            参数对象
	 */
	public void registerThemeChange(Object target,
									ThemeChangeListener observer, Object userObject) {
	//add by zhouerlong prizeTheme add
		synchronized (mThemeChangeListeners) {
			if (observer != null && !mThemeChangeListeners.contains(observer)) {
//add by zhouerlong  lock 
				Log.i("zhouerlong", "mThemeChangeListeners:size  registerThemeChange"+mThemeChangeListeners.size());
//add by zhouerlong  lock 
				mThemeChangeListeners.add(observer);
			}

		}
	}
	//add by zhouerlong prizeTheme add
	
	/*private static ThemeNotification mInstance;
	
	public static ThemeNotification getInstance() {
		if(mInstance==null) {
			mInstance = new ThemeNotification();
		}
		return mInstance;
	}*/
	//add by zhouerlong prizeTheme add
	/**
	 * 解除注册主题变化
	 * 
	 * @param target
	 *            监听对象
	 */
	public void unRegisterThemeChange(ThemeChangeListener observer,
			Object target) {
		synchronized (mThemeChangeListeners) {
			if (observer != null && mThemeChangeListeners.contains(observer)) {
				mThemeChangeListeners.remove(observer);
			}
		}
	}

	public ArrayList<ThemeChangeListener> getmThemeChangeListeners() {
		return mThemeChangeListeners;
	}
	/**
	 * 解除注册主题变化
	 *
	 *            监听对象
	 */
	public void clear() {
		
		synchronized (mThemeChangeListeners) {

			if (mThemeChangeListeners != null) {
				mThemeChangeListeners.clear();
			}
		}
	}

	class NotifyTask extends AsyncTask<Void, ThemeChangeListener, Boolean> {

		private boolean end;

		@Override
		protected Boolean doInBackground(Void... params) {
			//add by zhouerlong
		/*	try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			//add by zhouerlong  
			synchronized (mThemeChangeListeners) {
//add by zhouerlong  lock 
				Log.i("zhouerlong", "mThemeChangeListeners:size处于等待状态");
				Log.i("zhouerlong", "mThemeChangeListeners:size得到锁了 mThemeChangeListeners  继续执行");
				for (int i = 0; i < mThemeChangeListeners.size(); i++) {
					final int s = i;

					end = i == mThemeChangeListeners.size() - 1;

					Log.i("zhouerlong", "mThemeChangeListeners:size  NotifyTask"+mThemeChangeListeners.size());
//add by zhouerlong  lock 
//					publishProgress(mThemeChangeListeners.get(i));
					mThemeChangeListeners.get(i).onThemeChange(end);
				}
			}
			return true;
		}

		@Override
		protected void onProgressUpdate(final ThemeChangeListener... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);

			/*ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					values[0].onThemeChange(end);
				}
			});*/


		/*	Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (mThemeChangeListeners) {
						values[0].onThemeChange(end);
					}
				}
			});
			t.start();*/
			

//			cachedThreadPool.shutdown();

		}

	/*	@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			synchronized (mThemeChangeListeners) {
				mThemeChangeListeners.clear();
			}
			super.onPostExecute(result);
		}*/

	}

//add by zhouerlong  lock 	
	public    void notifys() {
		synchronized (mThemeChangeListeners) {
			mThemeChangeListeners.notify();
		}
	}
	
	public void waits() {
		synchronized (mThemeChangeListeners) {
			try {
			//add by zhouerlong delay  200
				int start = (int) System.currentTimeMillis();
				Thread.sleep(400);
				int end = (int) System.currentTimeMillis();
			//add by zhouerlong delay  200
				mThemeChangeListeners.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
//add by zhouerlong  lock 

	/**
	 * 通知主题发生变化
	 */
	public  void notifyThemeChange(Context c) {
		// final Handler h = new Handler();

		synchronized (mThemeChangeListeners) {
			clear(c);
//			mLauncher.mThemeTool.setWallpaper(c);
		NotifyTask task = new NotifyTask();
		task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	}

	public void clear(Context c) {
//		IconCache iconCache = LauncherAppState.getInstance(c).getIconCache();

		ThemeIconTool.getInstance().mResources=null;
		/*if (iconCache != null) {
			iconCache.clear();

		}*/
	}

	//add by zhouerlong prizeTheme add
}

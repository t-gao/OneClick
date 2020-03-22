package me.tangni.oneclick;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OneClick {

    private static final String TAG = "OneClick";

    private static final long CLICK_INTERVAL = 2000;

    private static Map<View, Map<String, Long>> clickTimeStamps = new HashMap<>();

    private static Set<View> observedSet = new HashSet<>();

    private static View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onViewAttachedToWindow: " + v.toString());
            }
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "onViewDetachedFromWindow: " + v.toString());
            }

            v.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            observedSet.remove(v);
            clickTimeStamps.remove(v);
        }
    };

    /**
     *
     * @return true if this is a fast click
     */
    public static boolean isFastClick(View v, @NonNull String listenerClassName) {
        return isFastClick(v, listenerClassName, CLICK_INTERVAL);
    }

    /**
     *
     * @return true if this is a fast click
     */
    public static boolean isFastClick(View v, @NonNull String listenerClassName, long clickIntervalMillis) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onFastClick, viewId: " + v.getId() + ", view: " + v + ", listenerClassName: " + listenerClassName + ", clickInterval: " + clickIntervalMillis);
        }

        boolean isFastClick = false;
        if (v != null) {
            long lastTime = getLastClickTime(v, listenerClassName);
            long now = SystemClock.elapsedRealtime();
            long diff = now - lastTime;

            if (BuildConfig.DEBUG) {
                Log.d(TAG, "  lastTime: " + lastTime + ", now: " + now + ", diff: " + diff);
            }

            if ( diff < clickIntervalMillis ) {
                isFastClick = true;
            }
        }

        Map<String, Long> listenerMap = clickTimeStamps.get(v);
        if (listenerMap == null) {
            // in most cases, there is no super.onClick(), so set the initial capacity of the map to 1
            listenerMap = new HashMap<>(1);
            clickTimeStamps.put(v, listenerMap);
        }
        listenerMap.put(listenerClassName, SystemClock.elapsedRealtime());
        return isFastClick;
    }

    private static long getLastClickTime(@NonNull View v, @NonNull String listenerClassName) {
        boolean observed = observedSet.contains(v);
        if (!observed) {
            observedSet.add(v);
            v.addOnAttachStateChangeListener(onAttachStateChangeListener);
        }
        Map<String, Long> listenerMap = clickTimeStamps.get(v);
        if (listenerMap == null) {
            return 0;
        }
        Long lastTime = listenerMap.get(listenerClassName);
        return lastTime == null ? 0 : lastTime;
    }
}

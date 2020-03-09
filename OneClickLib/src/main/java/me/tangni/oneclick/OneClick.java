package me.tangni.oneclick;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class OneClick {

    private static final String TAG = "OneClick";

    private static final long CLICK_INTERVAL = 2000;

//    private static Map<View, LifecycleOwner> owners;

    private static Map<View, Long> clickTimeStamps = new HashMap<>();

    private static Set<View> observedSet = new HashSet<>();

    private static View.OnAttachStateChangeListener onAttachStateChangeListener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(View v) {
            Log.d(TAG, "onViewAttachedToWindow: " + v.toString());
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            Log.d(TAG, "onViewDetachedFromWindow: " + v.toString());
            v.removeOnAttachStateChangeListener(onAttachStateChangeListener);
            observedSet.remove(v);
            clickTimeStamps.remove(v);
        }
    };

    /**
     *
     * @return true if this is a fast click
     */
    public static boolean isFastClick(View v) {
        Log.d(TAG, "onFastClick, viewId: " + v.getId() + ", view: " + v);
        boolean isFastClick = false;
        if (v != null) {
            long lastTime = getLastClickTime(v);
            long now = SystemClock.elapsedRealtime();
            long diff = now - lastTime;
            Log.d(TAG, "onFastClick, lastTime: " + lastTime + ", now: " + now + ", diff: " + diff);
            if ( diff < CLICK_INTERVAL ) {
                isFastClick = true;
            }
        }

        clickTimeStamps.put(v, SystemClock.elapsedRealtime());
        return isFastClick;
    }

    private static long getLastClickTime(@NonNull View v) {
        Log.d(TAG, "getLastClickTime");
        boolean observed = observedSet.contains(v);
        if (!observed) {
            observedSet.add(v);
            v.addOnAttachStateChangeListener(onAttachStateChangeListener);
        }

        Long lastTime = clickTimeStamps.get(v);
        return lastTime == null ? 0 : lastTime;
    }

//    private static LifecycleOwner getLifeCycleOwnerOfView() {
//        return null;
//    }
}

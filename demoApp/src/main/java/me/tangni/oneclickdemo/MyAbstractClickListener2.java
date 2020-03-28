package me.tangni.oneclickdemo;

import android.util.Log;
import android.view.View;

public abstract class MyAbstractClickListener2 implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Log.d("OneClick-AbsClick2", "onClick, viewId: " + v.getId() + ", view: " + v);
    }
}

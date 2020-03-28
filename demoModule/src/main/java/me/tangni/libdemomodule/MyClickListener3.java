package me.tangni.libdemomodule;

import android.util.Log;
import android.view.View;

public class MyClickListener3 implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        Log.d("OneClick-MyClickLi3", "onClick, viewId: " + v.getId() + ", view: " + v);
    }
}

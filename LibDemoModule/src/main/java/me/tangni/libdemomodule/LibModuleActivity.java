package me.tangni.libdemomodule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class LibModuleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OneClick-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_module);

        findViewById(R.id.button).setOnClickListener(this);
//        findViewById(R.id.button2).setOnClickListener(new MyAbstractClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "button2 onClick");
//            }
//        });
//        findViewById(R.id.button2).setOnClickListener(new MyClickListener3());
        findViewById(R.id.button2).setOnClickListener(new MyAbstractClickListener2() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "button2 onClick");
                super.onClick(v);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Log.d(TAG, "button onClick");
        }
    }
}

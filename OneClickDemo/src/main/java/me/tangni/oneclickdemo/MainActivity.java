package me.tangni.oneclickdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OneClick-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        switch (v.getId()) {
            case R.id.button:
                Log.d(TAG, "button onClick");
                break;
        }
    }
}

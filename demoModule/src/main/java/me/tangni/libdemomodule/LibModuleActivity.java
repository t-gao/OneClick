package me.tangni.libdemomodule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class LibModuleActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "OneClick-LibModuleAct";
    private TextView clickCount1, clickCount2;
    private int count1, count2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_module);

        clickCount1 = findViewById(R.id.click_count_1);
        clickCount2 = findViewById(R.id.click_count_2);

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
                clickCount2.setText("Click Count:\n\n         " + ++count2);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Log.d(TAG, "button onClick");
            clickCount1.setText("Click Count:\n\n         " + ++count1);
        }
    }
}

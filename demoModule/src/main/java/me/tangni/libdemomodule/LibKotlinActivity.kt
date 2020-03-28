package me.tangni.libdemomodule

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_lib_kotlin.*

class LibKotlinActivity : AppCompatActivity(), View.OnClickListener {
    companion object {
        private const val TAG = "OneClick-LibKotlinAct"
    }

    private var count1 = 0
    private var count2 = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lib_kotlin)
        button.setOnClickListener(this)
        button2.setOnClickListener(object : MyAbstractClickListener2() {
            override fun onClick(v: View?) {
                super.onClick(v)
                Log.d(TAG, "button2 onClick")
                click_count_2.text = "Click Count:\n\n         ${++count2}"
            }
        })
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> {
                Log.d(TAG, "button onClick")
                click_count_1.text = "Click Count:\n\n         ${++count1}"
            }
        }
    }
}

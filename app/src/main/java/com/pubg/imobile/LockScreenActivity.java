package com.pubg.imobile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

public class LockScreenActivity extends AppCompatActivity {

    private View mContentView;
    private Button btnStoreAnswer;
    private TextView txtQuestion;
    private EditText txtAnswer;
    private String mQuestion, mAnswer;
    private JSONObject qa;
    private final SharedPreferences sPref = ParentalApplication.getInstance().getSharedPreferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        makeFullScreen();

        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);




        btnStoreAnswer.setOnClickListener(v -> {

            String answer = txtAnswer.getText().toString();

            if (answer.equalsIgnoreCase(mAnswer)){

                Intent in = new Intent();
                in.setAction(ParentalApplication.LOCK);
                in.putExtra("exclude", sPref.getString("exclude",null));
                sendBroadcast(in);

                finish();
            }
            else{
                txtAnswer.setText("");
                Toast.makeText(getApplicationContext(),
                        "Jawaban salah, Masukkan lagi !", Toast.LENGTH_SHORT).show();

            }
        });

    }



    @Override
    public void onBackPressed() {
    }

    public void makeFullScreen() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.getWindow().getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE);
    }
}
package cn.cooper.support.activitylauncher.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_start_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SecondActivityLauncher.builder(new User("123", "Hallo"))
                        .selected(true)
                        .start(MainActivity.this);
            }
        });
    }
}

package cn.cooper.support.activitylauncher.sample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import cn.cooper.support.activitylauncher.core.ActivityLauncher;

@SuppressLint("Registered")
public class BaseLauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLauncher.fill(this, savedInstanceState);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // optional
        if (intent != null && intent.getExtras() != null) {
            ActivityLauncher.fill(this, intent.getExtras());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // optional
        ActivityLauncher.save(this, outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityLauncher.dispatchResult(this, requestCode, resultCode, data);
    }
}

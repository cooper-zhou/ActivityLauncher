package cn.cooper.support.activitylauncher.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import cn.cooper.support.activitylauncher.annotation.Launcher;
import cn.cooper.support.activitylauncher.annotation.Optional;
import cn.cooper.support.activitylauncher.annotation.Required;

@Launcher(
        flags = {Intent.FLAG_ACTIVITY_CLEAR_TOP},
        pendingTransition = R.anim.anim_slide_right_in,
        pendingTransitionOnFinish = R.anim.anim_slide_left_out
)
public class SecondActivity extends AppCompatActivity {

//    @Required boolean booleanType;
//
//    @Required Boolean booleanType2;

//    @Required boolean booleanType;
//
//    @Required Boolean booleanType2;

//    @Required byte byteType;
//
//    @Required Byte byteType2;
//
//    @Required short shortType;
//
//    @Required Short shortType2;
//
//    @Required int intType;
//
//    @Required Integer intType2;
//
//    @Required long longType;
//
//    @Required Long longType2;
//
//    @Required char charType;
//
//    @Required Character charType2;
//
//    @Required float floatType;
//
//    @Required Float floatType2;
//
//    @Required double doubleType;
//
//    @Required Double doubleType2;

//    @Required String stringType;
//
//    @Required boolean[] booleanArrayType;

//    @Required Boolean[] booleanArrayType2;

//    @Required String[] stringArrayType;
//
//    @Required ArrayList<Integer> intListType;
//
//    @Required ArrayList<String> stringListType;
//
//    @Required SerializableObject serializableObject;
//
//    @Required ParcelObject parcelObject;
//
//    @Required ArrayList<ParcelObject> parcelListObject;
//
//    @Required ParcelObject[] parcelArrayObject;
//
//    @Optional Bundle bundeObejct;

    @Required
    User user;

    @Optional
    int[] index;

    @Optional
    boolean selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SecondActivityLauncher.fill(this, savedInstanceState);
        Log.d("user", user.toString());
        findViewById(R.id.btn_back_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getExtras() != null) {
            SecondActivityLauncher.fill(this, intent.getExtras());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        SecondActivityLauncher.save(this, outState);
    }
}
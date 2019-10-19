package cn.cooper.support.activitylauncher.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import cn.cooper.support.activitylauncher.annotation.Launcher;
import cn.cooper.support.activitylauncher.annotation.Optional;
import cn.cooper.support.activitylauncher.annotation.Required;

@Launcher
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

    @Required User user;

    @Optional int[] index;

    @Optional boolean selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SecondActivityLauncher.fill(this, savedInstanceState);

        Log.d("user", user.toString());
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
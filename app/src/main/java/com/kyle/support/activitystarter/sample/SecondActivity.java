package com.kyle.support.activitystarter.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kyle.support.activitystarter.annotation.Optional;
import com.kyle.support.activitystarter.annotation.Required;
import com.kyle.support.activitystarter.annotation.StarterBuilder;

import java.util.ArrayList;
import java.util.List;

@StarterBuilder
public class SecondActivity extends AppCompatActivity {

    @Required boolean booleanType;

    @Required Boolean booleanType2;

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

    @Required String stringType;

    @Required boolean[] booleanArrayType;

//    @Required Boolean[] booleanArrayType2;

    @Required String[] stringArrayType;

    @Required ArrayList<Integer> intListType;

    @Required ArrayList<String> stringListType;

    @Required SerializableObject serializableObject;

    @Required ParcelObject parcelObject;

    @Required ArrayList<ParcelObject> parcelListObject;

    @Required ParcelObject[] parcelArrayObject;

    @Optional Bundle bundeObejct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SecondActivityStarter.fill(this, savedInstanceState);
    }
}
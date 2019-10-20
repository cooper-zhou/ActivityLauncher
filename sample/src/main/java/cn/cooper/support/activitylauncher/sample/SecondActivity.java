package cn.cooper.support.activitylauncher.sample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cn.cooper.support.activitylauncher.annotation.Launcher;
import cn.cooper.support.activitylauncher.annotation.OnActivityResult;
import cn.cooper.support.activitylauncher.annotation.Optional;
import cn.cooper.support.activitylauncher.annotation.Required;

@Launcher(
        flags = {Intent.FLAG_ACTIVITY_CLEAR_TOP},
        pendingTransition = R.anim.anim_slide_right_in,
        pendingTransitionOnFinish = R.anim.anim_slide_left_out
)
public class SecondActivity extends BaseLauncherActivity {

    @Optional boolean booleanType;

    @Optional Boolean booleanType2;

    @Optional byte byteType;

    @Optional Byte byteType2;

    @Optional short shortType;

    @Optional Short shortType2;

    @Optional int intType;

    @Optional Integer intType2;

    @Optional long longType;

    @Optional Long longType2;

    @Optional char charType;

    @Optional Character charType2;

    @Optional float floatType;

    @Optional Float floatType2;

    @Optional double doubleType;

    @Optional Double doubleType2;

    @Optional String stringType;

    @Optional CharSequence charSequenceType;

    @Optional UserParcelable parcelType;

    @Optional UserSerializable serializableType;

    @Optional Bundle bundleType;



    @Optional boolean[] booleanArrayType;

    @Optional byte[] byteArrayType;

    @Optional short[] shortArrayType;

    @Optional int[] intArrayType;

    @Optional long[] longArrayType;

    @Optional char[] charArrayType;

    @Optional float[] floatArrayType;

    @Optional double[] doubleArrayType;

    @Optional String[] stringArrayType;

    @Optional CharSequence[] charSequenceArrayType;

    @Optional UserParcelable[] parcelArrayObject;



    @Optional ArrayList<Integer> intListType;

    @Optional ArrayList<String> stringListType;

    @Optional ArrayList<CharSequence> charSequenceListType;

    @Optional ArrayList<UserParcelable> parcelListType;

    @Required int optionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Log.d("user", parcelType.getId() + " - " + parcelType.getName());
        findViewById(R.id.btn_back_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @OnActivityResult(requestCode = 1, resultCode = RESULT_OK)
    public void handleTakePhoto(Intent data) {

    }
}
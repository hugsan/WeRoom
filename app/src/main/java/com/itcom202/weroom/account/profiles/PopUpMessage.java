package com.itcom202.weroom.account.profiles;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.itcom202.weroom.R;

public class PopUpMessage {
    private boolean mStartActivity = false;
    private Activity mActivity;
    private Intent mIntent;


    public void showDialog(Activity activity,String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pop_up);

        TextView text =  dialog.findViewById(R.id.textViewDialogMsg);
        text.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.buttonClose);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (mStartActivity){
                    mStartActivity = false;
                    mActivity.startActivity(mIntent);
                }
            }
        });

        dialog.show();

    }
    public void changeToActivityOnClose(Activity activity, Intent intent){
        mStartActivity = true;
        mActivity = activity;
        mIntent = intent;
    }
}


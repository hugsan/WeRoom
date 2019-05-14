package com.itcom202.weroom.framework;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.itcom202.weroom.R;

/**
 * Class that creates a dialog for the users. The dialog can display different message on runtime.
 */
public class PopUpMessage {
    private boolean mStartActivity = false;
    private Activity mActivity;
    private Intent mIntent;


    /**
     * Method that show dialog with a specific message.
     * @param activity context of the application.
     * @param msg String of the message to be displayed to the user.
     */
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


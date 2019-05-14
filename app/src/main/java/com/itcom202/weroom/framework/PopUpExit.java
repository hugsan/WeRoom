package com.itcom202.weroom.framework;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.itcom202.weroom.R;

public class PopUpExit {


    public void showDialog( final Activity activity, String msg ) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.pop_exit);

        TextView text = dialog.findViewById(R.id.textViewDialogMsg);
        text.setText(msg);

        Button dialogButton = dialog.findViewById(R.id.exitButton);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                System.exit(0);
            }
        });
        Button cancelButton = dialog.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

}


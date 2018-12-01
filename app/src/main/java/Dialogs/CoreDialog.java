package Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

public class CoreDialog {

    public CoreDialog(Context context, String msg){
        createDialog(context,msg);
    }

    private void createDialog(Context context,String msg) {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Ads combinations to be shown");
        dialogBuilder.setMessage(msg);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog=dialogBuilder.create();
        dialog.show();
    }


}
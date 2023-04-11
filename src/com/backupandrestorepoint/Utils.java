package com.backupandrestorepoint;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class Utils
{
  @SuppressWarnings("deprecation")
public static void alertDialogShow(Context paramContext, String paramString)
  {
    final AlertDialog localAlertDialog = new AlertDialog.Builder(paramContext).create();
    localAlertDialog.setMessage(paramString);
    localAlertDialog.setTitle(paramContext.getString(R.string.app_name));
    localAlertDialog.setButton(paramContext.getString(R.string.cancel), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        localAlertDialog.dismiss();
      }
    });
    localAlertDialog.show();
  }

  public static long getFreeSpace()
  {
    StatFs localStatFs = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
    return localStatFs.getBlockSize() * localStatFs.getBlockCount() / 1048576L;
  }

  static boolean isSDcardPresent()
  {
    return Environment.getExternalStorageState().equals("mounted");
  }
}

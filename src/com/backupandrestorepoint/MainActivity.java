package com.backupandrestorepoint;

import java.io.File;
import java.util.ArrayList;

import module.constants.AppConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.provider.Telephony;
import android.provider.Telephony.Sms;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.NativeAdPreferences.NativeAdBitmapSize;
import com.startapp.android.publish.nativead.StartAppNativeAd;

public class MainActivity extends Activity
{
  Button btnBookmarksBackup;
  Button btnAppBackup;
  Button btnCallLogsBackup;
  Button btnContactBackup;
  Button btnSMSBackup;
  Button ibSettings;
  LinearLayout llStorageBar;
  TextView topText;
  TextView tvFreeSpace;
  TextView tvUsedSpace;
  View vProgress;

	private StartAppAd startAppAd = new StartAppAd(this);
	
	private StartAppNativeAd startAppNativeAd = new StartAppNativeAd(this);
	private NativeAdDetails nativeAd = null;
	
	private AdEventListener nativeAdListener = new AdEventListener() {
		
		@Override
		public void onReceiveAd(Ad ad) {
			
			// Get the native ad
			ArrayList<NativeAdDetails> nativeAdsList = startAppNativeAd.getNativeAds();
			if (nativeAdsList.size() > 0){
				nativeAd = nativeAdsList.get(0);
			}
			
			// Verify that an ad was retrieved
			if (nativeAd != null){
				
				// When ad is received and displayed - we MUST send impression
				nativeAd.sendImpression(MainActivity.this);
			}
		}
		
		@Override
		public void onFailedToReceiveAd(Ad ad) {
			
			// Error occurred while loading the native ad
			
		}
	};
  public int barWidth(double paramDouble1, double paramDouble2, int paramInt)
  {
    return (int)(100.0D * (paramDouble2 / (paramDouble1 + paramDouble2)) * paramInt / 100.0D);
  }

  public void crateStorateDicretories()
  {
    File localFile1 = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "sms");
    File localFile2 = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "contacts");
    File localFile3 = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "callLogs");
    File localFile4 = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "App");
    File localFile5 = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "bookmarks");
    Log.d("sms path is", localFile1.getAbsolutePath());
    if (!localFile1.exists())
      localFile1.mkdirs();
    if (!localFile2.exists())
      localFile2.mkdirs();
    if (!localFile3.exists())
      localFile3.mkdirs();
    if (!localFile4.exists())
      localFile4.mkdirs();
    if (!localFile5.exists())
      localFile5.mkdirs();
  }

  public double discSpace(String paramString)
  {
	  StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
	  int Free  = (stat.getAvailableBlocks() * stat.getBlockSize()) / 1048576;
	  return Free;
  }
  public double discSpace1(String paramString)
  {
  StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());        
  int Total = (statFs.getBlockCount() * statFs.getBlockSize()) / 1048576;
  return Total;
  }
  public void intitViews()
  {
    this.tvUsedSpace = ((TextView)findViewById(R.id.tvUsedSpace));
    this.tvFreeSpace = ((TextView)findViewById(R.id.tvFreeSpace));
    this.btnSMSBackup = ((Button)findViewById(R.id.btnSMSBackup));
    this.btnBookmarksBackup = ((Button)findViewById(R.id.btnBookmarksBackup));
    this.btnAppBackup  = ((Button)findViewById(R.id.btnappBackup));
    this.btnCallLogsBackup = ((Button)findViewById(R.id.btnCallLogsBackup));
    this.btnContactBackup = ((Button)findViewById(R.id.btnContactsBackup));
    this.llStorageBar = ((LinearLayout)findViewById(R.id.llStorageBar));
    this.ibSettings = ((Button)findViewById(R.id.ibSettings));
    this.topText = ((TextView)findViewById(R.id.toptext));
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "nexalight.otf");
    this.topText.setTypeface(localTypeface);
    
    this.btnSMSBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.this.startActivity(new Intent(MainActivity.this, SmsBackupActivity.class));
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	});
    this.btnBookmarksBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.this.startActivity(new Intent(MainActivity.this, BookMarksActivity.class));
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	});
    this.btnAppBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.this.startActivity(new Intent(MainActivity.this, AppMainActivity.class));
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	});
    this.btnCallLogsBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.this.startActivity(new Intent(MainActivity.this, CallLogsActivity.class));
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	});
    this.btnContactBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			MainActivity.this.startActivity(new Intent(MainActivity.this, ContactsActivity.class));
			startAppAd.showAd();
			startAppAd.loadAd();
		}
	});
    this.tvUsedSpace.setTypeface(localTypeface);
    this.tvFreeSpace.setTypeface(localTypeface);
    this.btnSMSBackup.setTypeface(localTypeface);
    this.btnBookmarksBackup.setTypeface(localTypeface);
    this.btnAppBackup.setTypeface(localTypeface);
    this.btnCallLogsBackup.setTypeface(localTypeface);
    this.btnContactBackup.setTypeface(localTypeface);
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    StartAppSDK.init(this, AppConstants.DEVELOPER_ID, AppConstants.APP_ID, true); //TODO: Replace with your IDs
    setContentView(R.layout.activity_main);
    intitViews();
    double d1 = discSpace(getString(R.string.free));
    double d2 = discSpace1(getString(R.string.used));
    this.llStorageBar.measure(0, 0);
    
    if (Utils.isSDcardPresent())
    {
      this.tvFreeSpace.setText(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.free) + ":</font>" + Math.round(d1 * 100.0D) / 100.0D + " GB"));
      this.tvUsedSpace.setText(Html.fromHtml("<font color='#ffffff'>" + getString(R.string.storage_used) + ":</font>" + Math.round(d2 * 100.0D) / 100.0D + " GB"));
    }
    this.vProgress = findViewById(R.id.vProgress);
    this.llStorageBar.removeAllViews();
    this.llStorageBar.addView(this.vProgress, new ViewGroup.LayoutParams(barWidth(d1, d2, this.llStorageBar.getMeasuredWidth()), 30));
    crateStorateDicretories();
    
    startAppNativeAd.loadAd(
			new NativeAdPreferences()
				.setAdsNumber(1)
				.setAutoBitmapDownload(true)
				.setImageSize(NativeAdBitmapSize.SIZE150X150),
			nativeAdListener);
  }

  	public void onDestroy()
  	{
  		super.onDestroy();
  	}

  	@Override
 	public void onResume() 
 	{
  		super.onResume();
  		startAppAd.onResume();
  		
  		/*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
  		 {
  			startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
  		 }*/
	}

	@Override
	public void onPause() 
	{
		super.onPause();
		startAppAd.onPause();
	}
	
	public void onBackPressed()
	{
		finish();
		super.onBackPressed();
	}
}

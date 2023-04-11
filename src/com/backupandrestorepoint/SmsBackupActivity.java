package com.backupandrestorepoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import module.constants.AppConstants;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.provider.Telephony;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
public class SmsBackupActivity extends Activity
{
  public static final String KEY_ADDRESS = "address";
  public static final String KEY_BODY = "body";
  public static final String KEY_DATE = "date";
  public static final String KEY_ID = "id";
  public static final String KEY_PERSON = "person";
  public static final String KEY_READ = "read";
  public static final String KEY_THRED_ID = "threadId";
  public static final String KEY_TYPE = "type";
  private static final int DEF_SMS_REQ = 0;
  String dPak ;
  Button btnBackup;
  Button btnDeleteAllMessages;
  //Button btnDeleteBackups;
  Button btnRestore;
  Button btnSendToEmail;
  Button btnViewBackups;
  AlertDialog dismiss;
  SMSGettersSetters localSMSGettersSetters;
  Cursor localCursor;
  int i;
  int j;
  private static Context context;
  ProgressDialog pDialog;
  public List<SMSGettersSetters> smsBuffer = new ArrayList();
  TextView tvLastBackup;
  FileWriter write;
  String[] xmlfile;
  Uri localUri;
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
				nativeAd.sendImpression(SmsBackupActivity.this);
			}
		}
		
		@Override
		public void onFailedToReceiveAd(Ad ad) {
			
			// Error occurred while loading the native ad
			
		}
	};
  private void backupSMS() throws IOException
  {
	  /*while(true)
	  {*/
	  this.smsBuffer.clear();
      String str1 = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "sms";
      this.write = new FileWriter(str1 + File.separator + this.xmlfile[0] + ".xml");
      localUri = Uri.parse("content://sms/");
      ContentResolver localContentResolver = getContentResolver();
      String[] arrayOfString1 = new String[8];
      arrayOfString1[0] = "_id";
      arrayOfString1[1] = "thread_id";
      arrayOfString1[2] = "address";
      arrayOfString1[3] = "person";
      arrayOfString1[4] = "date";
      arrayOfString1[5] = "body";
      arrayOfString1[6] = "type";
      arrayOfString1[7] = "read";
      localCursor = localContentResolver.query(localUri, arrayOfString1, null, null, null);
      String[] arrayOfString2 = new String[8];
      arrayOfString2[0] = "_id";
      arrayOfString2[1] = "thread_id";
      arrayOfString2[2] = "address";
      arrayOfString2[3] = "person";
      arrayOfString2[4] = "date";
      arrayOfString2[5] = "body";
      arrayOfString2[6] = "type";
      arrayOfString2[7] = "read";
      localCursor.moveToFirst();
      i = localCursor.getCount();
      this.pDialog.setMax(i);
      
      //Toast.makeText(getApplicationContext(), "I"+i, 400).show();
      this.write.append("<?xml version='1.0' encoding='UTF-8'?>");
      this.write.append('\n');
      this.write.append("<smsall>");
      this.write.append('\n');
         /* while (true)
          {
            this.write.append("</smsall>");
            this.write.flush();
            this.write.close();
          }*/
      while (true)
      {  
          localSMSGettersSetters = new SMSGettersSetters();
          String str2 = localCursor.getString(localCursor.getColumnIndex("_id"));
          String str3 = localCursor.getString(localCursor.getColumnIndex("thread_id"));
          String str4 = localCursor.getString(localCursor.getColumnIndex("address"));
          String str5 = localCursor.getString(localCursor.getColumnIndex("person"));
          String str6 = localCursor.getString(localCursor.getColumnIndex("date"));
          String str7 = localCursor.getString(localCursor.getColumnIndex("body")).replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll(">", "&apos;").replaceAll("'", "&frasl;");
          String str8 = localCursor.getString(localCursor.getColumnIndex("type"));
          String str9 = localCursor.getString(localCursor.getColumnIndex("read"));
          localSMSGettersSetters.setId(str2);
          localSMSGettersSetters.setThreadId(str3);
          localSMSGettersSetters.setAddres(str4);
          localSMSGettersSetters.setPerson(str5);
          localSMSGettersSetters.setDate(str6);
          localSMSGettersSetters.setBody(str7);
          localSMSGettersSetters.setType(str8);
          localSMSGettersSetters.setRead(str9);
          localSMSGettersSetters.setSentDate(str1);
          this.smsBuffer.add(localSMSGettersSetters);
         
         
          Handler localHandler = new Handler(Looper.getMainLooper());
          localHandler.post(new Runnable()
          {
            public void run()
            {
            	final int m = j;
              SmsBackupActivity.this.pDialog.setProgress(1+m);
              i++;
              //Toast.makeText(getApplicationContext(), "J"+j, 400).show();
              int v = -1 + i;
              //Toast.makeText(getApplicationContext(), "Vi"+v, 400).show();
             if (m == v)
              {
            	//Toast.makeText(getApplicationContext(), "M"+m, 400).show();
                Log.d("dismiss", "is called");
                SmsBackupActivity.this.pDialog.dismiss();
                SmsBackupActivity.this.setBackupDate();
                SmsBackupActivity.this.setEmailDialog();
              }
             i--;
             return;
            }
          });
          generateXMLFileForSMS(localSMSGettersSetters);
          localCursor.moveToNext();
          j++;
         
          /*Toast.makeText(getApplicationContext(), "J"+j, 400).show();
    	  Toast.makeText(getApplicationContext(), "I"+i, 400).show();*/
          if(j == i)
          {
        	  this.write.append("</smsall>");
              this.write.flush();
              this.write.close();
              //localCursor.close();
        	  pDialog.dismiss();
        	  return;
          }
         // }
          }
  }

  private void generateXMLFileForSMS(SMSGettersSetters paramSMSGettersSetters)
  {
    try
    {
      this.write.append("<sms>");
      this.write.append('\n');
      this.write.append("<id>" + paramSMSGettersSetters.getId() + "</id>");
      this.write.append('\n');
      this.write.append("<threadId>" + paramSMSGettersSetters.getThreadId() + "</threadId>");
      this.write.append('\n');
      this.write.append("<address>" + paramSMSGettersSetters.getAddress() + "</address>");
      this.write.append('\n');
      this.write.append("<person>" + paramSMSGettersSetters.getPerson() + "</person>");
      this.write.append('\n');
      this.write.append("<date>" + paramSMSGettersSetters.getDate() + "</date>");
      this.write.append('\n');
      this.write.append("<body>" + paramSMSGettersSetters.getBody() + "</body>");
      this.write.append('\n');
      this.write.append("<type>" + paramSMSGettersSetters.getType() + "</type>");
      this.write.append('\n');
      this.write.append("<read>" + paramSMSGettersSetters.getRead() + "</read>");
      this.write.append('\n');
      this.write.append("</sms>");
      this.write.append('\n');
      return;
    }
    catch (NullPointerException localNullPointerException)
    {
      while (true)
        System.out.println("Nullpointer Exception " + localNullPointerException);
    }
    catch (IOException localIOException)
    {
      while (true)
        localIOException.printStackTrace();
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  private void launchComponent(String paramString1, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.MAIN");
    localIntent.addCategory("android.intent.category.LAUNCHER");
    localIntent.setComponent(new ComponentName(paramString1, paramString2));
    localIntent.setFlags(268435456);
    startActivity(localIntent);
  }

  public void BackupAlert()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
    View localView = getLayoutInflater().inflate(R.layout.layout_backup_dialog, null);
    final EditText localEditText = (EditText)localView.findViewById(R.id.etFileName);
    CharSequence localCharSequence = DateFormat.format("yyMMddhhmmss", new Date().getTime());
    localEditText.setText("sms_" + localCharSequence + ".xml");
    localBuilder.setView(localView);
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        SmsBackupActivity.this.xmlfile = localEditText.getText().toString().trim().split(".xml");
        SmsBackupActivity.this.setProgressDialog();
        new Thread(new Runnable()
        {
          public void run()
          {
            try {
            	Looper.prepare();
				SmsBackupActivity.this.backupSMS();
				Looper.loop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
          }
        }).start();
      }
    });
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    localAlertDialog.show();
  }

  public void deleteAllSMS()
  {
    Uri localUri = Uri.parse("content://sms/");
    Cursor localCursor = getContentResolver().query(localUri, null, null, null, null);
    while (true)
    {
      if (!localCursor.moveToNext())
        return;
      try
      {
        localCursor.getString(0);
        getContentResolver().delete(Uri.parse("content://sms/"), null, null);
      }
      catch (Exception localException)
      {
      }
    }
  }

  public void deleteAllSMSDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setMessage("Are you sure you wan to delete all the SMS on the phone?");
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        SmsBackupActivity.this.panicDialog();
      }
    });
    localBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
      }
    });
    localBuilder.show();
  }

  public String getBackupDate(String paramString)
  {
    return getSharedPreferences("BackupPrefs", 0).getString(paramString, getString(R.string.never_backup));
  }

  public int getSMSCount()
  {
    Cursor localCursor = getContentResolver().query(Uri.parse("content://sms/"), null, null, null, null);
    String[] arrayOfString = localCursor.getColumnNames();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfString.length)
        return localCursor.getCount();
      Log.d("Names are", arrayOfString[i]);
    }
  }

  public List<FileGetterSetters> getSMSFiles()
  {
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "sms").listFiles();
    int i = arrayOfFile.length;
    for (int j = 0; ; j++)
    {
      if (j >= i)
        return localArrayList;
      File localFile = arrayOfFile[j];
      FileGetterSetters localFileGetterSetters = new FileGetterSetters();
      Log.d("file Name is", localFile.getName());
      localFileGetterSetters.setFileName(localFile.getName());
      Date localDate = new Date(localFile.lastModified());
      Log.d("Modified date is", localDate.toString());
      localFileGetterSetters.setDateCreated(localDate.toString());
      localArrayList.add(localFileGetterSetters);
    }
  }

  public String getXML(String paramString)
  {
    Log.d("File path is", paramString);
    File localFile = new File(paramString);
    StringBuilder localStringBuilder = new StringBuilder();
    try
    {
      BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(localFile)));
      while (true)
      {
        String str = localBufferedReader.readLine();
        if (str == null)
           return localStringBuilder.toString();
        localStringBuilder.append(str);
        localStringBuilder.append('\n');
      }
    }
    catch (IOException localIOException)
    {
     
    }
	return paramString;
  }

  public void initAllViews()
  {
	this.btnBackup = ((Button)findViewById(R.id.btnBackup));
	this.btnRestore = ((Button)findViewById(R.id.btnRestore));
	this.btnViewBackups = ((Button)findViewById(R.id.btnViewBackup));
	this.btnSendToEmail = ((Button)findViewById(R.id.btnSendToEmail));
    //this.btnDeleteBackups = ((Button)findViewById(2131361817));
    this.btnDeleteAllMessages = ((Button)findViewById(R.id.btnDeleteAllMessages));
    TextView localTextView1 = (TextView)findViewById(R.id.tvSMS);
    this.tvLastBackup = ((TextView)findViewById(R.id.tvLastBackup));
    TextView localTextView2 = (TextView)findViewById(R.id.tvTitle);
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "nexalight.otf");
    btnBackup.setTypeface(localTypeface);
    btnRestore.setTypeface(localTypeface);
    btnViewBackups.setTypeface(localTypeface);
    btnSendToEmail.setTypeface(localTypeface);
    btnDeleteAllMessages.setTypeface(localTypeface);
    //this.btnDeleteBackups.setTypeface(localTypeface);
    localTextView2.setTypeface(localTypeface);
    localTextView1.setTypeface(localTypeface);
    tvLastBackup.setTypeface(localTypeface);
    btnBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (getSMSCount() > 0)
	    	  {
	    			 SmsBackupActivity.this.BackupAlert();
	    	  }
	    	  else
	    	  {
	    		  	 Toast.makeText(getApplicationContext(), "No Backup Found", Toast.LENGTH_SHORT).show();
	    	  }
	    		 
		}
	});
    btnRestore.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
			{
				final String myPackageName = getPackageName();
	        	if (!Telephony.Sms.getDefaultSmsPackage(getApplicationContext()).equals(myPackageName)) 
	        	{
	              Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
	              intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,myPackageName);
	              startActivityForResult(intent, DEF_SMS_REQ);
	        	} 
	        	else
	        	{
	        		Toast.makeText(getApplicationContext(), "Set Your Default Messanger App", Toast.LENGTH_SHORT).show();
	        		startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
	        	}
			}
			else
			{
				SmsBackupActivity.this.restoreBackupFilesDialog(true);
			}
    
		}
	});
    btnViewBackups.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SmsBackupActivity.this.showBackupFilesDialog(false);
		}
	});
    btnSendToEmail.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SmsBackupActivity.this.showBackupFilesDialog(true);
		}
	});
    //this.btnDeleteBackups.setOnClickListener(this.myClick);
    btnDeleteAllMessages.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			SmsBackupActivity.this.deleteAllSMSDialog();
		}
	});
    localTextView1.setText(Html.fromHtml("<font color='#FFFFFF'>SMS:</font>" + getSMSCount()));
    this.tvLastBackup.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.last_backup) + ":</font>" + getBackupDate("smsBackupDate")));
  }

  public void okDialog()
  {
    AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
    localAlertDialog.setTitle(getString(R.string.app_name));
    localAlertDialog.setMessage(getString(R.string.deleted_successfully_));
    localAlertDialog.setButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
      }
    });
    localAlertDialog.show();
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    StartAppSDK.init(this, AppConstants.DEVELOPER_ID, AppConstants.APP_ID, true);
    setContentView(R.layout.layout_sms_backupt);
    SmsBackupActivity.context = getApplicationContext();
    //Toast.makeText(getApplicationContext(), ""+, 100).show();
    initAllViews();
    startAppNativeAd.loadAd(
			new NativeAdPreferences()
				.setAdsNumber(1)
				.setAutoBitmapDownload(true)
				.setImageSize(NativeAdBitmapSize.SIZE150X150),
			nativeAdListener);
  }
 
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      switch (requestCode) {
          case DEF_SMS_REQ:
              if (Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.KITKAT && isDefaultSmsApp(this) ||
                      Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && resultCode == Activity.RESULT_OK) {
            	      SmsBackupActivity.this.restoreBackupFilesDialog(true);
              }
              break;
      }
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static boolean isDefaultSmsApp(Context context) {
      return context.getPackageName().equals(Telephony.Sms.getDefaultSmsPackage(context));
  }
  public void openInbox()
  {
    try
    {
      Intent localIntent = new Intent("android.intent.action.MAIN");
      localIntent.addCategory("android.intent.category.LAUNCHER");
      localIntent.addFlags(65536);
      Iterator localIterator = getPackageManager().queryIntentActivities(localIntent, 0).iterator();
      while (localIterator.hasNext())
      {
        ResolveInfo localResolveInfo = (ResolveInfo)localIterator.next();
        if (localResolveInfo.activityInfo.packageName.equalsIgnoreCase("com.android.mms"))
          launchComponent(localResolveInfo.activityInfo.packageName, localResolveInfo.activityInfo.name);
      }
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      Toast.makeText(getApplicationContext(), getString(R.string.there_was_a_problem_loading_the_application_) + "com.android.mms", 0).show();
    }
  }

  public void panicDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setMessage("Are you sure you wan to delete all the SMS on the phone?");
    localBuilder.setPositiveButton("Sure", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        new SmsBackupActivity.DeletingSMSTask(SmsBackupActivity.this).execute(new Void[0]);
      }
    });
    localBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
      }
    });
    localBuilder.show();
  }

public void restoreBackup(HashMap<String, String> paramHashMap)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("address", (String)paramHashMap.get("address"));
    localContentValues.put("person", (String)paramHashMap.get("person"));
    localContentValues.put("date", (String)paramHashMap.get("date"));
    localContentValues.put("body", (String)paramHashMap.get("body"));
    localContentValues.put("_id", (String)paramHashMap.get("id"));
    localContentValues.put("type", (String)paramHashMap.get("type"));
    localContentValues.put("read", (String)paramHashMap.get("read"));
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
    {
    	getContentResolver().insert(Telephony.Sms.CONTENT_URI, localContentValues);
    }
    else 
    {
    	getContentResolver().insert(Uri.parse("content://sms/"), localContentValues);
    }
    //getContentResolver().insert(Uri.parse("content://sms/"), localContentValues); //""
  }

  public void restoreBackupFilesDialog(final boolean paramBoolean)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.backup_files);
    ListView localListView = new ListView(this);
    final List localList = getSMSFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        SmsBackupActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "sms" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          Log.d("Seleted file path is", str);
          SmsBackupActivity.RestoringTask localRestoringTask = new SmsBackupActivity.RestoringTask(SmsBackupActivity.this);
          String[] arrayOfString = new String[1];
          arrayOfString[0] = str;
          localRestoringTask.execute(arrayOfString);
        }
      }
    });
    localBuilder.setView(localListView);
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().alpha = 0.6F;
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.FileDialogAnimation;
    this.dismiss = localAlertDialog;
    localAlertDialog.show();
  }

  public void sendFileToEmail(String paramString)
  {
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.setType("message/rfc822");
    localIntent.putExtra("android.intent.extra.EMAIL", new String[0]);
    localIntent.putExtra("android.intent.extra.SUBJECT", "");
    localIntent.putExtra("android.intent.extra.STREAM", Uri.parse("file://" + paramString));
    localIntent.putExtra("android.intent.extra.TEXT", "");
    startActivity(Intent.createChooser(localIntent, "Send mail..."));
  }

  public void setBackupDate()
  {
    CharSequence localCharSequence = DateFormat.format("yy/MM/dd hh:mm:ss", new Date().getTime());
    this.tvLastBackup.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.last_backup) + ":</font>" + localCharSequence.toString()));
    setLastBackupDate("smsBackupDate", localCharSequence.toString());
  }

  public void setEmailDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setCancelable(false);
    localBuilder.setMessage(getString(R.string.send_to_email_backup) + "?");
    localBuilder.setNegativeButton("Back", new OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			// TODO Auto-generated method stub
			
			Intent inn = new Intent(getApplicationContext(),MainActivity.class);
			inn.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(inn);
		}
	});
    localBuilder.create().show();
  }

  public void setLastBackupDate(String paramString1, String paramString2)
  {
    SharedPreferences.Editor localEditor = getSharedPreferences("BackupPrefs", 0).edit();
    localEditor.putString(paramString1, paramString2);
    localEditor.commit();
  }

  public void setProgressDialog()
  {
    this.pDialog = new ProgressDialog(this);
    this.pDialog.setMessage(getString(R.string.backuping_files_please_wait_));
    this.pDialog.setIndeterminate(false);
    this.pDialog.setProgressDrawable(getResources().getDrawable(R.drawable.greenprogress));
    this.pDialog.setMax(100);
    this.pDialog.setProgressStyle(1);
    this.pDialog.setCancelable(true);
    this.pDialog.show();
  }

  public void showBackupFilesDialog(final boolean paramBoolean)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.backup_files));
    ListView localListView = new ListView(this);
    final List localList = getSMSFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        SmsBackupActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "sms" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          SmsBackupActivity.this.sendFileToEmail(str);
        }
      }
    });
    localBuilder.setView(localListView);
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().alpha = 0.6F;
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.FileDialogAnimation;
    this.dismiss = localAlertDialog;
    localAlertDialog.show();
  }

  public void viewMessagesDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
    
    localBuilder.setMessage(getString(R.string.restore_completed_successfully_));
    localBuilder.setPositiveButton(getString(R.string.view_messages), new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        SmsBackupActivity.this.openInbox();
      }
    });
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    localAlertDialog.show();
  }

  public class DeletingSMSTask extends AsyncTask<Void, Void, Void>
  {
    public ProgressDialog pd = new ProgressDialog(SmsBackupActivity.this);

    public DeletingSMSTask(SmsBackupActivity smsBackupActivity)
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      SmsBackupActivity.this.deleteAllSMS();
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      this.pd.dismiss();
      ((TextView)SmsBackupActivity.this.findViewById(R.id.tvSMS)).setText(Html.fromHtml("<font color='#FFFFFF'>SMS:</font>" + SmsBackupActivity.this.getSMSCount()));
      Toast.makeText(SmsBackupActivity.this.getApplicationContext(), "SMS Deleted Successfully!", 1).show();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setMessage(SmsBackupActivity.this.getString(R.string.deleting_all_sms_));
      this.pd.show();
    }
  }

  public class RestoringTask extends AsyncTask<String, Integer, String>
  {
	  
    ProgressDialog pd = new ProgressDialog(SmsBackupActivity.this);
    Integer[] arrayOfInteger;
   public RestoringTask(SmsBackupActivity smsBackupActivity)
    {
    }

	@SuppressWarnings("unchecked")
	protected String doInBackground(String[] paramArrayOfString)
    {
      String str = SmsBackupActivity.this.getXML(paramArrayOfString[0]);
      XMLParser localXMLParser = new XMLParser();
      
      NodeList localNodeList = localXMLParser.getDomElement(str).getElementsByTagName("sms");
      int i = localNodeList.getLength();
      for (int j = 0; ; j++)
      {
        if (j >= i)
          return "";
		@SuppressWarnings("rawtypes")
		HashMap localHashMap = new HashMap();
        Element localElement = (Element)localNodeList.item(j);
        localHashMap.put("id", localXMLParser.getValue(localElement, "id"));
        localHashMap.put("threadId", localXMLParser.getValue(localElement, "threadId"));
        localHashMap.put("address", localXMLParser.getValue(localElement, "address"));
        localHashMap.put("person", localXMLParser.getValue(localElement, "person"));
        localHashMap.put("date", localXMLParser.getValue(localElement, "date"));
        localHashMap.put("body", localXMLParser.getValue(localElement, "body"));
        localHashMap.put("type", localXMLParser.getValue(localElement, "type"));
        localHashMap.put("read", localXMLParser.getValue(localElement, "read"));
        
        SmsBackupActivity.this.restoreBackup(localHashMap);
        Integer[] arrayOfInteger = new Integer[2];
        arrayOfInteger[0] = Integer.valueOf(i);
        arrayOfInteger[1] = Integer.valueOf(j);
        publishProgress(arrayOfInteger);
    }
      
    }
    @SuppressLint("InlinedApi")
	protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      ((TextView)SmsBackupActivity.this.findViewById(R.id.tvSMS)).setText(Html.fromHtml("<font color='#FFFFFF'>SMS:</font>" + SmsBackupActivity.this.getSMSCount()));
      this.pd.dismiss();
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT)
	  {
    	  Toast.makeText(getApplicationContext(), "Restore Succesfully", Toast.LENGTH_SHORT).show();
    	  Toast.makeText(getApplicationContext(), "Set Your Default Messanger App", Toast.LENGTH_SHORT).show();
    	  startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), 0);
	  }else
	  {
		  SmsBackupActivity.this.viewMessagesDialog();
	  }
      
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setProgressStyle(1);
      this.pd.setMessage(SmsBackupActivity.this.getString(R.string.restoring_backups_));
      this.pd.setProgressDrawable(SmsBackupActivity.this.getResources().getDrawable(R.drawable.greenprogress));
      this.pd.show();
    }

    protected void onProgressUpdate(Integer[] paramArrayOfInteger)
    {
      this.pd.setMax(paramArrayOfInteger[0].intValue());
      this.pd.setProgress(paramArrayOfInteger[1].intValue());
      super.onProgressUpdate(paramArrayOfInteger);
    }
  }
}

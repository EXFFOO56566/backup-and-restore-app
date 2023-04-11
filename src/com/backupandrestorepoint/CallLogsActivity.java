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
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.CallLog;
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

public class CallLogsActivity extends Activity
{
  public static final String KEY_DATE = "date";
  public static final String KEY_DURATION = "duration";
  public static final String KEY_ID = "id";
  public static final String KEY_NEW = "new";
  public static final String KEY_NUMBER = "number";
  public static final String KEY_Name = "name";
  public static final String KEY_TYPE = "type";
  Button btnBackup;
  Button btnDeleteAllCallLogs;
  //Button btnDeleteBackups;
  Button btnRestore;
  Button btnSendToEmail;
  Button btnViewBackups;
  AlertDialog dismiss;
  
  Cursor localCursor;
  int i;
  int j;
  CallLogsGettersSetters localCallLogsGettersSetters;
 
  ProgressDialog pDialog;
  public List<SMSGettersSetters> smsBuffer = new ArrayList();
  TextView tvLastBackup;
  FileWriter write;
  String[] xmlfile;

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
				nativeAd.sendImpression(CallLogsActivity.this);
			}
		}
		
		@Override
		public void onFailedToReceiveAd(Ad ad) {
			
			// Error occurred while loading the native ad
			
		}
	};
  private void backupCallLogs() throws IOException
  {
    smsBuffer.clear();
    String str1 = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "callLogs";
	/*try
    {*/
      write = new FileWriter(str1 + File.separator + this.xmlfile[0] + ".xml");
      //Uri localUri = Uri.parse("content://call_log/calls");
      Uri localUri = CallLog.Calls.CONTENT_URI;
      ContentResolver localContentResolver = getContentResolver();
      String[] arrayOfString1 = new String[7];
      arrayOfString1[0] = "_id";
      arrayOfString1[1] = "number";
      arrayOfString1[2] = "date";
      arrayOfString1[3] = "type";
      arrayOfString1[4] = "name";
      arrayOfString1[5] = "new";
      arrayOfString1[6] = "duration";
      localCursor = localContentResolver.query(localUri, arrayOfString1, null, null, null);
      String[] arrayOfString2 = new String[7];
      arrayOfString2[0] = "_id";
      arrayOfString2[1] = "number";
      arrayOfString2[2] = "date";
      arrayOfString2[3] = "type";
      arrayOfString2[4] = "name";
      arrayOfString2[5] = "new";
      arrayOfString2[6] = "duration";
      localCursor.moveToFirst();
      i = localCursor.getCount();
      this.pDialog.setMax(i);
      
      this.write.append("<?xml version='1.0' encoding='UTF-8'?>");
      this.write.append('\n');
      this.write.append("<alllogs>");
      this.write.append('\n');
    /*}
    catch (IOException localIOException2)
    {*/
      /*if (j >= this.localCursor.getCount())
      {
    	  Toast.makeText(getApplicationContext(), "GUd", 400).show();
    	  write.close();
       localCursor.close();
      }*/
      while(true)
      { 
    	localCallLogsGettersSetters = new CallLogsGettersSetters();
          String str2 = localCursor.getString(localCursor.getColumnIndex("_id"));
          String str3 = localCursor.getString(localCursor.getColumnIndex("number"));
          String str4 = localCursor.getString(localCursor.getColumnIndex("name"));
          String str5 = localCursor.getString(localCursor.getColumnIndex("date"));
          String str6 = localCursor.getString(localCursor.getColumnIndex("type"));
          String str7 = localCursor.getString(localCursor.getColumnIndex("new"));
          String str8 = localCursor.getString(localCursor.getColumnIndex("duration"));
          localCallLogsGettersSetters.setId(str2);
          localCallLogsGettersSetters.setNumber(str3);
          localCallLogsGettersSetters.setName(str4);
          localCallLogsGettersSetters.setDate(str5);
          localCallLogsGettersSetters.setType(str6);
          localCallLogsGettersSetters.setNew(str7);
          localCallLogsGettersSetters.setDuration(str8);
          
          
          Handler localHandler = new Handler(Looper.getMainLooper());
          localHandler.post(new Runnable()
          {
            public void run()
            {
            	final int m = j;
              CallLogsActivity.this.pDialog.setProgress(1+m);
              //Log.d("value is", m);
              i++;
              //Toast.makeText(getApplicationContext(), "I0"+i, 400).show();
              //Toast.makeText(getApplicationContext(), "J"+j, 400).show();
              int v = -1 + i;
              //Toast.makeText(getApplicationContext(), "Vi"+v, 400).show();
             if (m == v)
              {
                Log.d("dismiss", "is called");
                CallLogsActivity.this.pDialog.dismiss();
                CallLogsActivity.this.setBackupDate();
                CallLogsActivity.this.setEmailDialog();
                //Toast.makeText(getApplicationContext(), "I1"+i, 400).show();
              }
             i--;
             return;
            }
          });
          generateXMLFileForCallLogs(localCallLogsGettersSetters);
          localCursor.moveToNext();
          j++;
          
          if (j == i)
          {
        	  this.write.append("</alllogs>");
              this.write.flush();
              this.write.close();
        	  pDialog.dismiss();
              return;
          }
      }
   
    }
  

  private void generateXMLFileForCallLogs(CallLogsGettersSetters paramCallLogsGettersSetters)
  {
    try
    {
      this.write.append("<logs>");
      this.write.append('\n');
      this.write.append("<id>" + paramCallLogsGettersSetters.getId() + "</id>");
      this.write.append('\n');
      this.write.append("<number>" + paramCallLogsGettersSetters.getNumber() + "</number>");
      this.write.append('\n');
      this.write.append("<name>" + paramCallLogsGettersSetters.getName() + "</name>");
      this.write.append('\n');
      this.write.append("<date>" + paramCallLogsGettersSetters.getDate() + "</date>");
      this.write.append('\n');
      this.write.append("<type>" + paramCallLogsGettersSetters.getType() + "</type>");
      this.write.append('\n');
      this.write.append("<new>" + paramCallLogsGettersSetters.getNew() + "</new>");
      this.write.append('\n');
      this.write.append("<duration>" + paramCallLogsGettersSetters.getDuration() + "</duration>");
      this.write.append('\n');
      this.write.append("</logs>");
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
    localBuilder.setTitle(R.string.app_name);
    View localView = getLayoutInflater().inflate(R.layout.layout_backup_dialog, null);
    final EditText localEditText = (EditText)localView.findViewById(R.id.etFileName);
    ((TextView)localView.findViewById(R.id.tvBackupLocation)).setText("storage/sdcard0/smsContactsBackups/callLogs");
    CharSequence localCharSequence = DateFormat.format("yyMMddhhmmss", new Date().getTime());
    localEditText.setText("call_logs_" + localCharSequence + ".xml");
    localBuilder.setView(localView);
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        CallLogsActivity.this.xmlfile = localEditText.getText().toString().trim().split(".xml");
        CallLogsActivity.this.setProgressDialog();
        new Thread(new Runnable()
        {
          public void run()
          {
            
            try {
            	Looper.prepare();
				CallLogsActivity.this.backupCallLogs();
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

  public void deleteAllCallLogs()
  {
    Uri localUri = Uri.parse("content://call_log/calls/");
    Cursor localCursor = getContentResolver().query(localUri, null, null, null, null);
    while (true)
    {
      if (!localCursor.moveToNext())
        return;
      try
      {
        localCursor.getString(0);
        getContentResolver().delete(Uri.parse("content://call_log/calls/"), null, null);
      }
      catch (Exception localException)
      {
      }
    }
  }

  public void deleteAllCallLogsDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
    localBuilder.setMessage(getString(R.string.are_you_sure_you_wan_to_delete_all_the_call_logs_on_the_phone_));
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        CallLogsActivity.this.panicDialog();
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

  public int getCallLogsCount()
  {
    Cursor localCursor = getContentResolver().query(Uri.parse("content://call_log/calls"), null, null, null, null);
    String[] arrayOfString = localCursor.getColumnNames();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfString.length)
        return localCursor.getCount();
      Log.d("Call Logs", arrayOfString[i]);
    }
  }

  public List<FileGetterSetters> getCallLogsFiles()
  {
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "callLogs").listFiles();
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
    this.btnDeleteAllCallLogs = ((Button)findViewById(R.id.btnDeleteAllCallLogs));
    TextView localTextView1 = (TextView)findViewById(R.id.tvCallLogs);
    this.tvLastBackup = ((TextView)findViewById(R.id.tvLastBackup));
    TextView localTextView2 = (TextView)findViewById(R.id.tvTitle);
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "nexalight.otf");
    this.btnBackup.setTypeface(localTypeface);
    this.btnRestore.setTypeface(localTypeface);
    this.btnViewBackups.setTypeface(localTypeface);
    this.btnSendToEmail.setTypeface(localTypeface);
    this.btnDeleteAllCallLogs.setTypeface(localTypeface);
    localTextView2.setTypeface(localTypeface);
    localTextView1.setTypeface(localTypeface);
    this.tvLastBackup.setTypeface(localTypeface);
    this.btnBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(getCallLogsCount() > 0)
			{
	    	  CallLogsActivity.this.BackupAlert();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Call Log Found", Toast.LENGTH_SHORT).show();
			}
		}
	});
    this.btnRestore.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CallLogsActivity.this.restoreBackupFilesDialog(true);
		}
	});
    this.btnViewBackups.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CallLogsActivity.this.showBackupFilesDialog(false);
		}
	});
    this.btnSendToEmail.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 CallLogsActivity.this.showBackupFilesDialog(true);
		}
	});
    //this.btnDeleteBackups.setOnClickListener(this.myClick);
    this.btnDeleteAllCallLogs.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			CallLogsActivity.this.deleteAllCallLogsDialog();
		}
	});
    localTextView1.setText(Html.fromHtml("<font color='#FFFFFF'>Call Logs:</font>" + getCallLogsCount()));
    this.tvLastBackup.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.last_backup) + ":</font>" + getBackupDate("logsBackupDate")));
  }

  public void okDialog()
  {
    AlertDialog localAlertDialog = new AlertDialog.Builder(this).create();
    localAlertDialog.setTitle(getString(R.string.app_name));
    localAlertDialog.setMessage(getString(R.string.deleted_successfully_));
    localAlertDialog.setButton(getString(R.string.OK), new DialogInterface.OnClickListener()
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
    setContentView(R.layout.layout_calllogs_backup);
    initAllViews();
    startAppNativeAd.loadAd(
			new NativeAdPreferences()
				.setAdsNumber(1)
				.setAutoBitmapDownload(true)
				.setImageSize(NativeAdBitmapSize.SIZE150X150),
			nativeAdListener);
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
  
 /* public void onDestroy() {
	   super.onDestroy();
	   if (localCursor != null) {
	      localCursor.close();
	   }
 }*/
  public void panicDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setIcon(R.drawable.icon_warning);
    localBuilder.setMessage(R.string.are_you_sure_you_wan_to_delete_all_the_call_logs_on_the_phone_);
    localBuilder.setPositiveButton("Sure", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        new DeletingCallLogsTask().execute(new Void[0]);
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
    Log.d("getted id is", ""+(String)paramHashMap.get("id"));
    Log.d("id", ""+(String)paramHashMap.get("name"));
    /*Log.d("number", ""+);
    Log.d("name", );
    Log.d("date", );
    //Log.d("type", localXMLParser.getValue(localElement, "type"));
    Log.d("new", );
    Log.d("duration", );*/
    localContentValues.put("_id", (String)paramHashMap.get("id"));
    localContentValues.put("name", (String)paramHashMap.get("name"));
    localContentValues.put("number", (String)paramHashMap.get("number"));
    localContentValues.put("date", (String)paramHashMap.get("date"));
    localContentValues.put("type", (String)paramHashMap.get("type"));
    localContentValues.put("new", (String)paramHashMap.get("new"));
    localContentValues.put("duration", (String)paramHashMap.get("duration"));
    getContentResolver().insert(CallLog.Calls.CONTENT_URI, localContentValues);
  }

public void restoreBackupFilesDialog(final boolean paramBoolean)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.backup_files);
    ListView localListView = new ListView(this);
    final List localList = getCallLogsFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        CallLogsActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "callLogs" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          Log.d("Seleted file path is", str);
          CallLogsActivity.RestoringTask localRestoringTask = new CallLogsActivity.RestoringTask(CallLogsActivity.this);
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
    setLastBackupDate("logsBackupDate", localCharSequence.toString());
  }

  public void setEmailDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setCancelable(false);
    localBuilder.setMessage(getString(R.string.send_to_email_backup) + "?");
    localBuilder.setPositiveButton("Back", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
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
    localBuilder.setTitle(R.string.backup_files);
    ListView localListView = new ListView(this);
    final List localList = getCallLogsFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        CallLogsActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "callLogs" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          CallLogsActivity.this.sendFileToEmail(str);
        }
      }
    });
    localBuilder.setView(localListView);
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().alpha = 0.6F;
    localAlertDialog.getWindow().getAttributes().windowAnimations = 2131230724;
    this.dismiss = localAlertDialog;
    localAlertDialog.show();
  }

  public void viewCallLogsDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setMessage(getString(R.string.restore_completed_successfully_));
    localBuilder.setPositiveButton("View Call Logs", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("content://call_log/calls"));
        CallLogsActivity.this.startActivity(localIntent);
      }
    });
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    localAlertDialog.show();
  }

  public class DeletingCallLogsTask extends AsyncTask<Void, Void, Void>
  {
    public ProgressDialog pd = new ProgressDialog(CallLogsActivity.this);

    public DeletingCallLogsTask()
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      CallLogsActivity.this.deleteAllCallLogs();
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      this.pd.dismiss();
      ((TextView)CallLogsActivity.this.findViewById(R.id.tvCallLogs)).setText(Html.fromHtml("<font color='#FFFFFF'>Call Logs:</font>" + CallLogsActivity.this.getCallLogsCount()));
      Toast.makeText(CallLogsActivity.this.getApplicationContext(), CallLogsActivity.this.getString(R.string.call_logs_deleted_successfully_), 1).show();
    }

    @SuppressLint("NewApi")
	protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setMessage(CallLogsActivity.this.getString(R.string.deleting_all_call_logs_));
      this.pd.show();
    }
  }

  public class RestoringTask extends AsyncTask<String, Integer, String>
  {
    ProgressDialog pd = new ProgressDialog(CallLogsActivity.this);

    public RestoringTask(CallLogsActivity callLogsActivity)
    {
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected String doInBackground(String[] paramArrayOfString)
    {
      String str = CallLogsActivity.this.getXML(paramArrayOfString[0]);
      XMLParser localXMLParser = new XMLParser();
      NodeList localNodeList = localXMLParser.getDomElement(str).getElementsByTagName("logs");
      
      int i = localNodeList.getLength();
      Log.d("total length is", "");
      for (int j = 0; ; j++)
      {
        if (j >= i)
          return "";
        HashMap localHashMap = new HashMap();
        Element localElement = (Element)localNodeList.item(j);
        localHashMap.put("id", localXMLParser.getValue(localElement, "id"));
        localHashMap.put("number", localXMLParser.getValue(localElement, "number"));
        localHashMap.put("name", localXMLParser.getValue(localElement, "name"));
        localHashMap.put("date", localXMLParser.getValue(localElement, "date"));
        localHashMap.put("type", localXMLParser.getValue(localElement, "type"));
        localHashMap.put("new", localXMLParser.getValue(localElement, "new"));
        localHashMap.put("duration", localXMLParser.getValue(localElement, "duration"));
        
        CallLogsActivity.this.restoreBackup(localHashMap);
        Integer[] arrayOfInteger = new Integer[2];
        arrayOfInteger[0] = Integer.valueOf(i);
        arrayOfInteger[1] = Integer.valueOf(j);
        publishProgress(arrayOfInteger);
      }
	
    }

    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      ((TextView)CallLogsActivity.this.findViewById(R.id.tvCallLogs)).setText(Html.fromHtml("<font color='#FFFFFF'>Call Logs:</font>" + CallLogsActivity.this.getCallLogsCount()));
      this.pd.dismiss();
      CallLogsActivity.this.viewCallLogsDialog();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setProgressStyle(1);
      this.pd.setMessage(CallLogsActivity.this.getString(R.string.restoring_backups_));
      this.pd.setProgressDrawable(CallLogsActivity.this.getResources().getDrawable(R.drawable.greenprogress));
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


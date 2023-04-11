package com.backupandrestorepoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import module.constants.AppConstants;
import a_vcard.android.syncml.pim.PropertyNode;
import a_vcard.android.syncml.pim.VDataBuilder;
import a_vcard.android.syncml.pim.VNode;
import a_vcard.android.syncml.pim.vcard.VCardException;
import a_vcard.android.syncml.pim.vcard.VCardParser;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.ContactsContract;
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

public class ContactsActivity extends Activity
{
  public static final int RESTORING_CODE = 1;
private int j = 0;
  Button btnBackup;
  Button btnDeleteAllContacts;
  //Button btnDeleteBackups;
  Button btnRestore;
  Button btnSendToEmail;
  Button btnViewBackups;
  Cursor cursor;
  AlertDialog dismiss;
  int i = 0;
  String str1;
  @SuppressWarnings("rawtypes")
  Iterator localIterator1;
  Iterator localIterator2;
  FileOutputStream localFileOutputStream;
  
  ProgressDialog pDialog;
  TextView tvLastBackup;
  ArrayList<String> vCard;
  String[] vfile;

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
				nativeAd.sendImpression(ContactsActivity.this);
			}
		}
		
		@Override
		public void onFailedToReceiveAd(Ad ad) {
			
			// Error occurred while loading the native ad
			
		}
	};
  private void get(Cursor paramCursor)
  {
    String str1 = this.cursor.getString(this.cursor.getColumnIndex("lookup"));
    Uri localUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, str1);
    try
    {
      AssetFileDescriptor localAssetFileDescriptor = getContentResolver().openAssetFileDescriptor(localUri, "r");
      FileInputStream localFileInputStream = localAssetFileDescriptor.createInputStream();
      byte[] arrayOfByte = new byte[(int)localAssetFileDescriptor.getDeclaredLength()];
      localFileInputStream.read(arrayOfByte);
      String str2 = new String(arrayOfByte);
      this.vCard.add(str2);
      return;
    }
    catch (Exception localException)
    {
      while (true)
        localException.printStackTrace();
    }
  }

  @SuppressWarnings("unchecked")
private void getVcardString()
    throws IOException
  {
    this.vCard = new ArrayList();
    this.cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    //Log.d("total contacts are", this.cursor.getCount());
     
    if ((this.cursor != null) && (this.cursor.getCount() > 0))
    {
      localFileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + "smsContactsBackups/contacts" + File.separator + this.vfile[0] + ".vcf", false);
      this.cursor.moveToFirst();
      i = this.cursor.getCount();
      this.pDialog.setMax(i);
      
      if (j >= this.cursor.getCount())
      {
    	  //Toast.makeText(getApplicationContext(), "GUd", 400).show();
        localFileOutputStream.close();
        this.cursor.close();
      }
    while (true)
    {
    	
      new Handler(Looper.getMainLooper()).post(new Runnable()
      {
        public void run()
        {
        	final int k = j;
          ContactsActivity.this.pDialog.setProgress(1+k);
          
          /*Toast.makeText(getApplicationContext(), "I"+i, 400).show();*/
          Log.d("value is", ""+k);
          
          if (k == -1 + i)
          {
            Log.d("dismiss", "is called");
            ContactsActivity.this.pDialog.dismiss();
            ContactsActivity.this.setBackupDate();
            ContactsActivity.this.setEmailDialog();
          }
          //return;
        }
      });
      get(this.cursor);
      Log.d("TAG", "Contact " + (j + 1) + "VcF String is" + (String)this.vCard.get(j));
      this.cursor.moveToNext();
      localFileOutputStream.write(((String)this.vCard.get(j)).toString().getBytes());
      j++;
      if (j == i)
      {
    	  ContactsActivity.this.pDialog.dismiss();
    	  //Toast.makeText(getApplicationContext(), "GUd", 400).show();
          return;
      }
      /*if (cursor != null && !cursor.isClosed()) {
          cursor.close();
      }*/
    }
    }
  }

  public void BackupAlert()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
    View localView = getLayoutInflater().inflate(R.layout.layout_backup_dialog, null);
    final EditText localEditText = (EditText)localView.findViewById(R.id.etFileName);
    ((TextView)localView.findViewById(R.id.tvBackupLocation)).setText("/storage/sdcard0/smsContactsBackups/contacts");
    CharSequence localCharSequence = DateFormat.format("yyMMddhhmmss", new Date().getTime());
    localEditText.setText("contacts_" + localCharSequence + ".vcf");
    localBuilder.setView(localView);
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ContactsActivity.this.vfile = localEditText.getText().toString().trim().split(".vcf");
        ContactsActivity.this.setProgressDialog();
        new Thread(new Runnable()
        {
          public void run()
          {
              try {
            	  Looper.prepare();
				ContactsActivity.this.getVcardString();
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
    localAlertDialog.getWindow().getAttributes().alpha = 0.9F;
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    localAlertDialog.show();
  }

  public void deleteAllContactDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setMessage(getString(R.string.are_you_sure_you_wan_to_delete_all_the_contacts_on_the_phone_));
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ContactsActivity.this.panicDialog();
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

  public void deleteAllContacts()
  {
    ContentResolver localContentResolver = getContentResolver();
    Cursor localCursor = localContentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    while (true)
    {
      if (!localCursor.moveToNext())
        return;
      try
      {
        String str = localCursor.getString(localCursor.getColumnIndex("lookup"));
        Uri localUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, str);
        System.out.println("The uri is " + localUri.toString());
        localContentResolver.delete(localUri, null, null);
      }
      catch (Exception localException)
      {
        System.out.println(localException.getStackTrace());
      }
    }
  }

  public String getBackupDate(String paramString)
  {
    return getSharedPreferences("BackupPrefs", 0).getString(paramString, getString(R.string.never_backup));
  }

  public List<FileGetterSetters> getContactFiles()
  {
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "contacts").listFiles();
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

  public int getContactsCount()
  {
    return managedQuery(ContactsContract.Contacts.CONTENT_URI, null, null, null, null).getCount();
  }

  public void initAllViews()
  {
	this.btnBackup = ((Button)findViewById(R.id.btnBackup));
	this.btnRestore = ((Button)findViewById(R.id.btnRestore));
	this.btnViewBackups = ((Button)findViewById(R.id.btnViewBackup));
	this.btnSendToEmail = ((Button)findViewById(R.id.btnSendToEmail));
    this.btnDeleteAllContacts = ((Button)findViewById(R.id.btnDeleteAllContacts));
    TextView localTextView1 = (TextView)findViewById(R.id.tvContacts);
    this.tvLastBackup = ((TextView)findViewById(R.id.tvLastBackup));
    TextView localTextView2 = (TextView)findViewById(R.id.tvTitle);
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "nexalight.otf");
    this.btnBackup.setTypeface(localTypeface);
    this.btnRestore.setTypeface(localTypeface);
    this.btnViewBackups.setTypeface(localTypeface);
    this.btnSendToEmail.setTypeface(localTypeface);
    this.btnDeleteAllContacts.setTypeface(localTypeface);
    localTextView2.setTypeface(localTypeface);
    localTextView1.setTypeface(localTypeface);
    this.tvLastBackup.setTypeface(localTypeface);
    this.btnBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(getContactsCount() > 0)
	    	  {
	    		  ContactsActivity.this.BackupAlert();
	    	  }
	    	  else
	    	  {
	    		  Toast.makeText(getApplicationContext(), "No Contact Found", Toast.LENGTH_SHORT).show();
	    	  }
		}
	});
    this.btnRestore.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ContactsActivity.this.restoreBackupFilesDialog(true);
		}
	});
    this.btnViewBackups.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 ContactsActivity.this.showBackupFilesDialog(false);
		}
	});
    this.btnSendToEmail.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ContactsActivity.this.showBackupFilesDialog(true);
		}
	});
    this.btnDeleteAllContacts.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			ContactsActivity.this.deleteAllContactDialog();
		}
	});
    localTextView1.setText(Html.fromHtml("<font color='#FFFFFF'>Contacts:</font>" + getContactsCount()));
    this.tvLastBackup.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.last_backup) + ":</font>" + getBackupDate("contactBackupDate")));
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

  protected void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 1)
    {
      Log.d("result has been called", "yes");
      new Handler().postDelayed(new Runnable()
      {
        public void run()
        {
          ((TextView)ContactsActivity.this.findViewById(R.id.tvContacts)).setText(Html.fromHtml("<font color='#FFFFFF'>Contacts:</font>" + ContactsActivity.this.getContactsCount()));
        }
      }
      , 10000L);
    }
  }

  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    StartAppSDK.init(this, AppConstants.DEVELOPER_ID, AppConstants.APP_ID, true);
    setContentView(R.layout.layout_contacts_backup);
    initAllViews();
    startAppNativeAd.loadAd(
			new NativeAdPreferences()
				.setAdsNumber(1)
				.setAutoBitmapDownload(true)
				.setImageSize(NativeAdBitmapSize.SIZE150X150),
			nativeAdListener);
  }

  public void panicDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
    localBuilder.setMessage(getString(R.string.are_you_sure_you_wan_to_delete_all_the_contacts_on_the_phone_));
    localBuilder.setPositiveButton("Sure", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        new ContactsActivity.DeletingContacsTask(ContactsActivity.this).execute(new Void[0]);
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

  public void restoreBackupFilesDialog(final boolean paramBoolean)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.backup_files));
    ListView localListView = new ListView(this);
    final List localList = getContactFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        ContactsActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "contacts" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          Intent localIntent = new Intent("android.intent.action.VIEW");
          Log.d("file path is", str);
          localIntent.setDataAndType(Uri.fromFile(new File(str)), "text/x-vcard");
          ContactsActivity.this.startActivityForResult(localIntent, 1);
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
    setLastBackupDate("contactBackupDate", localCharSequence.toString());
  }

  public void setEmailDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.app_name));
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
    localBuilder.setTitle(getString(R.string.backup_files));
    ListView localListView = new ListView(this);
    final List localList = getContactFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        ContactsActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "contacts" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          ContactsActivity.this.sendFileToEmail(str);
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

  public void vCardParser(String paramString)
  {
    VCardParser localVCardParser = new VCardParser();
    VDataBuilder localVDataBuilder = new VDataBuilder();
    int i = 0;
    File localFile = new File(paramString);
    Object localObject = "";
    while (true)
    {
      
      try
      {
        FileInputStream localFileInputStream = new FileInputStream(localFile);
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localFileInputStream, "UTF-8"));
        str1 = localBufferedReader.readLine();
        if (str1 == null)
        {
          Log.d("vCard value is", (String)localObject);
          localBufferedReader.close();
          j = 0;
        }
      }
      catch (IOException localIOException1)
      {
        try
        {
          
          boolean bool = localVCardParser.parse((String)localObject, "UTF-8", localVDataBuilder);
          if (j == 0)
            try
            {
              throw new VCardException("Could not parse vCard file: " + localFile);
            }
            catch (VCardException localVCardException1)
            {
              localVCardException1.printStackTrace();
            }
          List localList = localVDataBuilder.vNodeList;
          localList.size();
          localIterator1 = localList.iterator();
          if (!localIterator1.hasNext())
          {
            
            String str2 = localObject + str1 + "\n";
            localObject = str2;
            return;
          }
        }
        catch (VCardException localVCardException2)
        {
          localVCardException2.printStackTrace();
          continue;
        }
        catch (IOException localIOException2)
        {
          
          VNode localVNode = (VNode)localIterator1.next();
          i++;
          String.valueOf(i);
          localIterator2 = localVNode.propList.iterator();
        }
      }
      while (localIterator2.hasNext())
      {
        PropertyNode localPropertyNode = (PropertyNode)localIterator2.next();
        Log.d("prop Name is", localPropertyNode.propName);
        if ("FN".equals(localPropertyNode.propName))
          Log.d("Name is", localPropertyNode.propValue);
        if ("EMAIL".equals(localPropertyNode.propName))
          Log.d("Email is", localPropertyNode.propValue);
      }
    }
  }

  public class DeletingContacsTask extends AsyncTask<Void, Void, Void>
  {
    public ProgressDialog pd = new ProgressDialog(ContactsActivity.this);

    public DeletingContacsTask(ContactsActivity contactsActivity)
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      ContactsActivity.this.deleteAllContacts();
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      this.pd.dismiss();
      ((TextView)ContactsActivity.this.findViewById(R.id.tvContacts)).setText(Html.fromHtml("<font color='#FFFFFF'>Contacts:</font>" + ContactsActivity.this.getContactsCount()));
      Toast.makeText(ContactsActivity.this.getApplicationContext(), ContactsActivity.this.getString(R.string.contacts_deleted_successfully_), 1).show();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setMessage(ContactsActivity.this.getString(R.string.deleting_all_contacts_));
      this.pd.show();
    }
  }

  public class RestoringTask extends AsyncTask<String, Void, Void>
  {
    ProgressDialog pd = new ProgressDialog(ContactsActivity.this);

    public RestoringTask()
    {
    }

    protected Void doInBackground(String[] paramArrayOfString)
    {
      ContactsActivity.this.vCardParser(paramArrayOfString[0]);
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      this.pd.dismiss();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.show();
    }
  }
}

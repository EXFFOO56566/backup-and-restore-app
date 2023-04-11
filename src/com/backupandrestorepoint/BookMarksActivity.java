package com.backupandrestorepoint;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import module.constants.AppConstants;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdEventListener;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.nativead.NativeAdDetails;
import com.startapp.android.publish.nativead.NativeAdPreferences;
import com.startapp.android.publish.nativead.StartAppNativeAd;
import com.startapp.android.publish.nativead.NativeAdPreferences.NativeAdBitmapSize;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.Browser;
import android.text.Html;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BookMarksActivity extends Activity
{
  private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
  public static final String KEY_BOOKMARK = "bookmark";
  public static final String KEY_DATE = "date";
  public static final String KEY_FAVICON = "favicon";
  public static final String KEY_ID = "id";
  public static final String KEY_THUMBNAIL = "thumbnail";
  public static final String KEY_TITLE = "title";
  public static final String KEY_TOUCH_ICON = "touch_icon";
  public static final String KEY_URL = "url";
  public static final String KEY_USER_ENTERED = "user_entered";
  public static final String KEY_VISITS = "visits";
  Button btnBackup;
  Button btnDeleteAllBookMarks;
  //Button btnDeleteBackups;
  Button btnRestore;
  Button btnSendToEmail;
  Button btnViewBackups;
  AlertDialog dismiss;
  Cursor localCursor;
  String encodedImage;
  BookmarksGettersSetters localBookmarksGettersSetters;
  int j;
  int i;
  ProgressDialog pDialog;
  TextView tvLastBackup;
  FileWriter write;
  String[] xmlfile;
  
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
				nativeAd.sendImpression(BookMarksActivity.this);
			}
		}
		
		@Override
		public void onFailedToReceiveAd(Ad ad) {
			
			// Error occurred while loading the native ad
			
		}
	};
  private void backBookmarks() throws IOException
  {
    String str1 = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "bookmarks";
    
	/*try
    {*/
      this.write = new FileWriter(str1 + File.separator + this.xmlfile[0] + ".xml");
      Uri localUri = Browser.BOOKMARKS_URI;
      ContentResolver localContentResolver = getContentResolver();
      String[] arrayOfString1 = new String[10];
      arrayOfString1[0] = "_id";
      arrayOfString1[1] = "url";
      arrayOfString1[2] = "visits";
      arrayOfString1[3] = "date";
      arrayOfString1[4] = "bookmark";
      arrayOfString1[5] = "title";
      arrayOfString1[6] = "favicon";
      arrayOfString1[7] = "thumbnail";
      arrayOfString1[8] = "touch_icon";
      arrayOfString1[9] = "user_entered";
      localCursor  = localContentResolver.query(localUri, arrayOfString1, null, null, null);
      String[] arrayOfString2 = new String[10];
      arrayOfString2[0] = "_id";
      arrayOfString2[1] = "url";
      arrayOfString2[2] = "visits";
      arrayOfString2[3] = "date";
      arrayOfString2[4] = "bookmark";
      arrayOfString2[5] = "title";
      arrayOfString2[6] = "favicon";
      arrayOfString2[7] = "thumbnail";
      arrayOfString2[8] = "touch_icon";
      arrayOfString2[9] = "user_entered";
      localCursor.moveToFirst();
      i = this.localCursor.getCount();
      this.pDialog.setMax(i);
      //Toast.makeText(getApplicationContext(), "IIIIII"+i, 400).show();
      
      this.write.append("<?xml version='1.0' encoding='UTF-8'?>");
      this.write.append('\n');
      this.write.append("<allBookmarks>");
      this.write.append('\n');
      
      /*if (j >= this.localCursor.getCount())
      {
    	  Toast.makeText(getApplicationContext(), "GUd", 400).show();
    	  write.close();
        //this.localCursor.close();
      }*/
      
      while (true)
      {
          localBookmarksGettersSetters = new BookmarksGettersSetters();
          //String str2 = localCursor.getString(localCursor.getColumnIndex("_id"));
          String str3 = localCursor.getString(localCursor.getColumnIndex("url"));//.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll(">", "&apos;").replaceAll("'", "&frasl;").replaceAll("#", "&#35;").replaceAll("^", "&#94;").replaceAll("_", "&#95;").replaceAll("%", "&#37;").replaceAll("-", "&#45;").replaceAll("=", "&#61;").replaceAll("!", "&#33;").replaceAll("$", "&#36;").replaceAll("`", "&#96;").replaceAll("~", "&#126;").replaceAll("|", "&#124;");
          String str4 = localCursor.getString(localCursor.getColumnIndex("visits"));
          String str5 = localCursor.getString(localCursor.getColumnIndex("date"));
          String str6 = localCursor.getString(localCursor.getColumnIndex("bookmark"));
          String str7 = localCursor.getString(localCursor.getColumnIndex("title"));
         // String str8 = localCursor.getString(localCursor.getColumnIndex("favicon"));
          /*byte[] data = localCursor.getBlob(localCursor.getColumnIndex("favicon"));
          if (data != null) {
              Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
              if(bitmap!=null)
                 // bookmark.setFavicon(bitmap);
              encodedImage = Base64.encodeToString(data, Base64.DEFAULT);
              localBookmarksGettersSetters.setFavicon(encodedImage);
          }*/
          //localBookmarksGettersSetters.setId(str2);
          if (str5 == null)
              str5 = "0";
          String urrl = URLEncoder.encode(str3, "UTF-8");
          localBookmarksGettersSetters.setUrl(urrl);
          localBookmarksGettersSetters.setVisits(str4);
          localBookmarksGettersSetters.setDate(str5);
          localBookmarksGettersSetters.setBookmark(str6);
          localBookmarksGettersSetters.settitle(str7);
          
          
            Handler localHandler = new Handler(Looper.getMainLooper());
            localHandler.post(new Runnable()
            {
            public void run()
            {
            	final int m = j;
              BookMarksActivity.this.pDialog.setProgress(1+m);
              i++;
              //Toast.makeText(getApplicationContext(), "M"+m, 400).show();
              //Toast.makeText(getApplicationContext(), "J"+j, 400).show();
              int v = -1 + i;
              //Toast.makeText(getApplicationContext(), "Vi"+v, 400).show();
             if (m == v)
              {
                Log.d("dismiss", "is called");
                BookMarksActivity.this.pDialog.dismiss();
                BookMarksActivity.this.setBackupDate();
                BookMarksActivity.this.setEmailDialog();
              }
             i--;
             return;
            }
          });
          generateXMLFileForBookmarks(localBookmarksGettersSetters);
          localCursor.moveToNext();
          j++;
         
          if (j == i)
          {
        	  this.write.append("</allBookmarks>");
              this.write.flush();
              this.write.close();
        	  pDialog.dismiss();
              return;
          }
          
      }
  }
 
  private void generateXMLFileForBookmarks(BookmarksGettersSetters paramBookmarksGettersSetters)
  {
    try
    {
      this.write.append("<bookmarks>");
      this.write.append('\n');
      //this.write.append("<id>" + paramBookmarksGettersSetters.getId() + "</id>");
      //this.write.append('\n');
      this.write.append("<url>" + paramBookmarksGettersSetters.getUrl() + "</url>");
      this.write.append('\n');
      this.write.append("<visits>" + paramBookmarksGettersSetters.getVisits() + "</visits>");
      this.write.append('\n');
      this.write.append("<date>" + paramBookmarksGettersSetters.getDate() + "</date>");
      this.write.append('\n');
      this.write.append("<bookmark>" + paramBookmarksGettersSetters.getBookmark() + "</bookmark>");
      this.write.append('\n');
      this.write.append("<title>" + paramBookmarksGettersSetters.gettitle() + "</title>");
      this.write.append('\n');
      //this.write.append("<favicon>" + paramBookmarksGettersSetters.gettitle() + "</favicon>");
      //this.write.append('\n');
      this.write.append("</bookmarks>");
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

  public void BackupAlert()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    View localView = getLayoutInflater().inflate(R.layout.layout_backup_dialog, null);
    final EditText localEditText = (EditText)localView.findViewById(R.id.etFileName);
    ((TextView)localView.findViewById(R.id.tvBackupLocation)).setText("storage/sdcard0/smsContactsBackups/bookmarks");
    CharSequence localCharSequence = DateFormat.format("yyMMddhhmmss", new Date().getTime());
    localEditText.setText("bookmarks_" + localCharSequence + ".xml");
    localBuilder.setView(localView);
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        BookMarksActivity.this.xmlfile = localEditText.getText().toString().trim().split(".xml");
        BookMarksActivity.this.setProgressDialog();
        new Thread(new Runnable()
        {
          public void run()
          {
        	  //Toast.makeText(getApplicationContext(), "Hi", 400).show();
            try {
            	Looper.prepare();
				BookMarksActivity.this.backBookmarks();
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

  public void deleteAllBookmarks()
  {
    Cursor localCursor = getContentResolver().query(Browser.BOOKMARKS_URI, null, null, null, null);
    while (true)
    {
      if (!localCursor.moveToNext())
        return;
      try
      {
        localCursor.getString(0);
        getContentResolver().delete(Browser.BOOKMARKS_URI, null, null);
      }
      catch (Exception localException)
      {
      }
    }
  }

	public void deleteAllBookmarksDialog()
	  {
	    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
	    localBuilder.setTitle(R.string.app_name);
	    localBuilder.setMessage(R.string.are_you_sure_you_wan_to_delete_all_the_bookmarks_on_the_phone_);
	    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
	    {
	      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
	      {
	        BookMarksActivity.this.panicDialog();
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

  public int getBookmarksCount()
  {
    Cursor localCursor = getContentResolver().query(Browser.BOOKMARKS_URI, null, null, null, null);
    String[] arrayOfString = localCursor.getColumnNames();
    for (int i = 0; ; i++)
    {
      if (i >= arrayOfString.length)
        return localCursor.getCount();
      Log.d("Bookmarks", arrayOfString[i]);
    }
  }

  public List<FileGetterSetters> getBookmarksFiles()
  {
    ArrayList localArrayList = new ArrayList();
    File[] arrayOfFile = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "bookmarks").listFiles();
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
    this.btnDeleteAllBookMarks = ((Button)findViewById(R.id.btnDeleteAllBookMarks));
    TextView localTextView1 = (TextView)findViewById(R.id.tvBookmarks);
    this.tvLastBackup = ((TextView)findViewById(R.id.tvLastBackup));
    TextView localTextView2 = (TextView)findViewById(R.id.tvTitle);
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "nexalight.otf");
    this.btnBackup.setTypeface(localTypeface);
    this.btnRestore.setTypeface(localTypeface);
    this.btnViewBackups.setTypeface(localTypeface);
    this.btnSendToEmail.setTypeface(localTypeface);
    this.btnDeleteAllBookMarks.setTypeface(localTypeface);
    localTextView2.setTypeface(localTypeface);
    localTextView1.setTypeface(localTypeface);
    this.tvLastBackup.setTypeface(localTypeface);
    this.btnBackup.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(getBookmarksCount() > 0)
			{
				BookMarksActivity.this.BackupAlert();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "No Bookmark Found", Toast.LENGTH_SHORT).show();
			}
		}
	});
    this.btnRestore.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BookMarksActivity.this.restoreBackupFilesDialog(true);
		}
	});
    this.btnViewBackups.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			 BookMarksActivity.this.showBackupFilesDialog(false);
		}
	});
    this.btnSendToEmail.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BookMarksActivity.this.showBackupFilesDialog(true);
		}
	});
    //this.btnDeleteBackups.setOnClickListener(this.myClick);
    this.btnDeleteAllBookMarks.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			BookMarksActivity.this.deleteAllBookmarksDialog();
		}
	});
    localTextView1.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.bookmarks) + ":</font>" + getBookmarksCount()));
    this.tvLastBackup.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.last_backup) + ":</font>" + getBackupDate("bookmarksBackupDate")));
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
    setContentView(R.layout.layout_bookmarks_backup);
    
    startAppNativeAd.loadAd(
			new NativeAdPreferences()
				.setAdsNumber(1)
				.setAutoBitmapDownload(true)
				.setImageSize(NativeAdBitmapSize.SIZE150X150),
			nativeAdListener);
    initAllViews();
  }

  public void panicDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setIcon(R.drawable.icon_warning);
    localBuilder.setMessage(R.string.are_you_sure_you_wan_to_delete_all_the_bookmarks_on_the_phone_);
    localBuilder.setPositiveButton("Sure", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        new DeletingBookmarksTask().execute(new Void[0]);
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
    try {
    	//String decode_url=URLDecoder.decode(encode_url,"UTF-8");
		String urrl = URLDecoder.decode(paramHashMap.get("url"), "UTF-8");
		localContentValues.put("url", urrl);//(String)paramHashMap.get("url")
	    localContentValues.put("visits", (String)paramHashMap.get("visits"));
	    localContentValues.put("date", (String)paramHashMap.get("date"));
	    //localContentValues.put("_id", (String)paramHashMap.get("_id"));
	    localContentValues.put("bookmark", (String)paramHashMap.get("bookmark"));
	    localContentValues.put("title", (String)paramHashMap.get("title"));
	    //localContentValues.put("favicon", (String)paramHashMap.get("favicon"));
	    getContentResolver().insert(Browser.BOOKMARKS_URI, localContentValues);
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }

  public void restoreBackupFilesDialog(final boolean paramBoolean)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(getString(R.string.backup_files));
    ListView localListView = new ListView(this);
    final List localList = getBookmarksFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        BookMarksActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "bookmarks" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          Log.d("Seleted file path is", str);
          BookMarksActivity.RestoringTask localRestoringTask = new BookMarksActivity.RestoringTask(BookMarksActivity.this);
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

  public void onDestroy() {
	   super.onDestroy();
	   if (localCursor != null) {
	      localCursor.close();
	   }
  }
  public void setBackupDate()
  {
    CharSequence localCharSequence = DateFormat.format("yy/MM/dd hh:mm:ss", new Date().getTime());
    this.tvLastBackup.setText(Html.fromHtml("<font color='#FFFFFF'>" + getString(R.string.last_backup) + ":</font>" + localCharSequence.toString()));
    setLastBackupDate("bookmarksBackupDate", localCharSequence.toString());
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
    final List localList = getBookmarksFiles();
    localListView.setAdapter(new FileAdapter(this, R.layout.item_row_file, localList));
    localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        BookMarksActivity.this.dismiss.dismiss();
        if (paramBoolean)
        {
          String str = Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "bookmarks" + File.separator + ((FileGetterSetters)localList.get(paramAnonymousInt)).getFileName();
          Log.i("aaaaaaaaaaaaaaaaaaaaaaaaa", str);
          BookMarksActivity.this.sendFileToEmail(str);
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

  public void viewBookmarksDialog()
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle(R.string.app_name);
    localBuilder.setMessage(getString(R.string.restore_completed_successfully_));
    localBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.google.com"));
        BookMarksActivity.this.startActivity(localIntent);
      }
    });
    localBuilder.setNegativeButton("Cancel", null);
    AlertDialog localAlertDialog = localBuilder.create();
    localAlertDialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
    localAlertDialog.show();
  }

  public class DeletingBookmarksTask extends AsyncTask<Void, Void, Void>
  {
    public ProgressDialog pd = new ProgressDialog(BookMarksActivity.this);

    public DeletingBookmarksTask()
    {
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      BookMarksActivity.this.deleteAllBookmarks();
      return null;
    }

    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      this.pd.dismiss();
      ((TextView)BookMarksActivity.this.findViewById(R.id.tvBookmarks)).setText(Html.fromHtml("<font color='#FFFFFF'>Bookmarks:</font>" + BookMarksActivity.this.getBookmarksCount()));
      Toast.makeText(BookMarksActivity.this.getApplicationContext(), R.string.bookmarks_deleted_successfully_, 1).show();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setMessage(BookMarksActivity.this.getString(R.string.deleting_all_bookmarks_));
      this.pd.show();
    }
  }
  
 

  @SuppressLint("NewApi")
public class RestoringTask extends AsyncTask<String, Integer, String>
  {
    ProgressDialog pd = new ProgressDialog(BookMarksActivity.this);

    public RestoringTask(BookMarksActivity bookMarksActivity)
    {
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	protected String doInBackground(String[] paramArrayOfString)
    {
      String str = BookMarksActivity.this.getXML(paramArrayOfString[0]);
      XMLParser localXMLParser = new XMLParser();
      NodeList localNodeList = localXMLParser.getDomElement(str).getElementsByTagName("bookmarks");      
      
      int i = localNodeList.getLength();
      for (int j = 0; ; j++)
      {
        if (j >= i)
          return "";
        HashMap localHashMap = new HashMap();
        Element localElement = (Element)localNodeList.item(j);
        //localHashMap.put("id", localXMLParser.getValue(localElement, "id"));
        localHashMap.put("url", localXMLParser.getValue(localElement, "url"));
        localHashMap.put("visits", localXMLParser.getValue(localElement, "visits"));
        localHashMap.put("date", localXMLParser.getValue(localElement, "date"));
        localHashMap.put("bookmark", localXMLParser.getValue(localElement, "bookmark"));
        localHashMap.put("title", localXMLParser.getValue(localElement, "title"));
        //localHashMap.put("favicon", localXMLParser.getValue(localElement, "favicon"));
        
        BookMarksActivity.this.restoreBackup(localHashMap);
        Integer[] arrayOfInteger = new Integer[2];
        arrayOfInteger[0] = Integer.valueOf(i);
        arrayOfInteger[1] = Integer.valueOf(j);
        publishProgress(arrayOfInteger);
      }
    }

    protected void onPostExecute(String paramString)
    {
      super.onPostExecute(paramString);
      ((TextView)BookMarksActivity.this.findViewById(R.id.tvBookmarks)).setText(Html.fromHtml("<font color='#FFFFFF'>Bookmarks:</font>" + BookMarksActivity.this.getBookmarksCount()));
      this.pd.dismiss();
      BookMarksActivity.this.viewBookmarksDialog();
    }

    protected void onPreExecute()
    {
      super.onPreExecute();
      this.pd.setProgressStyle(1);
      this.pd.setMessage(BookMarksActivity.this.getString(R.string.restoring_backups_));
      this.pd.setProgressDrawable(BookMarksActivity.this.getResources().getDrawable(R.drawable.greenprogress));
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

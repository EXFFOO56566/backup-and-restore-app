package com.backupandrestorepoint;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

public class AppMainActivity extends ListActivity {
private PackageManager packageManager = null;
private List<ApplicationInfo> applist = null;
private ApplicationAdapter listadaptor = null;
boolean blo = true;
Button btn1;
private ProgressDialog progressDialog;

@Override
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app);
    btn1 = (Button)findViewById(R.id.button1);
    Typeface localTypeface = Typeface.createFromAsset(getAssets(), "nexalight.otf");
    btn1.setTypeface(localTypeface);
    btn1.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
				new LoApplications().execute();
			//
			/*if(Utils.getFreeSpace() > 20L)
			{
				Toast.makeText(getApplicationContext(), "Hi", 100).show();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Enough Sapce", Toast.LENGTH_SHORT);
			}*/
		}
	});
    //
    packageManager = getPackageManager();
    /*final CheckBox chkbox = (CheckBox)findViewById(R.id.chkAll);
    chkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			// TODO Auto-generated method stub
			if(isChecked == true)
			{
				listadaptor = new ApplicationAdapter(AppMainActivity.this,R.layout.row, applist,true);
				//setListAdapter(listadaptor);
			}
			else
			{
				listadaptor = new ApplicationAdapter(AppMainActivity.this,R.layout.row, applist,false);
				//setListAdapter(listadaptor);
			}
			 setListAdapter(listadaptor);
		}

	});
    */
    new LoadApplications().execute();
}

@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);

    ApplicationInfo app = applist.get(position);
    try {
        Intent intent = packageManager
                .getLaunchIntentForPackage(app.packageName);

        if (null != intent) {
            startActivity(intent);
        }
    } catch (ActivityNotFoundException e) {
        Toast.makeText(AppMainActivity.this, e.getMessage(),
                Toast.LENGTH_LONG).show();
    } catch (Exception e) {
        Toast.makeText(AppMainActivity.this, e.getMessage(),
                Toast.LENGTH_LONG).show();
    }
}

public void app()
{
	final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
    @SuppressWarnings("rawtypes")
	final List pkgAppsList = getPackageManager().queryIntentActivities( mainIntent, 0);
    
    int z = 0;
    for (Object object : pkgAppsList) {

        ResolveInfo info = (ResolveInfo) object;

        File f1 =new File( info.activityInfo.applicationInfo.publicSourceDir);

            try{
                String file_name = info.loadLabel(getApplicationContext().getPackageManager()).toString();
               
                File f2 = new File(Environment.getExternalStorageDirectory() + File.separator + "smsContactsBackups" + File.separator + "App"); 
                f2.mkdirs();            
                f2 = new File(f2.getPath()+"/"+file_name+".apk");
                f2.createNewFile();
                InputStream in = new FileInputStream(f1);

                OutputStream out = new FileOutputStream(f2);

                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0){
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                System.out.println("File copied.");
            }
            catch(FileNotFoundException ex){
                System.out.println(ex.getMessage() + " in the specified directory.");
            }
            catch(IOException e){
                System.out.println(e.getMessage());      
            }
    }
}
private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {
    ArrayList<ApplicationInfo> applist = new ArrayList<ApplicationInfo>();
    for (ApplicationInfo info : list) {
        try {
            if (null != packageManager.getLaunchIntentForPackage(info.packageName)) {
                applist.add(info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    return applist;
}

private class LoadApplications extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progress = null;

    @Override
    protected Void doInBackground(Void... params) {
        applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        listadaptor = new ApplicationAdapter(AppMainActivity.this,R.layout.row, applist,false);

        return null;
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    @Override
    protected void onPostExecute(Void result) {
        setListAdapter(listadaptor);
        progress.dismiss();
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
        progress = ProgressDialog.show(AppMainActivity.this, null,
                "Loading application info...");
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}

private class LoApplications extends AsyncTask<Void, Void, Void> {
    private ProgressDialog progress1 = null;

    @Override
    protected Void doInBackground(Void... params) {
       // applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
        listadaptor = new ApplicationAdapter(AppMainActivity.this,R.layout.row, applist,true);
        app();
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //setListAdapter(listadaptor);
        progress1.dismiss();
        super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
    	listadaptor = new ApplicationAdapter(AppMainActivity.this,R.layout.row, applist,true);
        progress1 = ProgressDialog.show(AppMainActivity.this, null,
                "Generate Apk File...");
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    	
        super.onProgressUpdate(values);
    }
}
}
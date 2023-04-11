package com.backupandrestorepoint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class FileAdapter extends ArrayAdapter<FileGetterSetters>
{
  Context _context;
  List<FileGetterSetters> _data;
  int _resource;
  LayoutInflater inflater;

  public FileAdapter(Context paramContext, int paramInt, List<FileGetterSetters> paramList)
  {
    super(paramContext, paramInt, paramList);
    this._context = paramContext;
    this._resource = paramInt;
    this._data = paramList;
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
      this.inflater = LayoutInflater.from(this._context);
    for (View localView = this.inflater.inflate(this._resource, paramViewGroup, false); ; localView = paramView)
    {
      TextView localTextView1 = (TextView)localView.findViewById(R.id.tvFileName);
      TextView localTextView2 = (TextView)localView.findViewById(R.id.tvDateCreated);
      localTextView1.setText(((FileGetterSetters)this._data.get(paramInt)).getFileName());
      localTextView2.setText(((FileGetterSetters)this._data.get(paramInt)).getDateCreated());
      return localView;
    }
  }
}

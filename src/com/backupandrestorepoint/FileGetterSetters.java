package com.backupandrestorepoint;

public class FileGetterSetters
{
  boolean checked = false;
  private String dateCreated_;
  private String fileName_;

  public String getDateCreated()
  {
    return this.dateCreated_;
  }

  public String getFileName()
  {
    return this.fileName_;
  }

  public boolean isChecked()
  {
    return this.checked;
  }

  public void setChecked(boolean paramBoolean)
  {
    this.checked = paramBoolean;
  }

  public void setDateCreated(String paramString)
  {
    this.dateCreated_ = paramString;
  }

  public void setFileName(String paramString)
  {
    this.fileName_ = paramString;
  }
}

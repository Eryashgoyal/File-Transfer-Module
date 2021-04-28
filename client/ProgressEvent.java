import java.io.*;
public class ProgressEvent
{
private File file;
private String id;
private long numberOfBytesUploaded;
private String message;
public ProgressEvent()
{
this.file=null;
this.id=null;
this.numberOfBytesUploaded=0;
this.message=null;
}
public void setMessage(String message)
{
this.message=message;
}
public String getMessage()
{
return this.message;
}
public void setFile(File file)
{
this.file=file;
}
public File getFile()
{
return this.file;
}
public void setId(String id)
{
this.id=id;
}
public String getId()
{
return this.id;
}
public void setNumberOfBytesUploaded(long numberOfBytesUploaded)
{
this.numberOfBytesUploaded=numberOfBytesUploaded;
}
public long getNumberOfBytesUploaded()
{
return this.numberOfBytesUploaded;
}
}
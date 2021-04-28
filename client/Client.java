import javax.swing.*;
import java.io.*;
import java.net.*;
public class Client extends Thread
{
private AddProgressListener addProgressListener;
private Socket socket;
private InputStream is;
private OutputStream os;
private File file;
private String id;
private ProgressEvent progressEvent;
public Client(AddProgressListener addProgressListener,String id,File file)
{
this.id=id;
this.addProgressListener=addProgressListener;
this.file=file;
}
public void run()
{
try
{
this.socket=new Socket("localhost",9550);
this.is=socket.getInputStream();
this.os=socket.getOutputStream();
long lengthOfFile=file.length();
byte requestHeader[]=new byte[1024];
int i=0;
long lof=lengthOfFile;
while(lof>0)
{
requestHeader[i]=(byte)(lof%10);
lof/=10;
i++;
}
requestHeader[i]=(byte)',';
i++;
int k=0;
while(k<file.getAbsolutePath().length())
{
requestHeader[i]=(byte)file.getAbsolutePath().charAt(k);
k++;
i++;
}
while(i<requestHeader.length)
{
requestHeader[i]=(byte)' ';
i++;
}
int bytesRead;
int bytesSend=0;
int r=0;
byte ack[]=new byte[2];
int ackr;
int howMany=1024;
while(bytesSend<requestHeader.length)
{
if((requestHeader.length-bytesSend)<howMany) howMany=requestHeader.length-bytesSend;
os.write(requestHeader,0,howMany);
os.flush();
bytesSend+=howMany;
}
r=0;
while(true)
{
ackr=is.read(ack);
if(ackr==-1) continue;
r+=ackr;
if(r==2) break;
}
FileInputStream fis=new FileInputStream(file);
bytesSend=0;
byte chunk[]=new byte[4096];
howMany=4096;
SwingUtilities.invokeLater(new Thread(){
public void run()
{
progressEvent=new ProgressEvent();
progressEvent.setMessage("File : "+file.getName()+" is being uploading,please wait...");
progressEvent.setFile(file);
progressEvent.setId(id);
addProgressListener.fileNameStatusChanged(progressEvent);
}
});
while(bytesSend<lengthOfFile)
{
if((lengthOfFile-bytesSend)<howMany) howMany=(int)lengthOfFile-bytesSend;
fis.read(chunk);
os.write(chunk,0,howMany);
os.flush();
r=0;
while(true)
{
ackr=is.read(ack);
if(ackr==-1) continue;
r+=ackr;
if(r==2) break;
}
bytesSend+=howMany;
long bs=bytesSend;
SwingUtilities.invokeLater(new Thread(){
public void run()
{
progressEvent=new ProgressEvent();
progressEvent.setNumberOfBytesUploaded(bs);
progressEvent.setFile(file);
progressEvent.setId(id);
addProgressListener.progressPanelStatusChanged(progressEvent);
}
});
}
SwingUtilities.invokeLater(new Thread(){
public void run()
{
progressEvent=new ProgressEvent();
progressEvent.setMessage("File : "+file.getName()+" uploaded successfully");
progressEvent.setFile(file);
progressEvent.setId(id);
addProgressListener.fileNameStatusChanged(progressEvent);
}
});
fis.close();
socket.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage()); //remove after testing
}
}
}
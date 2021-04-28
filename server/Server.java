import javax.swing.*;
import java.io.*;
import java.net.*;
class Server extends Thread
{
private ServerSocket serverSocket;
private int portNumber;
private ServerFrame serverFrame;
public Server(ServerFrame serverFrame)
{
try
{
this.serverFrame=serverFrame;
this.portNumber=9550;
this.serverSocket=new ServerSocket(portNumber);
this.portNumber=portNumber;
}catch(IOException ioException)
{
System.out.println(ioException.getMessage()); //remove after testing
}
}
public void shutDown()
{
try
{
this.serverSocket.close();
}catch(Exception exception)
{
System.out.println(exception.getMessage());
}
}
public void run()
{
try
{
RequestProcessor requestProcessor;
Socket socket;
while(true)
{
SwingUtilities.invokeLater(()->{
serverFrame.append("Server is ready on port Number : "+portNumber);
});
socket=serverSocket.accept();
SwingUtilities.invokeLater(()->{
serverFrame.append("Request arrived and processing...");
});
requestProcessor=new RequestProcessor(socket,serverFrame);
}
}catch(IOException ioException)
{
System.out.println(ioException.getMessage()); //remove after testing
}
}
}
class RequestProcessor extends Thread
{
private Socket socket;
private InputStream is;
private OutputStream os;
private ServerFrame serverFrame;
public RequestProcessor(Socket socket,ServerFrame serverFrame)
{
this.serverFrame=serverFrame;
try
{
this.socket=socket;
this.is=socket.getInputStream();
this.os=socket.getOutputStream();
start();
}catch(SocketException socketException)
{
System.out.println(socketException.getMessage()); //remove after testing
}
catch(IOException ioException)
{
System.out.println(ioException.getMessage()); //remove after testing
}
}
public void run()
{
try
{
byte requestHeader[]=new byte[1024];
int bytesConsumed=0;
byte chunk[]=new byte[1024];
int r=0;
int bytesRead;
while(bytesConsumed<requestHeader.length)
{
bytesRead=is.read(chunk);
if(bytesRead==-1) continue;
for(int i=0;i<bytesRead;i++)
{
requestHeader[r]=chunk[i];
r++;
}
bytesConsumed+=bytesRead;
}
byte ack[]={23,34};
os.write(ack);
os.flush();
long lengthOfFile=0;
int i=0;
int u=1;
while(requestHeader[i]!=(byte)',')
{
lengthOfFile=lengthOfFile+(long)(requestHeader[i]*u);
u=u*10;
i++;
}
i++;
StringBuffer stringBuffer=new StringBuffer();
while(i<requestHeader.length)
{
stringBuffer.append((char)requestHeader[i]);
i++;
}
long lof=lengthOfFile;
String fileName=stringBuffer.toString().trim();
SwingUtilities.invokeLater(()->{
serverFrame.append("File Name : "+fileName);
serverFrame.append("File length : "+lof);
});
File file=new File(fileName);
FileOutputStream fos=new FileOutputStream("uploads"+file.separator+file.getName());
bytesConsumed=0;
chunk=new byte[4096];
while(bytesConsumed<lengthOfFile)
{
bytesRead=is.read(chunk);
if(bytesRead==-1) continue;
fos.write(chunk,0,bytesRead);
os.write(ack);
os.flush();
bytesConsumed+=bytesRead;
}
fos.close();
SwingUtilities.invokeLater(new Thread(){
public void run()
{
serverFrame.append("File : "+file.getName()+" Uploaded successfully.");
}
});
socket.close();
}catch(IOException ioException)
{
System.out.println(ioException.getMessage()); //remove after tesing
}
}
}
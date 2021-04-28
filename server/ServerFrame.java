import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
class ServerFrame extends JFrame
{
private Container container;
private JTextArea jta;
private JScrollPane jsp;
private JButton button;
private boolean considerIt=false;
private Server server;
public ServerFrame()
{
container=getContentPane();
button=new JButton("Start");
jta=new JTextArea();
jsp=new JScrollPane(jta,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container.setLayout(new BorderLayout());
container.add(jsp);
container.add(button,BorderLayout.SOUTH);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
setSize(500,650);
setLocation(0,10);
button.addActionListener((ae)->{
if(considerIt==false)
{
considerIt=true;
button.setText("Stop");
server=new Server(this);
server.start();
}
else
{
considerIt=false;
button.setText("Start");
server.shutDown();
}
});
}
public void append(String message)
{
jta.append(message+"\n");
}
public static void main(String gg[])
{
new ServerFrame();
}
}
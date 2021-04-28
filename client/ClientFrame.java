import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import javax.swing.table.*;
class ClientFrame extends JFrame
{
private Container container;
private Client client;
private RightPanel rightPanel;
private LeftPanel leftPanel;
private Model model;
public ClientFrame()
{
container=getContentPane();
container.setLayout(new GridLayout(1,2));
this.leftPanel=new LeftPanel();
this.rightPanel=new RightPanel();
container.add(leftPanel);
container.add(rightPanel);
setSize(780,650);
setLocation(500,10);
setVisible(true);
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
public void remove(File file)
{
for(int i=0;i<model.size();i++)
{
if(model.get(i).getAbsolutePath().equals(file.getAbsolutePath()))
{
model.remove(i);
break;
}
}
}
class LeftPanel extends JPanel
{
private JLabel titleLabel;
private JTable table;
private JButton addButton;
private JScrollPane jsp;
private JFileChooser jFileChooser;
private File file;
public LeftPanel()
{
jFileChooser=new JFileChooser();
jFileChooser.setCurrentDirectory(new File("."));
titleLabel=new JLabel("Selected Files-");
model=new Model();
table=new JTable(model);
DefaultTableCellRenderer cellRenderer=new DefaultTableCellRenderer();
cellRenderer.setHorizontalAlignment(JLabel.CENTER);
table.getColumnModel().getColumn(1).setPreferredWidth(500);
table.getColumnModel().getColumn(0).setCellRenderer(cellRenderer);
addButton=new JButton("Add");
jsp=new JScrollPane(table,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
setLayout(new BorderLayout());
add(titleLabel,BorderLayout.NORTH);
add(jsp);
add(addButton,BorderLayout.SOUTH);
addButton.addActionListener((ae)->{
int selectedFile=jFileChooser.showOpenDialog(this);
file=jFileChooser.getSelectedFile();
if(selectedFile==JFileChooser.APPROVE_OPTION)
{
for(int i=0;i<model.size();i++)
{
if(model.get(i).getAbsolutePath().equals(file.getAbsolutePath()))
{
JOptionPane.showMessageDialog(this,"File : "+file.getAbsolutePath()+" exists.");
return;
}
}
model.add(file);
}
});
}
}
class RightPanel extends JPanel implements AddProgressListener
{
private JButton uploadButton;
private ProgressPanel progressPanel;
private java.util.List<ProgressPanel> progressPanels;
private JPanel progressPanelContainer;
private java.util.List<Client> clients;
public RightPanel()
{
uploadButton=new JButton("Upload");
setLayout(new BorderLayout());
add(uploadButton,BorderLayout.NORTH);
uploadButton.addActionListener((ae)->{
progressPanels=new java.util.ArrayList<>();
progressPanelContainer=new JPanel();
clients=new java.util.ArrayList<>();
if(model.size()<9) progressPanelContainer.setLayout(new GridLayout(9,2));
else progressPanelContainer.setLayout(new GridLayout(model.size(),2));
String id;
for(int i=0;i<model.size();i++)
{
id=UUID.randomUUID().toString();
progressPanel=new ProgressPanel(model.get(i),id);
progressPanels.add(progressPanel);
progressPanelContainer.add(progressPanel);
client=new Client(this,id,model.get(i));
clients.add(client);
}
add(progressPanelContainer,BorderLayout.CENTER);
container.validate();
container.repaint();
for(int i=0;i<clients.size();i++) clients.get(i).start();
});
}
public void progressPanelStatusChanged(ProgressEvent progressEvent)
{
long numberOfBytesUploaded=progressEvent.getNumberOfBytesUploaded();
File file=progressEvent.getFile();
String id=progressEvent.getId();
for(ProgressPanel progressPanel:progressPanels)
{
if(progressPanel.getId().equals(id))
{
progressPanel.setValue(numberOfBytesUploaded);
break;
}
}
}
public void fileNameStatusChanged(ProgressEvent progressEvent)
{
long numberOfBytesUploaded=progressEvent.getNumberOfBytesUploaded();
File file=progressEvent.getFile();
String id=progressEvent.getId();
String message=progressEvent.getMessage();
for(ProgressPanel progressPanel:progressPanels)
{
if(progressPanel.getId().equals(id))
{
progressPanel.updateLog(message);
break;
}
}
}
class ProgressPanel extends JPanel
{
private File file;
private JProgressBar jProgressBar;
private JLabel fileLabel;
private String id;
ProgressPanel(File file,String id)
{
this.id=id;
fileLabel=new JLabel(file.getAbsolutePath());
this.file=file;
jProgressBar=new JProgressBar(1,(int)file.length());
setLayout(new GridLayout(2,1));
add(fileLabel);
add(jProgressBar);
}
public void setId(String id)
{
this.id=id;
}
public String getId()
{
return this.id;
}
public void updateLog(String message)
{
this.fileLabel.setText(message);
if(message.equals("File : "+file.getName()+" uploaded successfully"))
{
ClientFrame.this.remove(file);
if(model.size()==0)
{
JOptionPane.showMessageDialog(this,"Everything is uploaded successfully.");
rightPanel.remove(progressPanelContainer);
container.validate();
container.repaint();
}
}
}
public void setValue(long numberOfBytesUploaded)
{
this.jProgressBar.setValue((int)numberOfBytesUploaded);
}
}
}
public static void main(String gg[])
{
new ClientFrame();
}
}
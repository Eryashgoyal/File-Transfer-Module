import javax.swing.table.*;
import java.io.*;
public class Model extends AbstractTableModel
{
java.util.List<File> files;
public Model()
{
files=new java.util.LinkedList<>();
}
public int getRowCount()
{
return this.files.size();
}
public int getColumnCount()
{
return 2;
}
public Class getColumnClass(int columnIndex)
{
if(columnIndex==0) return Integer.class;
else return String.class;
}
public String getColumnName(int columnIndex)
{
if(columnIndex==0) return "S.No.";
else return "File Name ";
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0) return (rowIndex+1);
else return this.files.get(rowIndex).getAbsolutePath();
}
public void add(File file)
{
this.files.add(file);
fireTableDataChanged();
}
public void remove(int index)
{
this.files.remove(index);
fireTableDataChanged();
}
public int size()
{
return this.files.size();
}
public File get(int index)
{
return this.files.get(index);
}
public java.util.List<File> getFiles()
{
return this.files;
}
}
package comp475.teammuon.projecthulk;

import javax.swing.table.DefaultTableModel;

public class DAQTableModel extends DefaultTableModel
{
    DAQTableModel(Object[][] object, String[] string) 
    {    super(object, string);  }
    
    @Override
    public boolean isCellEditable(int row, int column)
    {   return false;   }
}

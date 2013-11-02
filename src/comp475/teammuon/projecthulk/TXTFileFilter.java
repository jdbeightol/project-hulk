package comp475.teammuon.projecthulk;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class TXTFileFilter extends FileFilter
{
    @Override
    public boolean accept(File inFile) 
    {
        if (inFile.isDirectory())
            return true;
    
        String filename = inFile.getName();
    
        int index = filename.lastIndexOf('.');

        if (index > 0 && index < filename.length() - 1)
            if(filename.substring(index + 1).toLowerCase().equals("txt" ))
                return true;

        return false;
    }

    @Override
    public String getDescription()
    {
        return "*.txt";
    }
}

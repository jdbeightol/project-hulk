package comp475.teammuon.projecthulk;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

public final class ClipboardManager implements ClipboardOwner
{
    public void setContents(String inStr)
    {
        Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();
        clippy.setContents(new StringSelection(inStr), this);
    }
    
    public String getContents()
    {
        String result = "";
        Clipboard clippy = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable cont = clippy.getContents(null);
        
        if(cont!=null && cont.isDataFlavorSupported(DataFlavor.stringFlavor))
            try
            {
                result = (String)cont.getTransferData(DataFlavor.stringFlavor);
            } catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        
        return result;
    }

    @Override
    public void lostOwnership(Clipboard clpbrd, Transferable t)
    {
        System.out.println("Lost clipboard ownership.");
    }
}

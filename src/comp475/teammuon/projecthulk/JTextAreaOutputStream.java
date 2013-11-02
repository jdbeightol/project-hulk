package comp475.teammuon.projecthulk;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JTextAreaOutputStream extends OutputStream
{
    private final JTextArea textArea;
    private final StringBuilder sb = new StringBuilder();
    private String title;

    public JTextAreaOutputStream(final JTextArea textArea, String title)
    {
        this.textArea = textArea;
        this.title = title;
        sb.append((this.title + "> "));
    }

    @Override
    public void write(int i) throws IOException
    {

      if (i == '\r')
         return;

      if (i == '\n') {
         final String text = sb.toString() + "\n";
         SwingUtilities.invokeLater(new Runnable() {
            public void run() {
               textArea.append(text);
            }
         });
         sb.setLength(0);
         sb.append((this.title + "> "));

         return;
      }

      sb.append((char) i);        
    }
        
}

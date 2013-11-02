package comp475.teammuon.projecthulk;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.JInternalFrame;

public class AppWorkspace extends javax.swing.JFrame
{
    static int openFrameCount = 0;
    static final int xOffset = 20, yOffset = 20;

    private ArrayList<JInternalFrame> windowList
            = new ArrayList<JInternalFrame>();
    
    public AppWorkspace()
    {
        initComponents();
        this.setExtendedState(this.getExtendedState() | MAXIMIZED_BOTH);
        this.desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }
    
    public void refreshWindowList()
    {
        //@todo Add a refreshing window list.  Not really important.
//        for(JInternalFrame window : windowList)
            
    }
    
    public void showChartWindow(String chartName, String fileName,
            XYSeries[] XYPlotSeries)
    {
        XYSeriesCollection XYPlotData = new XYSeriesCollection();

        for (XYSeries x : XYPlotSeries)
            if (!x.isEmpty())
                XYPlotData.addSeries(x);
        
        HistChartForm chartWindow = new HistChartForm("Histogram of " + fileName,
                chartName, XYPlotData);
        
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() 
                < Toolkit.getDefaultToolkit().getScreenSize().getWidth())
            chartWindow.setSize(new Dimension(
             4*(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.2), 
             3*(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.2)
        ));
        
        else
            chartWindow.setSize(new Dimension(
             4*(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.2),
             3*(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.2)
        ));
        
        chartWindow.pack();
        chartWindow.setVisible(true);
        desktopPane.add(chartWindow);

        chartWindow.setLocation(xOffset*(openFrameCount%7), 
                yOffset*(openFrameCount++%7));
        
        chartWindow.moveToFront();
        chartWindow.requestFocus();
        
        windowList.add((JInternalFrame) chartWindow);
    }

    public void showSessionWindow(String file)
    {
        SessionDataForm sessWindow = new SessionDataForm();
        sessWindow.openFile(file);
        this.showSessionWindow(sessWindow);
    }
        
    public void showSessionWindow(Connection inConn, String sessHash)
    {
        SessionDataForm sessWindow = new SessionDataForm();
        sessWindow.parseDB(inConn, sessHash);
        this.showSessionWindow(sessWindow);
    }
    
    private void showSessionWindow(SessionDataForm sessWindow)
    {
        this.desktopPane.add(sessWindow);
        
        sessWindow.setSize(new Dimension(
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.7),             
              (int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.7)
        ));
        
        sessWindow.setVisible(true);

        sessWindow.setLocation(xOffset*(openFrameCount%7), 
                yOffset*(openFrameCount++%7));
       
        sessWindow.moveToFront();
        sessWindow.requestFocus();

        windowList.add((JInternalFrame) sessWindow);        
    }

    public void showCommWindow()
    {
        CommTerminalForm commWindow = new CommTerminalForm();
        commWindow.setVisible(true);

        this.desktopPane.add(commWindow);
        
        if(Toolkit.getDefaultToolkit().getScreenSize().getHeight() 
                < Toolkit.getDefaultToolkit().getScreenSize().getWidth())
            commWindow.setSize(new Dimension(
             16*(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.07), 
             9*(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()*.07)
        ));
        
        else
            commWindow.setSize(new Dimension(
             16*(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.06),
             9*(int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()*.06)
        ));

        commWindow.setLocation(xOffset*(openFrameCount%7), 
                yOffset*(openFrameCount++%7));

        commWindow.moveToFront();
        commWindow.requestFocus();
        
        windowList.add((JInternalFrame) commWindow);
    }
        
    public void showCondWindow(CR_Session sessIn)
    {
        GraphConditionForm condWindow = new GraphConditionForm(sessIn);

        this.desktopPane.add(condWindow);

        condWindow.setLocation((this.desktopPane.getWidth() 
                - condWindow.getWidth()) / 2,
                (this.desktopPane.getHeight() - condWindow.getHeight()) / 2);
        
        condWindow.setVisible(true);
        condWindow.moveToFront();
        condWindow.requestFocus();

        windowList.add((JInternalFrame) condWindow);
    }
    
    public void showDBWindow()
    {
        DBManager dbWindow = new DBManager();

        this.desktopPane.add(dbWindow);

        dbWindow.setLocation((this.desktopPane.getWidth() 
                - dbWindow.getWidth()) / 2,
                (this.desktopPane.getHeight() - dbWindow.getHeight()) / 2);
        
        dbWindow.setVisible(true);
        dbWindow.moveToFront();
        dbWindow.requestFocus();

        windowList.add((JInternalFrame) dbWindow);
    }
    
    public void showCommSettings(CommTerminalForm in)
    {
        CommSetupForm commSetWindow = new CommSetupForm(in);

        this.desktopPane.add(commSetWindow);

        commSetWindow.setLocation((this.desktopPane.getWidth() 
                - commSetWindow.getWidth()) / 2,
                (this.desktopPane.getHeight() - commSetWindow.getHeight()) / 2);
        
        commSetWindow.setVisible(true);
        commSetWindow.moveToFront();
        commSetWindow.requestFocus();

        windowList.add((JInternalFrame) commSetWindow);
    }
        
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        exitMenuItem = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Project HULK");
        addWindowListener(new java.awt.event.WindowAdapter()
        {
            public void windowOpened(java.awt.event.WindowEvent evt)
            {
                formWindowOpened(evt);
            }
            public void windowClosing(java.awt.event.WindowEvent evt)
            {
                formWindowClosing(evt);
            }
        });

        desktopPane.setBackground(new java.awt.Color(153, 153, 153));

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        exitMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu2.setText("Session");

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open DAQ File...");
        openMenuItem.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                openMenuItemActionPerformed(evt);
            }
        });
        jMenu2.add(openMenuItem);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setMnemonic('p');
        jMenuItem1.setText("Open Previous Session...");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);
        jMenu2.add(jSeparator2);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setMnemonic('c');
        jMenuItem2.setText("Connect to Device...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        menuBar.add(jMenu2);

        jMenu1.setMnemonic('r');
        jMenu1.setText("Window");

        jMenuItem4.setMnemonic('r');
        jMenuItem4.setText("Restore All");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);

        jMenuItem3.setMnemonic('m');
        jMenuItem3.setText("Minimize All");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem3);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
    }//GEN-LAST:event_formWindowOpened

    private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openMenuItemActionPerformed
        JFileChooser ofDialog = new JFileChooser();
        
        if (ofDialog.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            this.showSessionWindow(ofDialog.getSelectedFile().getAbsolutePath());
            
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_openMenuItemActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        showCommWindow();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        for(JInternalFrame x : windowList)
            try {
            x.setIcon(true);
        } catch (PropertyVetoException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        for(JInternalFrame x : windowList)
            try {
            x.setMaximum(false);
            x.setIcon(false);
        } catch (PropertyVetoException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem1ActionPerformed
        this.showDBWindow();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt)//GEN-FIRST:event_formWindowClosing
    {//GEN-HEADEREND:event_formWindowClosing
        DBManager.dbDisconnect();
    }//GEN-LAST:event_formWindowClosing

    public static void main(String args[])
    {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                if ("Nimbus".equals(info.getName()))
                {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }


            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
//            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
//            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
//            javax.swing.UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
        } catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(AppWorkspace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(AppWorkspace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(AppWorkspace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(AppWorkspace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                new AppWorkspace().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    // End of variables declaration//GEN-END:variables
}
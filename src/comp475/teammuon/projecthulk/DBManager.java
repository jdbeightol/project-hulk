package comp475.teammuon.projecthulk;

import java.awt.Cursor;
import java.sql.*;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class DBManager extends javax.swing.JInternalFrame
{
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONN = "jdbc:mysql://localhost:3306/hulk";
    private static final String DB_USER = "comp475";
    private static final String DB_PASS = "hunter2";
    
    private static final String DB_SESSION_INSERT 
            = "INSERT INTO SESSION VALUES(?,?,?,?)";
    
    private static final String DB_EVENT_INSERT
            = "INSERT INTO EVENT VALUES(?,?)";
    
    private static final String DB_EVENTDATA_INSERT
            = "INSERT INTO EVENTDATA (TRIGGERCOUNT1, TRIGGERCOUNT2, PORT, "
            + "EDGERISE, EDGEFALL, GPSSATELLITES, GPSLATENCY, GPSVALID, "
            + "DAQSTATUS, EVENT_ID, MD5HASH) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    
    //@todo Add drop / create table statements.
    
    private static Connection dbConn = null;
    
    private ArrayList<String> hashList;
    
    public DBManager()
    {
        super("Previous DAQ Sessions",true,true,true,true);
        
        initComponents();
        
        dbConnect();
        updateList();
    }
    
    private void dbConnect()
    {
        if (dbConn == null)        
            try
            {
                Class.forName(DBManager.DB_DRIVER);
                dbConn = DriverManager.getConnection(
                        DBManager.DB_CONN, DBManager.DB_USER, DBManager.DB_PASS);
            } catch (Exception e)
            {
                System.out.println(e.getMessage());
                JOptionPane.showMessageDialog(null,
                        "An error occured while connecting to the database.",
                        "Error!", JOptionPane.INFORMATION_MESSAGE);
                dbConn = null;
                this.dispose();
            }
    }
    
    private void updateList()
    {
        DefaultListModel listItems = new DefaultListModel();
        
        try
        {
            if(dbConn != null)
            {
                ResultSet rs = dbConn.createStatement().executeQuery(
                        "select * from SESSION;");

                hashList = new ArrayList<String>();
                jList1.setEnabled(true);
                
                while(rs.next())
                {
                    hashList.add(rs.getString(1));
                    listItems.addElement(rs.getString(2) + " ("
                            + rs.getString(3) + ")");
                }
            }
            
            else
                jList1.setEnabled(false);
        } catch(Exception e)
        {  
            e.getMessage();     
        }

        jList1.setModel(listItems);
        jList1.setSelectedIndex(0);
    }
    
    private void openSession(String hashCode)
    {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            ((AppWorkspace)this.getTopLevelAncestor())
                    .showSessionWindow(dbConn, hashCode);
            
            this.setCursor(Cursor.getDefaultCursor());   
    }
    
    public static void dbDisconnect()
    {
        try
        {
            if(dbConn != null)
                dbConn.close();
        } catch(Exception e)
        {   
            e.getMessage();   
        }

        dbConn = null;
    }
    
    public static void saveSession(final CR_Session crSess)
    {
        (new Thread()
        {
            @Override
            public void run()
            
            {
                try
                {
                    Class.forName(DBManager.DB_DRIVER);
                    Connection saveConn = DriverManager.getConnection(
                            DBManager.DB_CONN, DBManager.DB_USER, 
                            DBManager.DB_PASS);
                    
                    ResultSet rs = saveConn.createStatement().executeQuery(
                            "select FILENAME from SESSION WHERE MD5HASH = '"
                            + crSess.getHash() + "';");
                    
                    if (!rs.next())
                    {
                        System.out.println("Adding " + crSess.getFilename()
                                + " to the database.");
                        saveConn.setAutoCommit(false);                    
                        PreparedStatement sessState = saveConn.prepareStatement(
                                DBManager.DB_SESSION_INSERT);
                        sessState.setString(1, crSess.getHash());
                        sessState.setString(2, crSess.getFilename());
                        sessState.setTimestamp(3, new Timestamp(
                                new java.util.Date().getTime()));
                        sessState.setTimestamp(4, new Timestamp(
                                new java.util.Date().getTime()));
                        sessState.executeUpdate();
                        
                        for (CR_Event x : crSess.getEvents())
                        {
                            PreparedStatement evState 
                                    = saveConn.prepareStatement(
                                    DBManager.DB_EVENT_INSERT);

                            evState.setLong(1, x.getEventID());
                            evState.setString(2, crSess.getHash());
                            
                            evState.executeUpdate();
                            
                            for (CR_EventData y : x.getEvents())
                            {
                                PreparedStatement evDataState 
                                        = saveConn.prepareStatement(
                                        DBManager.DB_EVENTDATA_INSERT);
                                evDataState.setLong(1, 
                                        y.getTriggerCount1().getLong());
                                evDataState.setLong(2, 
                                        y.getTriggerCount2().getLong());
                                evDataState.setInt(3, (int) y.getPort());
                                evDataState.setInt(4, y.getRise().getInt());
                                evDataState.setInt(5, y.getFall().getInt());
                                evDataState.setInt(6, (int) 
                                        y.getSatelliteCount());
                                evDataState.setLong(7, y.getGPSLatency());
                                evDataState.setInt(8, (y.isGPSValid()) ? 1 : 0);
                                evDataState.setInt(9, y.getStatus().getInt());
                                evDataState.setLong(10, x.getEventID());
                                evDataState.setString(11, crSess.getHash());
                                
                                evDataState.executeUpdate();
                            }
                        }
                        
                        saveConn.commit();
                    }
                    else
                        System.out.println(crSess.getFilename() 
                                + " exists in the database.");
                    
                    saveConn.close();
                    
                } catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

            }
        }).start();
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

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jButton3 = new javax.swing.JButton();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt)
            {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });

        jButton1.setText("Open");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Cancel");
        jButton2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton2ActionPerformed(evt);
            }
        });

        jList1.setModel(new javax.swing.AbstractListModel()
        {
            String[] strings = { "No database connection." };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.addMouseListener(new java.awt.event.MouseAdapter()
        {
            public void mouseClicked(java.awt.event.MouseEvent evt)
            {
                jList1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jList1);

        jButton3.setText("Refresh");
        jButton3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 274, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing

    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
    {//GEN-HEADEREND:event_jButton2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        this.openSession(hashList.get(this.jList1.getSelectedIndex()));
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        updateList();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jList1MouseClicked(java.awt.event.MouseEvent evt)//GEN-FIRST:event_jList1MouseClicked
    {//GEN-HEADEREND:event_jList1MouseClicked
        if (evt.getClickCount() == 2)
        {
            this.openSession(hashList.get(this.jList1.getSelectedIndex()));
            this.dispose();
        }
    }//GEN-LAST:event_jList1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
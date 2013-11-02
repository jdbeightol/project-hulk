package comp475.teammuon.projecthulk;

import gnu.io.*;
import java.awt.Cursor;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.swing.JFileChooser;

public class CommTerminalForm extends javax.swing.JInternalFrame 
{
    private static final String version = "0.1a";
    
    private String portName;
    private int baud = 115200;
    private int dataBits = SerialPort.DATABITS_8;
    private int parity = SerialPort.PARITY_NONE;
    private int stopBits = SerialPort.STOPBITS_1;
    private int flowControl = SerialPort.FLOWCONTROL_XONXOFF_IN; //Xon/Xoff
    
    private boolean echo = false;
    
    private SerialPort comm;
    private InputStream in;
    private OutputStream out;    
    
    private int reverseSplitHeight;
    
    public CommTerminalForm() 
    {
        super("DAQ Terminal",true,true,true,true);
        
        reverseSplitHeight = 125;
        portName = "";
        comm = null;
        out = null;
        in = null;
        
        initComponents();
        
        this.jTextArea1.setEditable(false);
        this.jTextArea2.setEditable(false);
        this.infoUpdate("DAQ Terminal Version " + version + "\n");
    }
    
    /**
     * Sets the Serial Parameters of the terminal.
     * @param bd - Baud
     * @param db - Data Bits
     * @param pr - Parity
     * @param sb - Stop Bits
     * @param fc - Floc Control
     */
    public void setPortSettings(int bd, int db, int pr, int sb, int fc)
    {
        this.baud = bd;
        this.dataBits = db;
        this.parity = pr;
        this.stopBits = sb;
        this.flowControl = fc;
    }
    
    /**     
     * Sets the serial parameters of the terminal.
     * @param inInt - Integer array in the order of:
     * baud, data bits, parity, stop bits, and flow control.
     */
    public void setPortSettings(int[] inInt)
    {
        this.baud = inInt[0];
        this.dataBits = inInt[1];
        this.parity = inInt[2];
        this.stopBits = inInt[3];
        this.flowControl = inInt[4];        
    }
    
    /** 
     * Returns an int array of parameters in the order of:
     * baud, data bits, parity, stop bits, and flow control.
     */
    public int[] getPortSettings()
    {
        return new int[] 
        {this.baud, this.dataBits, this.parity, this.stopBits, this.flowControl};
    }
    
    public String getPort()
    {
        return this.portName;
    }
    
    private void saveCommLog()
    {
        JFileChooser sfDialog = new JFileChooser();

        sfDialog.setFileFilter(new TXTFileFilter());
        sfDialog.setAcceptAllFileFilterUsed(false);

        if (sfDialog.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            
            try
            {
                PrintWriter fileOut = new PrintWriter(
                        sfDialog.getSelectedFile().getAbsolutePath(), "UTF-8");
                fileOut.print(this.jTextArea1.getText());
                fileOut.close();
            } catch (Exception e)
            {
                this.infoUpdate("[ERROR]" + e.getMessage() + "\n");
            }
            
            finally
            {
                this.setCursor(Cursor.getDefaultCursor());
            }
        }  
    }
    
    /**
     * Connects 
     * @param commPort 
     */
    public void connect(String commPort)
    {
        CommPortIdentifier portID;
        
        this.jMenuItem5.setEnabled(false);
        
        if(this.comm != null)
            this.disconnect();
        
        try
        {
            CommDriver RXTXDriver = (CommDriver) Class.forName(
                    "gnu.io.RXTXCommDriver").newInstance();
            RXTXDriver.initialize();
            this.infoUpdate("[DRIVER] RXTX Driver initialized.\n");
            
            try
            {
                portID = CommPortIdentifier.getPortIdentifier(commPort);
                portName = commPort;

                if (!portID.isCurrentlyOwned())
                {
                    comm = (SerialPort) portID.open(
                            this.getClass().getName(), 5000);

                    try
                    {
                        this.infoUpdate("[CONTROL] Connecting to "
                                + this.portName +  " with settings: "
                                + this.baud + " b/s, "
                                + this.dataBits + " data bits, "
                                + this.parity + " parity bits, "
                                + this.stopBits + " stop bit(s), and "
                                + this.flowControl + " flow control\n");
                        
                        comm.setSerialPortParams(
                                baud, dataBits, stopBits, parity);

                        out = comm.getOutputStream();

                        comm.addEventListener(new sRead(
                                in = comm.getInputStream(), this));
                        comm.notifyOnDataAvailable(true);

                        this.infoUpdate("[CONTROL] " + portName + " opened.\n");
                        this.jMenuItem5.setEnabled(true);
                        this.jTextArea1.setEnabled(true);
                    } catch (Exception e)
                    {
                        this.infoUpdate("[ERROR] Failed to open stream. ("
                                + e.getMessage() + ")\n");
                        
                        this.disconnect();
                    }
                    
                } else
                    this.infoUpdate("[ERROR] The port is currently owned by "
                            + portID.getCurrentOwner() + "\n");

            } catch (Exception e)
            {
                this.infoUpdate("[ERROR] Failed to identify port. ("
                        + e.getLocalizedMessage() + ")\n");
            }
            
        } catch (Exception e)
        {
            this.infoUpdate("[DRIVER] Error loading RXTX Driver. ("
                    + e.getMessage() + ")\n");
        }
    }
    
    public void disconnect()
    {
        try
        {
            if(out != null)
            {
                out.close();
                out = null;
            }
            
            if(in != null)
            {
                in.close();
                in = null;
            }
            
            this.jTextArea1.setEnabled(false);        
        } catch (Exception e)
        {
            this.infoUpdate("[ERROR] Failed to close stream. ("
                    + e.getMessage() + ")\n");
        }
        
        if(comm != null)
        {
            comm.close();
            comm = null;
            this.infoUpdate("[CONTROL] " + portName + " closed.\n");
            System.out.println("[CONTROL] " + portName + " closed.\n");            
        }
        
        else
            this.infoUpdate("[ERROR] No connection to close.\n");
    }
    
    private void termUpdate(String str)
    {
        this.jTextArea1.append(str);
        this.jTextArea1.setCaretPosition(this.jTextArea1.getText().length());
    }
    
    private void infoUpdate(String str)
    {
        this.jTextArea2.append(str);
        this.jTextArea2.setCaretPosition(this.jTextArea2.getText().length());
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

        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        jMenuItem5 = new javax.swing.JMenuItem();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener()
        {
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
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt)
            {
            }
        });
        addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                formComponentResized(evt);
            }
        });

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setToolTipText("");

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setEnabled(false);
        jTextArea1.addKeyListener(new java.awt.event.KeyAdapter()
        {
            public void keyPressed(java.awt.event.KeyEvent evt)
            {
                jTextArea1KeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTextArea1);

        jSplitPane1.setLeftComponent(jScrollPane1);

        jScrollPane2.addComponentListener(new java.awt.event.ComponentAdapter()
        {
            public void componentResized(java.awt.event.ComponentEvent evt)
            {
                jScrollPane2ComponentResized(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jTextArea2.setToolTipText("");
        jTextArea2.setFocusable(false);
        jScrollPane2.setViewportView(jTextArea2);

        jSplitPane1.setRightComponent(jScrollPane2);

        jMenu1.setText("File");

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Save As...");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem4);
        jMenu1.add(jSeparator1);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Close");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText("Copy");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText("Select All");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);
        jMenu2.add(jSeparator3);

        jCheckBoxMenuItem1.setText("Echo");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jCheckBoxMenuItem1);

        jMenuBar1.add(jMenu2);

        jMenu3.setText("Call");

        jMenuItem2.setText("Connect...");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        jMenuItem3.setText("Disconnect");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem3);
        jMenu3.add(jSeparator2);

        jMenuItem5.setText("Reconnect");
        jMenuItem5.setBorderPainted(false);
        jMenuItem5.setEnabled(false);
        jMenuItem5.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuBar1.add(jMenu3);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 724, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 401, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem2ActionPerformed
        ((AppWorkspace)this.getTopLevelAncestor()).showCommSettings(this);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem3ActionPerformed
        this.disconnect();
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt)//GEN-FIRST:event_formInternalFrameClosing
    {//GEN-HEADEREND:event_formInternalFrameClosing
        this.disconnect();
    }//GEN-LAST:event_formInternalFrameClosing

    private void jTextArea1KeyPressed(java.awt.event.KeyEvent evt)//GEN-FIRST:event_jTextArea1KeyPressed
    {//GEN-HEADEREND:event_jTextArea1KeyPressed
        try
        {
            if(this.out != null 
                    && evt.getKeyCode() != java.awt.event.KeyEvent.VK_SHIFT 
                    && evt.getKeyCode() != java.awt.event.KeyEvent.VK_CONTROL
                    && evt.getKeyCode() != java.awt.event.KeyEvent.VK_META 
                    && evt.getKeyCode() != java.awt.event.KeyEvent.VK_ALT 
                    && evt.getKeyCode() != java.awt.event.KeyEvent.VK_WINDOWS)
            {
                this.out.write(evt.getKeyChar());
                
                if(this.echo)
                {
                    if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_BACK_SPACE
                            && !jTextArea1.getDocument().getText(
                            jTextArea1.getDocument().getLength()-1, 1).equals("\n"))
                        jTextArea1.getDocument().remove(
                                jTextArea1.getDocument().getLength()-1, 1);
                    
                    else
                        termUpdate(Character.toString(evt.getKeyChar()));
                }
            }
            
        } catch (Exception e)
        {
            this.infoUpdate("[ERROR] Error writing to stream. ("
                    + e.getMessage() + ")\n");
            this.disconnect();
        }
    }//GEN-LAST:event_jTextArea1KeyPressed

    private void formComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_formComponentResized
    {//GEN-HEADEREND:event_formComponentResized
        this.jSplitPane1.setDividerLocation(
                (int)this.jSplitPane1.getSize().getHeight() 
                - this.reverseSplitHeight);
    }//GEN-LAST:event_formComponentResized

    private void jScrollPane2ComponentResized(java.awt.event.ComponentEvent evt)//GEN-FIRST:event_jScrollPane2ComponentResized
    {//GEN-HEADEREND:event_jScrollPane2ComponentResized
        this.reverseSplitHeight = this.jSplitPane1.getHeight() 
                - this.jSplitPane1.getDividerLocation();
    }//GEN-LAST:event_jScrollPane2ComponentResized

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem4ActionPerformed
        this.saveCommLog();
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
    {//GEN-HEADEREND:event_jCheckBoxMenuItem1ActionPerformed
        this.echo = this.jCheckBoxMenuItem1.getState();
    }//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem5ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem5ActionPerformed
        if(!portName.equals(""))
            this.connect(portName);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem6ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem6ActionPerformed
        new ClipboardManager().setContents(this.jTextArea1.getSelectedText());
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem7ActionPerformed
    {//GEN-HEADEREND:event_jMenuItem7ActionPerformed
        this.jTextArea1.setSelectionStart(0);
        this.jTextArea1.setSelectionEnd(this.jTextArea1.getText().length());
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    // <editor-fold defaultstate="collapsed" desc="SerialPort Reader Class">                          
    public static class sRead implements SerialPortEventListener
    {
        byte[] buffer = new byte[1024];
        CommTerminalForm commForm;
        InputStream in;
        
        public sRead(final InputStream i, final CommTerminalForm t)
        {
            this.commForm = t;
            this.in = i;
        }
        
        @Override
        public void serialEvent(SerialPortEvent arg0)
        {
            int len = 0;
            int data;
            
            try
            {
                while ((data = in.read()) > -1)
                {
                    buffer[len++] = (byte)data;

                    if (data == '\n')
                        break;
                }
                
            } catch (Exception e)
            {
                commForm.infoUpdate("[ERROR] Error reading stream. (" 
                        + e.getMessage() + ")\n");
                commForm.disconnect();
            }
            
            finally
            {
                commForm.termUpdate(new String(buffer,0,len));
            }
        }
    }    
    // </editor-fold>
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
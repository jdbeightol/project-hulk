package comp475.teammuon.projecthulk;

import gnu.io.*;
import java.util.ArrayList;

public class CommSetupForm extends javax.swing.JInternalFrame
{
    //Serial Port Parameters.
    private static final int[] BAUD_LIST = {110, 300, 1200, 2400, 4800, 9600, 
        19200, 38400, 57600, 115200, 230400, 460800, 921600};
    private static final int[] DATA_BIT_LIST = {SerialPort.DATABITS_5, 
        SerialPort.DATABITS_6, SerialPort.DATABITS_7, SerialPort.DATABITS_8};
    private static final int[] PARITY_LIST = {SerialPort.PARITY_EVEN, 
        SerialPort.PARITY_ODD, SerialPort.PARITY_NONE, SerialPort.PARITY_MARK,
        SerialPort.PARITY_SPACE};
    private static final int[] STOP_BIT_LIST = {SerialPort.STOPBITS_1, 
        SerialPort.STOPBITS_1_5, SerialPort.STOPBITS_2};
    private static final int[] FLOW_CONTROL_LIST = {
        SerialPort.FLOWCONTROL_XONXOFF_IN, SerialPort.FLOWCONTROL_RTSCTS_IN, 
        SerialPort.FLOWCONTROL_NONE};
    
    private CommTerminalForm owner;
    
    public CommSetupForm(CommTerminalForm in)
    {
        initComponents();
        
        this.owner = in;
        this.getSettings();
        
        for(CommPortIdentifier x : getSerialPortList())
            this.jComboBox1.addItem(x.getName());
    }
    
    private ArrayList<CommPortIdentifier> getSerialPortList()
    {
        ArrayList<CommPortIdentifier> portList 
                = new ArrayList<CommPortIdentifier>();
        java.util.Enumeration<CommPortIdentifier> portEnum 
                = CommPortIdentifier.getPortIdentifiers();
        
        while(portEnum.hasMoreElements())
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            
            if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL)
                portList.add(portIdentifier);
        }
        
        return portList;
    }   
    
    private int getBaud()
    {
        return CommSetupForm.BAUD_LIST[this.jComboBox2.getSelectedIndex()];
    }
    
    private int getDataBits()
    {
        return CommSetupForm.DATA_BIT_LIST[this.jComboBox3.getSelectedIndex()];
    }
    
    private int getParity()
    {
        return CommSetupForm.PARITY_LIST[this.jComboBox4.getSelectedIndex()];
    }
    
    private int getStopBits()
    {
        return CommSetupForm.STOP_BIT_LIST[this.jComboBox5.getSelectedIndex()];
    }
    
    private int getFlowControl()
    {
        return CommSetupForm.FLOW_CONTROL_LIST[
                this.jComboBox6.getSelectedIndex()];
    }
    
    private void setBaud(int in)
    {
        for(int x = 0;x<CommSetupForm.BAUD_LIST.length;x++)
            if(CommSetupForm.BAUD_LIST[x] == in)
                this.jComboBox2.setSelectedIndex(x);
    }

    private void setDataBits(int in)
    {
        for (int x = 0; x < CommSetupForm.DATA_BIT_LIST.length; x++)
            if (CommSetupForm.DATA_BIT_LIST[x] == in)
                this.jComboBox3.setSelectedIndex(x);
    }

    private void setParity(int in)
    {
        for (int x = 0; x < CommSetupForm.PARITY_LIST.length; x++)
            if (CommSetupForm.PARITY_LIST[x] == in)
                this.jComboBox4.setSelectedIndex(x);
    }

    private void setStopBits(int in)
    {
        for(int x = 0;x<CommSetupForm.STOP_BIT_LIST.length;x++)
            if (CommSetupForm.STOP_BIT_LIST[x] == in)
                this.jComboBox5.setSelectedIndex(x);
    }    
    
    private void setFlowControl(int in)
    {
        for(int x = 0;x<CommSetupForm.FLOW_CONTROL_LIST.length;x++)
            if (CommSetupForm.FLOW_CONTROL_LIST[x] == in)
                this.jComboBox6.setSelectedIndex(x);
    }
   
    private void getSettings()
    {
        int[] settingArray = this.owner.getPortSettings();

        this.setBaud(settingArray[0]);
        this.setDataBits(settingArray[1]);
        this.setParity(settingArray[2]);
        this.setStopBits(settingArray[3]);
        this.setFlowControl(settingArray[4]);
 //       this.jTextField1.setText(this.owner.getPort());
    } 
    
    private void setSettings()
    {
        this.owner.setPortSettings(getBaud(), getDataBits(), getParity(), 
                getStopBits(), getFlowControl());
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

        jFrame1 = new javax.swing.JFrame();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jComboBox3 = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox5 = new javax.swing.JComboBox();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jButton3 = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox();

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        setTitle("Serial Connection");

        jLabel2.setText("Comm Port");

        jLabel3.setText("Baud");

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "110", "300", "1200", "2400", "4800", "9600", "19200", "38400", "57600", "115200", "230400", "460800", "921600" }));
        jComboBox2.setSelectedIndex(5);
        jComboBox2.setToolTipText("");

        jLabel4.setText("Data Bits");

        jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5", "6", "7", "8" }));
        jComboBox3.setSelectedIndex(3);
        jComboBox3.setToolTipText("");

        jLabel5.setText("Parity");

        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Even", "Odd", "None", "Mark", "Space" }));
        jComboBox4.setSelectedIndex(2);

        jLabel6.setText("Stop Bits");

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "1.5", "2" }));

        jButton1.setText("Connect");
        jButton1.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Flow Control");

        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Xon / Xoff", "Hardware", "None" }));
        jComboBox6.setSelectedIndex(2);

        jButton3.setText("Cancel");
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox2, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox4, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jComboBox6, 0, 190, Short.MAX_VALUE)
                            .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton3))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
    {//GEN-HEADEREND:event_jButton1ActionPerformed
        this.setSettings();
        this.owner.connect((String)this.jComboBox1.getSelectedItem());
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
    {//GEN-HEADEREND:event_jButton3ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    // End of variables declaration//GEN-END:variables
}
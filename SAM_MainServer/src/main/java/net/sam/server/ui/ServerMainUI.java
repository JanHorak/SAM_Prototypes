package net.sam.server.ui;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.manager.FileManager;
import net.sam.server.servermain.Server;
import net.sam.server.utilities.Utilities;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;


/**
 *
 * @author janhorak
 */
public class ServerMainUI extends javax.swing.JFrame {

    /**
     * Creates new form ServerMainUI
     */
    public ServerMainUI() {
        initComponents();
        BasicConfigurator.configure();
        logger = Logger.getLogger(ServerMainUI.class);

        this.setTitle("SAM - SecureAndroidMessenger - Server");
        lb_logo.setIcon(new ImageIcon("src/main/resources/graphics/simpleLogoSAM.png"));
        
        Properties serverprops = FileManager.initProperties(SERVERPROPERTIES);
        chk_logging.setSelected(Boolean.getBoolean(serverprops.getProperty("logging")));
        
        serverMainBean = ServerMainBean.getInstance();

        // Loading of the UIThread
        uiThread = new UIUpdateThread(list_members);
        uiThread.start();

        // Get wrapped Lists from Singleton for UI
        ui_registeredInUsers = ServerMainBean.wrapForUI(serverMainBean.getRegisteredMembers());
        ui_loggedInUsers = ServerMainBean.wrapForUI(serverMainBean.getloggedInMembers());

        // ---- UI Settings ------
        ButtonGroup bg = new ButtonGroup();
        bg.add(radio_membersOnline);
        bg.add(radio_membersRegistered);

        radio_membersRegistered.doClick();
        logger.info(Utilities.getLogTime()+" UI loaded successfully");
    }

    // ---------- Variables ----------------
    private Server server;

    private ServerMainBean serverMainBean;

    private UIUpdateThread uiThread;

    private List<String> ui_loggedInUsers;

    private List<String> ui_registeredInUsers;

    private Logger logger;
    
    private static final String SERVERPROPERTIES = "src/main/resources/server.properties";
    
    private static final String LOGGINGPROPERTIES = "src/main/resources/log4j.properties";

    // ---------- ---------- ----------------
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lb_serverIP = new javax.swing.JLabel();
        lb_serverPORT = new javax.swing.JLabel();
        lb_serverMAXCON = new javax.swing.JLabel();
        tf_serverIP = new javax.swing.JTextField();
        tf_serverPORT = new javax.swing.JTextField();
        chk_defaultIP = new javax.swing.JCheckBox();
        chk_defaultPORT = new javax.swing.JCheckBox();
        chk_defaultMAXCON = new javax.swing.JCheckBox();
        spr_serverMAXCON = new javax.swing.JSpinner();
        jSeparator1 = new javax.swing.JSeparator();
        tgl_StartServer = new javax.swing.JToggleButton();
        pnl_messanges = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ta_messanges = new javax.swing.JTextArea();
        tf_singleMessange = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        lb_logo = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        chk_logging = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_members = new javax.swing.JList();
        radio_membersOnline = new javax.swing.JRadioButton();
        radio_membersRegistered = new javax.swing.JRadioButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lb_serverIP.setText("ServerIP:");

        lb_serverPORT.setText("ServerPort:");

        lb_serverMAXCON.setText("MaximumConnections:");

        tf_serverIP.setText("127.0.0.1");
        tf_serverIP.setEnabled(false);

        tf_serverPORT.setText("2222");
        tf_serverPORT.setEnabled(false);

        chk_defaultIP.setSelected(true);
        chk_defaultIP.setText("default");
        chk_defaultIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_defaultIPActionPerformed(evt);
            }
        });

        chk_defaultPORT.setSelected(true);
        chk_defaultPORT.setText("default");
        chk_defaultPORT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_defaultPORTActionPerformed(evt);
            }
        });

        chk_defaultMAXCON.setSelected(true);
        chk_defaultMAXCON.setText("default");
        chk_defaultMAXCON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_defaultMAXCONActionPerformed(evt);
            }
        });

        spr_serverMAXCON.setEnabled(false);
        spr_serverMAXCON.setValue(8);
        spr_serverMAXCON.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spr_serverMAXCONStateChanged(evt);
            }
        });

        tgl_StartServer.setText("Start Server");
        tgl_StartServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgl_StartServerActionPerformed(evt);
            }
        });

        pnl_messanges.setBorder(javax.swing.BorderFactory.createTitledBorder("Messages"));

        ta_messanges.setColumns(20);
        ta_messanges.setRows(5);
        jScrollPane1.setViewportView(ta_messanges);

        javax.swing.GroupLayout pnl_messangesLayout = new javax.swing.GroupLayout(pnl_messanges);
        pnl_messanges.setLayout(pnl_messangesLayout);
        pnl_messangesLayout.setHorizontalGroup(
            pnl_messangesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        pnl_messangesLayout.setVerticalGroup(
            pnl_messangesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
        );

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        chk_logging.setSelected(true);
        chk_logging.setText("Save logging in File");
        chk_logging.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_loggingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chk_logging)
                .addContainerGap(73, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chk_logging)
                .addContainerGap(92, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Settings", jPanel1);

        list_members.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(list_members);

        radio_membersOnline.setText("online");
        radio_membersOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_membersOnlineActionPerformed(evt);
            }
        });

        radio_membersRegistered.setSelected(true);
        radio_membersRegistered.setText("registered");
        radio_membersRegistered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radio_membersRegisteredActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(radio_membersOnline)
                    .addComponent(radio_membersRegistered)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radio_membersRegistered)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radio_membersOnline)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Users", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_messanges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tf_singleMessange)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lb_logo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tgl_StartServer))
                            .addComponent(jSeparator1)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lb_serverMAXCON)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(lb_serverIP)
                                        .addComponent(lb_serverPORT))
                                    .addGap(103, 103, 103)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(tf_serverIP, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(chk_defaultIP))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(tf_serverPORT, javax.swing.GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                                .addComponent(spr_serverMAXCON))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(chk_defaultMAXCON)
                                                .addComponent(chk_defaultPORT)))))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTabbedPane1)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_serverIP)
                            .addComponent(tf_serverIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chk_defaultIP))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_serverPORT)
                            .addComponent(tf_serverPORT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(chk_defaultPORT))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lb_serverMAXCON)
                            .addComponent(chk_defaultMAXCON)
                            .addComponent(spr_serverMAXCON, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 6, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tgl_StartServer)
                            .addComponent(lb_logo)))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnl_messanges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_singleMessange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_send))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // ---------- Methods -----------------
    //@TODO: Refactoring of the Radio- buttons!!
    
    
    // -------------------------------------

    private void chk_defaultPORTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_defaultPORTActionPerformed
        if (chk_defaultPORT.isSelected()) {
            tf_serverPORT.setText("2222");
            tf_serverPORT.setEnabled(false);
        } else {
            tf_serverPORT.setText("");
            tf_serverPORT.setEnabled(true);
        }
    }//GEN-LAST:event_chk_defaultPORTActionPerformed

    private void chk_defaultIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_defaultIPActionPerformed
        if (chk_defaultIP.isSelected()) {
            tf_serverIP.setText("127.0.0.1");
            tf_serverIP.setEnabled(false);
        } else {
            tf_serverIP.setText("");
            tf_serverIP.setEnabled(true);
        }
    }//GEN-LAST:event_chk_defaultIPActionPerformed

    private void chk_defaultMAXCONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_defaultMAXCONActionPerformed
        if (chk_defaultMAXCON.isSelected()) {
            spr_serverMAXCON.setValue(8);
            spr_serverMAXCON.setEnabled(false);
        } else {
            spr_serverMAXCON.setValue(0);
            spr_serverMAXCON.setEnabled(true);
        }
    }//GEN-LAST:event_chk_defaultMAXCONActionPerformed

    private void spr_serverMAXCONStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spr_serverMAXCONStateChanged
        if ((int) spr_serverMAXCON.getValue() < 0) {
            spr_serverMAXCON.setValue(0);
        }
        //@TODO: Upper range?
    }//GEN-LAST:event_spr_serverMAXCONStateChanged

    private void tgl_StartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_StartServerActionPerformed
        if (tgl_StartServer.isSelected()) {
            server = new Server(ta_messanges);
            ta_messanges.append(Utilities.getLogTime() + " Server started...");
            tgl_StartServer.setText("Stop Server");
        } else {
            try {
                server.shutDown();
                ta_messanges.append("\n" + Utilities.getLogTime() + " Server stopped!");
                tgl_StartServer.setText("Start Server");
            } catch (IOException ex) {

            }
        }
    }//GEN-LAST:event_tgl_StartServerActionPerformed

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        
    }//GEN-LAST:event_btn_sendActionPerformed

    private void radio_membersOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_membersOnlineActionPerformed
        DefaultListModel lm = new DefaultListModel();
        if (!serverMainBean.getloggedInMembers().isEmpty()) {
            List<String> loggedInFormatted = ServerMainBean.wrapForUI(serverMainBean.getloggedInMembers());
            for (String s : loggedInFormatted) {
                lm.addElement(s);
            }
        } else {
            lm.addElement("No one here!");
        }
        this.list_members.setModel(lm);
    }//GEN-LAST:event_radio_membersOnlineActionPerformed

    private void radio_membersRegisteredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radio_membersRegisteredActionPerformed
        DefaultListModel lm = new DefaultListModel();
        if (!serverMainBean.getRegisteredMembers().isEmpty()) {
            List<String> loggedInFormatted = ServerMainBean.wrapForUI(serverMainBean.getRegisteredMembers());
            for (String s : loggedInFormatted) {
                lm.addElement(s);
            }
        } else {
            lm.addElement("No one registered!");
        }
        this.list_members.setModel(lm);
    }//GEN-LAST:event_radio_membersRegisteredActionPerformed

    private void chk_loggingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_loggingActionPerformed
        if (chk_logging.isSelected()){
            FileManager.storeValueInPropertiesFile(SERVERPROPERTIES, "logging", "true");
            FileManager.storeValueInPropertiesFile(LOGGINGPROPERTIES, "log4j.rootLogger", "info, file");
        } else {
            FileManager.storeValueInPropertiesFile(SERVERPROPERTIES, "logging", "false");
            FileManager.storeValueInPropertiesFile(LOGGINGPROPERTIES, "log4j.rootLogger", "\"\"");
        }
    }//GEN-LAST:event_chk_loggingActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ServerMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerMainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerMainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_send;
    private javax.swing.JCheckBox chk_defaultIP;
    private javax.swing.JCheckBox chk_defaultMAXCON;
    private javax.swing.JCheckBox chk_defaultPORT;
    private javax.swing.JCheckBox chk_logging;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lb_logo;
    private javax.swing.JLabel lb_serverIP;
    private javax.swing.JLabel lb_serverMAXCON;
    private javax.swing.JLabel lb_serverPORT;
    private javax.swing.JList list_members;
    private javax.swing.JPanel pnl_messanges;
    private javax.swing.JRadioButton radio_membersOnline;
    private javax.swing.JRadioButton radio_membersRegistered;
    private javax.swing.JSpinner spr_serverMAXCON;
    public javax.swing.JTextArea ta_messanges;
    private javax.swing.JTextField tf_serverIP;
    private javax.swing.JTextField tf_serverPORT;
    private javax.swing.JTextField tf_singleMessange;
    private javax.swing.JToggleButton tgl_StartServer;
    // End of variables declaration//GEN-END:variables
}

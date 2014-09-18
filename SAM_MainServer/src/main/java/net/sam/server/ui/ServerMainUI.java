package net.sam.server.ui;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import net.sam.server.beans.ServerMainBean;
import net.sam.server.entities.ServerConfig;
import net.sam.server.manager.DataAccess;
import net.sam.server.servermain.Server;
import net.sam.server.services.ContainerService;
import net.sam.server.services.ServerResoucesPool;
import net.sam.server.ui.components.ServerConfiguration;
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
        ResourcePoolHandler.loadFileResources(ServerResoucesPool.class);

        this.setTitle("SAM - SecureAndroidMessenger - Server");

        ImageIcon samLogo = ResourcePoolHandler.getResource("samLogo");
        lb_logo.setIcon(samLogo);

        loadSettings();

        container = new ContainerService();
        container.startContainer();
        serverMainBean = ServerMainBean.getInstance();

        // Loading of the UIThread
        uiThread = new UIUpdateThread(list_members);
        uiThread.start();

        // Get wrapped Lists from Singleton for UI
        ui_registeredInUsers = ServerMainBean.wrapUserListForUI(serverMainBean.getRegisteredMembers());
        ui_loggedInUsers = ServerMainBean.wrapUserListForUI(serverMainBean.getloggedInMembers());
        ui_configs = serverMainBean.wrapServerConfigListForUI();

        // ---- UI Settings ------
        ButtonGroup bg = new ButtonGroup();
        bg.add(radio_membersOnline);
        bg.add(radio_membersRegistered);

        refreshConfigList();

        radio_membersRegistered.doClick();
        logger.info(Utilities.getLogTime() + " UI loaded successfully");

        serverPropertyPath = ResourcePoolHandler.getPathOfResource("serverProperties");
        loggingPropertyPath = ResourcePoolHandler.getPathOfResource("log4jProperties");

        // Set icons
        ImageIcon editIcon = ResourcePoolHandler.getResource("editConfig");
        btn_editConfig.setIcon(editIcon);
        ImageIcon newIcon = ResourcePoolHandler.getResource("newConfig");
        btn_newConfig.setIcon(newIcon);
        ImageIcon deleteIcon = ResourcePoolHandler.getResource("deleteConfig");
        btn_deleteConfig.setIcon(deleteIcon);
    }

    // ---------- Variables ----------------
    private Server server;

    private ContainerService container;

    private ServerMainBean serverMainBean;

    private UIUpdateThread uiThread;

    private List<String> ui_loggedInUsers;

    private List<String> ui_registeredInUsers;

    private List<String> ui_configs;

    private ServerConfig current;

    private Logger logger;

    private static String serverPropertyPath;

    private static String loggingPropertyPath;

    // ---------- ---------- ----------------
    public void refreshConfigList() {
        DefaultListModel lm = new DefaultListModel();
        serverMainBean = ServerMainBean.getInstance();
        if (!serverMainBean.getConfigs().isEmpty()) {
            List<String> loggedInFormatted = serverMainBean.wrapServerConfigListForUI();
            for (String s : loggedInFormatted) {
                lm.addElement(s);
            }
        }
        list_configurations.setModel(lm);
    }

    private void updateServerConfig(ServerConfig sc) {
        tf_serverIP.setText(sc.getIpaddress());
        tf_serverPORT.setText(String.valueOf(sc.getPort()));
        tf_maximalCons.setText(String.valueOf(sc.getMaximalUsers()));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        chk_saveMessages = new javax.swing.JCheckBox();
        lb_settingKeypair = new javax.swing.JLabel();
        spr_daysKeyPair = new javax.swing.JSpinner();
        chk_memberListPublic = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_members = new javax.swing.JList();
        radio_membersOnline = new javax.swing.JRadioButton();
        radio_membersRegistered = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        tf_serverIP = new javax.swing.JTextField();
        tf_serverPORT = new javax.swing.JTextField();
        lb_registerPW = new javax.swing.JLabel();
        pwf_register = new javax.swing.JPasswordField();
        jScrollPane3 = new javax.swing.JScrollPane();
        list_configurations = new javax.swing.JList();
        btn_deleteConfig = new javax.swing.JButton();
        lb_serverIP = new javax.swing.JLabel();
        btn_editConfig = new javax.swing.JButton();
        btn_newConfig = new javax.swing.JButton();
        tf_maximalCons = new javax.swing.JTextField();
        lb_serverPORT = new javax.swing.JLabel();
        lb_serverMAXCON = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tgl_StartServer.setText("Start Server");
        tgl_StartServer.setEnabled(false);
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
            .addGroup(pnl_messangesLayout.createSequentialGroup()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        pnl_messangesLayout.setVerticalGroup(
            pnl_messangesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnl_messangesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        chk_saveMessages.setText("Save Messages in Buffer");
        chk_saveMessages.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_saveMessagesActionPerformed(evt);
            }
        });

        lb_settingKeypair.setText("Recreate Keypair in (days)");

        spr_daysKeyPair.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spr_daysKeyPairStateChanged(evt);
            }
        });

        chk_memberListPublic.setText("Memberlist is public");
        chk_memberListPublic.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_memberListPublicActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chk_logging)
                    .addComponent(chk_saveMessages)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lb_settingKeypair)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spr_daysKeyPair, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(chk_memberListPublic))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chk_logging)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chk_saveMessages)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_settingKeypair)
                    .addComponent(spr_daysKeyPair, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chk_memberListPublic)
                .addContainerGap(48, Short.MAX_VALUE))
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
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
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
                .addContainerGap(95, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Users", jPanel2);

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Serverconfiguration"));

        tf_serverIP.setEnabled(false);

        tf_serverPORT.setEnabled(false);

        lb_registerPW.setText("Register-password:");

        pwf_register.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                pwf_registerKeyTyped(evt);
            }
        });

        list_configurations.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list_configurations.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_configurationsValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(list_configurations);

        btn_deleteConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteConfigActionPerformed(evt);
            }
        });

        lb_serverIP.setText("ServerIP:");

        btn_editConfig.setEnabled(false);
        btn_editConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_editConfigActionPerformed(evt);
            }
        });

        btn_newConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_newConfigActionPerformed(evt);
            }
        });

        tf_maximalCons.setEnabled(false);

        lb_serverPORT.setText("ServerPort:");

        lb_serverMAXCON.setText("MaximumConnections:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(btn_newConfig)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btn_editConfig)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_deleteConfig))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lb_serverMAXCON)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tf_maximalCons))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lb_serverIP)
                                .addGap(13, 13, 13)
                                .addComponent(tf_serverIP, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_serverPORT)
                            .addComponent(lb_registerPW))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(tf_serverPORT, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pwf_register, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btn_editConfig)
                            .addComponent(btn_newConfig)
                            .addComponent(btn_deleteConfig))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_serverIP)
                    .addComponent(tf_serverIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tf_serverPORT, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lb_serverPORT))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_registerPW)
                        .addComponent(pwf_register, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lb_serverMAXCON)
                        .addComponent(tf_maximalCons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

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
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(lb_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tgl_StartServer))
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tgl_StartServer)
                            .addComponent(lb_logo, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(8, 8, 8))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPane1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(pnl_messanges, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
    /**
     * This method loads the Data from the server.properties and parses the
     * values to the UI- components.
     *
     */
    private void loadSettings() {
        Properties serverprops = ResourcePoolHandler.getResource("serverProperties");

        chk_logging.setSelected(Boolean.parseBoolean(serverprops.getProperty("LOGGING")));
        chk_memberListPublic.setSelected(Boolean.parseBoolean(serverprops.getProperty("MEMBERLISTISPUBLIC")));
        chk_saveMessages.setSelected(Boolean.parseBoolean(serverprops.getProperty("SAVEMESSAGESINBUFFER")));
    }

    private void tgl_StartServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_StartServerActionPerformed
        // Get MD5- Hash from Serverpassword
        String md5 = Utilities.getHash(pwf_register.getText());
        serverMainBean.setServerPassword(md5);

        // Save the settings for key-recreation
        ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "DAYSFORKEYRECREATION", String.valueOf(spr_daysKeyPair.getValue()));

        if (tgl_StartServer.isSelected()) {
            server = new Server(ta_messanges);
            ta_messanges.append("\n" + Utilities.getLogTime() + " Server started...");
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
        serverMainBean = ServerMainBean.getInstance();
        if (!serverMainBean.getloggedInMembers().isEmpty()) {
            List<String> loggedInFormatted = ServerMainBean.wrapUserListForUI(serverMainBean.getloggedInMembers());
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
        serverMainBean = ServerMainBean.getInstance();
        if (!serverMainBean.getRegisteredMembers().isEmpty()) {
            List<String> loggedInFormatted = ServerMainBean.wrapUserListForUI(serverMainBean.getRegisteredMembers());
            for (String s : loggedInFormatted) {
                lm.addElement(s);
            }
        } else {
            lm.addElement("No one registered!");
        }
        this.list_members.setModel(lm);
    }//GEN-LAST:event_radio_membersRegisteredActionPerformed

    private void chk_loggingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_loggingActionPerformed
        if (chk_logging.isSelected()) {
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "LOGGING", "true");
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "log4j.rootLogger", "info, file");
        } else {
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "LOGGING", "false");
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "log4j.rootLogger", "\"\"");
        }
    }//GEN-LAST:event_chk_loggingActionPerformed

    private void chk_memberListPublicActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_memberListPublicActionPerformed
        if (chk_memberListPublic.isSelected()) {
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "MEMBERLISTISPUBLIC", "true");
        } else {
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "MEMBERLISTISPUBLIC", "false");
        }
    }//GEN-LAST:event_chk_memberListPublicActionPerformed

    private void chk_saveMessagesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_saveMessagesActionPerformed
        if (chk_saveMessages.isSelected()) {
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "SAVEMESSAGESINBUFFER", "true");
        } else {
            ResourcePoolHandler.PropertiesHelper.setValueInProperties("serverProperties", "SAVEMESSAGESINBUFFER", "false");
        }
    }//GEN-LAST:event_chk_saveMessagesActionPerformed

    private void spr_daysKeyPairStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spr_daysKeyPairStateChanged
        if ((int) spr_daysKeyPair.getValue() < 0) {
            spr_daysKeyPair.setValue(0);
        }
    }//GEN-LAST:event_spr_daysKeyPairStateChanged

    private void pwf_registerKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pwf_registerKeyTyped
        if (pwf_register.getPassword().length > 0) {
            tgl_StartServer.setEnabled(true);
        } else {
            tgl_StartServer.setEnabled(false);
        }
    }//GEN-LAST:event_pwf_registerKeyTyped

    private void btn_newConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_newConfigActionPerformed
        new ServerConfiguration(this).setVisible(true);
    }//GEN-LAST:event_btn_newConfigActionPerformed

    private void btn_editConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_editConfigActionPerformed
        new ServerConfiguration(this, current).setVisible(true);
    }//GEN-LAST:event_btn_editConfigActionPerformed

    private void list_configurationsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_configurationsValueChanged
        if (list_configurations.isSelectionEmpty()) {
            btn_editConfig.setEnabled(false);
        } else {
            current = serverMainBean.getConfigByName(list_configurations.getSelectedValue().toString());
            serverMainBean.setCurrentConfig(current);
            updateServerConfig(current);
            btn_editConfig.setEnabled(true);
        }
    }//GEN-LAST:event_list_configurationsValueChanged

    private void btn_deleteConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteConfigActionPerformed
        int delete = -5;
        if (current.isDeleteable()) {
            delete = JOptionPane.showConfirmDialog(this, "Do you want to delete " + current.getName(), "Confirm", JOptionPane.YES_NO_OPTION);
            if (delete == JOptionPane.YES_OPTION) {
                DataAccess.deleteServerConfig(current);
                ServerMainBean.reloadAllConfigs();
                refreshConfigList();
            }
        } else {
            JOptionPane.showMessageDialog(this, current.getName() + " cannot be deleted. It is default.", "Information", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btn_deleteConfigActionPerformed

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
    private javax.swing.JButton btn_deleteConfig;
    private javax.swing.JButton btn_editConfig;
    private javax.swing.JButton btn_newConfig;
    private javax.swing.JButton btn_send;
    private javax.swing.JCheckBox chk_logging;
    private javax.swing.JCheckBox chk_memberListPublic;
    private javax.swing.JCheckBox chk_saveMessages;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lb_logo;
    private javax.swing.JLabel lb_registerPW;
    private javax.swing.JLabel lb_serverIP;
    private javax.swing.JLabel lb_serverMAXCON;
    private javax.swing.JLabel lb_serverPORT;
    private javax.swing.JLabel lb_settingKeypair;
    private javax.swing.JList list_configurations;
    private javax.swing.JList list_members;
    private javax.swing.JPanel pnl_messanges;
    private javax.swing.JPasswordField pwf_register;
    private javax.swing.JRadioButton radio_membersOnline;
    private javax.swing.JRadioButton radio_membersRegistered;
    private javax.swing.JSpinner spr_daysKeyPair;
    public javax.swing.JTextArea ta_messanges;
    private javax.swing.JTextField tf_maximalCons;
    private javax.swing.JTextField tf_serverIP;
    private javax.swing.JTextField tf_serverPORT;
    private javax.swing.JTextField tf_singleMessange;
    private javax.swing.JToggleButton tgl_StartServer;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sam_testclient.ui.components;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.apache.log4j.Logger;
import sam_testclient.beans.ClientMainBean;
import sam_testclient.communication.Client;
import sam_testclient.entities.MemberSettings;
import sam_testclient.exceptions.InvalidSettingsException;
import sam_testclient.sources.FileManager;
import sam_testclient.ui.main.MainUI;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class SettingsFrame extends javax.swing.JInternalFrame {

    /**
     * Creates new form SettingsFrame
     */
    private Client client;
    private MainUI ui;
    private JTextField nameField;
    private JTextArea area;
    
    private Logger logger;

    private ClientMainBean cmb;

    private final String CLIENTPROPERTIES = "resources/properties/client.properties";

    public SettingsFrame(Client client, MainUI ui, JTextField nameField, JTextArea area) {
        initComponents();
        this.client = client;
        this.ui = ui;
        this.nameField = nameField;
        this.area = area;
        this.cmb = ClientMainBean.getInstance();
        this.setClosable(true);
        logger = org.apache.log4j.Logger.getLogger(SettingsFrame.class);
        
        ButtonGroup recreationGroup = new ButtonGroup();
        recreationGroup.add(rd_atLogin);
        recreationGroup.add(rd_inDays);
        recreationGroup.add(rd_listenToServer);

        ButtonGroup autoDownload = new ButtonGroup();
        autoDownload.add(rd_yes);
        autoDownload.add(rd_ask);

        ButtonGroup valid = new ButtonGroup();
        valid.add(rd_mobile);
        valid.add(rd_wlan);

        prepareUI();
    }

    private void prepareUI() {
        MemberSettings settings = null;
        try {
            settings = FileManager.getMemberSettings(CLIENTPROPERTIES);
        } catch (InvalidSettingsException ex) {
            logger.error("Validation of Settings are failed: " + ex);
        }

        lb_avatar.setIcon(new ImageIcon(settings.getAvatarPath()));

        if (settings.getRecreationType() == MemberSettings.RecreationEnum.AT_LOGIN) {
            rd_atLogin.setSelected(true);
        } else if (settings.getRecreationType() == MemberSettings.RecreationEnum.AT_SERVER) {
            rd_listenToServer.setSelected(true);
        } else if (settings.getRecreationType() == MemberSettings.RecreationEnum.BY_DAYS) {
            rd_inDays.setSelected(true);
            spn_days.setEnabled(true);
        }

        spn_days.setValue(settings.getRecreationDays());

        chk_allowWebClients.setSelected(settings.isAllowWebClients());

        chk_saveLocaleHist.setSelected(settings.isSaveLocaleHistory());
        
        spn_KByte.setValue(Integer.decode(settings.getHistBorder()));
        
        lb_wordamaount.setText("(about "+calcWords((int) spn_KByte.getValue())+" words)");

        tf_name.setText(settings.getName());

        if (settings.getAutoDownload() == MemberSettings.AutoDownload.YES) {
            rd_ask.setSelected(true);
        } else if (settings.getAutoDownload() == MemberSettings.AutoDownload.ASK) {
            rd_mobile.setSelected(true);
        }

        if (settings.getValidFor() == MemberSettings.ValidFor.WLAN) {
            rd_wlan.setSelected(true);
        } else if (settings.getValidFor() == MemberSettings.ValidFor.MOBILE) {
            rd_mobile.setSelected(true);
        }

    }
    
    private void checkSpinnerRange(){
        if ((int) spn_days.getValue() <= 0){
            spn_days.setValue(1);
        } else if ( (int) spn_days.getValue() >= 30){
            spn_days.setValue(30);
        }
    }
    
    private String calcWords(int kb){
        return String.valueOf(kb*150);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        lb_avatar = new javax.swing.JLabel();
        btn_changeAvatar = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        tf_name = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        rd_atLogin = new javax.swing.JRadioButton();
        rd_listenToServer = new javax.swing.JRadioButton();
        rd_inDays = new javax.swing.JRadioButton();
        spn_days = new javax.swing.JSpinner();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel7 = new javax.swing.JLabel();
        rd_yes = new javax.swing.JRadioButton();
        rd_ask = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        rd_mobile = new javax.swing.JRadioButton();
        rd_wlan = new javax.swing.JRadioButton();
        jSeparator8 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        chk_allowWebClients = new javax.swing.JCheckBox();
        jSeparator9 = new javax.swing.JSeparator();
        chk_saveLocaleHist = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        spn_KByte = new javax.swing.JSpinner();
        jLabel9 = new javax.swing.JLabel();
        lb_wordamaount = new javax.swing.JLabel();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        jLabel4.setText("Avatar:");

        lb_avatar.setAlignmentX(50.0F);
        lb_avatar.setAlignmentY(50.0F);
        lb_avatar.setMaximumSize(new java.awt.Dimension(50, 50));
        lb_avatar.setMinimumSize(new java.awt.Dimension(50, 50));
        lb_avatar.setPreferredSize(new java.awt.Dimension(50, 50));

        btn_changeAvatar.setText("Browse");
        btn_changeAvatar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_changeAvatarActionPerformed(evt);
            }
        });

        jLabel5.setText("Announcement name:");

        tf_name.setText("#{autoFilled}");

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel6.setText("Force recreation:");

        rd_atLogin.setText("At login");
        rd_atLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_atLoginActionPerformed(evt);
            }
        });

        rd_listenToServer.setSelected(true);
        rd_listenToServer.setText("Listen to Serversettings");
        rd_listenToServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_listenToServerActionPerformed(evt);
            }
        });

        rd_inDays.setText("In x Days");
        rd_inDays.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_inDaysActionPerformed(evt);
            }
        });

        spn_days.setEnabled(false);
        spn_days.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spn_daysStateChanged(evt);
            }
        });

        jLabel7.setText("Autodownload for Image:");

        rd_yes.setSelected(true);
        rd_yes.setText("Yes");
        rd_yes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_yesActionPerformed(evt);
            }
        });

        rd_ask.setText("Ask if needed");
        rd_ask.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_askActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Ubuntu", 0, 13)); // NOI18N
        jLabel8.setText("valid for:");

        rd_mobile.setText("Mobile");
        rd_mobile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_mobileActionPerformed(evt);
            }
        });

        rd_wlan.setSelected(true);
        rd_wlan.setText("WLAN");
        rd_wlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rd_wlanActionPerformed(evt);
            }
        });

        jLabel1.setText("Allow WebClient- Requests:");

        jLabel2.setText("Save locale history:");

        chk_allowWebClients.setSelected(true);
        chk_allowWebClients.setText("Yes");
        chk_allowWebClients.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_allowWebClientsActionPerformed(evt);
            }
        });

        chk_saveLocaleHist.setSelected(true);
        chk_saveLocaleHist.setText("Yes");
        chk_saveLocaleHist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chk_saveLocaleHistActionPerformed(evt);
            }
        });

        jLabel3.setText("Histoize at every");

        spn_KByte.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spn_KByteStateChanged(evt);
            }
        });

        jLabel9.setText("Kb per File");

        lb_wordamaount.setText("#{autoCalculates}");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(btn_changeAvatar))
                                .addGap(55, 55, 55)
                                .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addComponent(tf_name, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton2)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING))
                                .addGap(2, 2, 2)
                                .addComponent(jSeparator8))
                            .addComponent(jSeparator9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2)
                                    .addComponent(chk_allowWebClients)
                                    .addComponent(chk_saveLocaleHist)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(spn_KByte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel9))
                                    .addComponent(lb_wordamaount))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rd_listenToServer)
                            .addComponent(jLabel6)
                            .addComponent(rd_atLogin)
                            .addComponent(rd_inDays)
                            .addComponent(spn_days, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(20, Short.MAX_VALUE))
                    .addComponent(jSeparator7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(rd_yes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(rd_ask))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(rd_wlan)
                                    .addComponent(jLabel8))
                                .addGap(18, 18, 18)
                                .addComponent(rd_mobile)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator6)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_atLogin)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_listenToServer)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rd_inDays)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(spn_days, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rd_yes)
                            .addComponent(rd_ask))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rd_wlan)
                            .addComponent(rd_mobile))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_name, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_changeAvatar)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addGap(12, 12, 12)
                .addComponent(chk_allowWebClients)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chk_saveLocaleHist)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(spn_KByte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lb_wordamaount))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_changeAvatarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_changeAvatarActionPerformed
        ui.changeAvatarAction(MainUI.CLIENTPROPERTIES);
        ui.updateAvatar();
    }//GEN-LAST:event_btn_changeAvatarActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        if (!tf_name.getText().isEmpty()) {
            FileManager.storeValueInPropertiesFile(MainUI.CLIENTPROPERTIES, "announcementName", tf_name.getText());
        } else {
            area.append(Utilities.getLogTime() + "Error: Your name has to be filled (AnnouncementName)!\n");
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "recreationDays", spn_days.getValue().toString());
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "histBorder", spn_KByte.getValue().toString());
    }//GEN-LAST:event_formInternalFrameClosing

    private void chk_allowWebClientsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_allowWebClientsActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "allowWebClientRequests", String.valueOf(chk_allowWebClients.isSelected()));
    }//GEN-LAST:event_chk_allowWebClientsActionPerformed

    private void chk_saveLocaleHistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chk_saveLocaleHistActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "allowWebClientRequests", String.valueOf(chk_allowWebClients.isSelected()));
        if (chk_saveLocaleHist.isSelected()){
            jLabel3.setEnabled(true);
            spn_KByte.setEnabled(true);
            jLabel9.setEnabled(true);
            lb_wordamaount.setEnabled(true);
            spn_KByte.setValue(((int) Integer.decode(FileManager.getValueOfPropertyByKey(CLIENTPROPERTIES, "histBorder")) / 1000));
        } else {
            jLabel3.setEnabled(false);
            spn_KByte.setEnabled(false);
            jLabel9.setEnabled(false);
            lb_wordamaount.setEnabled(false);
            spn_KByte.setValue(0);
        }
    }//GEN-LAST:event_chk_saveLocaleHistActionPerformed

    private void rd_atLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_atLoginActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "recreationType", MemberSettings.RecreationEnum.AT_LOGIN.toString());
        spn_days.setEnabled(false);
        spn_days.setValue(0);
    }//GEN-LAST:event_rd_atLoginActionPerformed

    private void rd_listenToServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_listenToServerActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "recreationType", MemberSettings.RecreationEnum.AT_SERVER.toString());
        spn_days.setEnabled(false);
        spn_days.setValue(0);
    }//GEN-LAST:event_rd_listenToServerActionPerformed

    private void rd_inDaysActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_inDaysActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "recreationType", MemberSettings.RecreationEnum.BY_DAYS.toString());
        spn_days.setEnabled(true);
        spn_days.setValue(1);
    }//GEN-LAST:event_rd_inDaysActionPerformed

    private void rd_yesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_yesActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "autodownload", MemberSettings.AutoDownload.YES.toString());
    }//GEN-LAST:event_rd_yesActionPerformed

    private void rd_askActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_askActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "autodownload", MemberSettings.AutoDownload.ASK.toString());
    }//GEN-LAST:event_rd_askActionPerformed

    private void rd_wlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_wlanActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "validFor", MemberSettings.ValidFor.WLAN.toString());
    }//GEN-LAST:event_rd_wlanActionPerformed

    private void rd_mobileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rd_mobileActionPerformed
        FileManager.storeValueInPropertiesFile(CLIENTPROPERTIES, "validFor", MemberSettings.ValidFor.MOBILE.toString());
    }//GEN-LAST:event_rd_mobileActionPerformed

    private void spn_daysStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spn_daysStateChanged
        checkSpinnerRange();
    }//GEN-LAST:event_spn_daysStateChanged

    private void spn_KByteStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spn_KByteStateChanged
        if ((int)spn_KByte.getValue() <= 0){
            spn_KByte.setValue(1);
        } 
        if ((int) spn_KByte.getValue() > 15){
            spn_KByte.setValue(15);
        }
        lb_wordamaount.setText("(about "+calcWords((int) spn_KByte.getValue())+" words)");
    }//GEN-LAST:event_spn_KByteStateChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_changeAvatar;
    private javax.swing.JCheckBox chk_allowWebClients;
    private javax.swing.JCheckBox chk_saveLocaleHist;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lb_avatar;
    private javax.swing.JLabel lb_wordamaount;
    private javax.swing.JRadioButton rd_ask;
    private javax.swing.JRadioButton rd_atLogin;
    private javax.swing.JRadioButton rd_inDays;
    private javax.swing.JRadioButton rd_listenToServer;
    private javax.swing.JRadioButton rd_mobile;
    private javax.swing.JRadioButton rd_wlan;
    private javax.swing.JRadioButton rd_yes;
    private javax.swing.JSpinner spn_KByte;
    private javax.swing.JSpinner spn_days;
    private javax.swing.JTextField tf_name;
    // End of variables declaration//GEN-END:variables
}
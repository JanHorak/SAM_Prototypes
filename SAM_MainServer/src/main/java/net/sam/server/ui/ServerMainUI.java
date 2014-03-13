package net.sam.server.ui;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import net.sam.server.manager.DataAccess;
import net.sam.server.servermain.Server;

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
        this.setTitle("SAM - SecureAndroidMessenger - Server");
        lb_logo.setIcon(new ImageIcon("src/main/resources/graphics/simpleLogoSAM.png"));
    }

    // ---------- Variables ----------------
    private Server server;

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
        );

        btn_send.setText("Send");
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnl_messanges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(lb_logo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tgl_StartServer))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_serverMAXCON)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lb_serverIP)
                                    .addComponent(lb_serverPORT))
                                .addGap(103, 103, 103)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(tf_serverIP, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(chk_defaultIP))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(tf_serverPORT, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                                            .addComponent(spr_serverMAXCON))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(chk_defaultMAXCON)
                                            .addComponent(chk_defaultPORT))))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tf_singleMessange, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.DEFAULT_SIZE, 79, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
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
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tgl_StartServer)
                    .addComponent(lb_logo))
                .addGap(10, 10, 10)
                .addComponent(pnl_messanges, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tf_singleMessange, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_send))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            ta_messanges.append("["+new Date().toString()+"] Server started...");
            tgl_StartServer.setText("Stop Server");
        } else {
            try {
                server.shutDown();
                ta_messanges.append("\n["+new Date().toString()+"] Server stopped!");
                tgl_StartServer.setText("Start Server");
            } catch (IOException ex) {
                Logger.getLogger(ServerMainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_tgl_StartServerActionPerformed

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed

    }//GEN-LAST:event_btn_sendActionPerformed

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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lb_logo;
    private javax.swing.JLabel lb_serverIP;
    private javax.swing.JLabel lb_serverMAXCON;
    private javax.swing.JLabel lb_serverPORT;
    private javax.swing.JPanel pnl_messanges;
    private javax.swing.JSpinner spr_serverMAXCON;
    public javax.swing.JTextArea ta_messanges;
    private javax.swing.JTextField tf_serverIP;
    private javax.swing.JTextField tf_serverPORT;
    private javax.swing.JTextField tf_singleMessange;
    private javax.swing.JToggleButton tgl_StartServer;
    // End of variables declaration//GEN-END:variables
}

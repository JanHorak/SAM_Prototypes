package sam_testclient.ui.main;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTextArea;
import sam_testclient.beans.ClientMainBean;
import sam_testclient.communication.Client;
import sam_testclient.communication.CommunicationThread;
import sam_testclient.entities.AvatarImage;
import sam_testclient.entities.Handshake;
import sam_testclient.entities.MediaFile;
import sam_testclient.entities.Message;
import sam_testclient.enums.EnumHandshakeReason;
import sam_testclient.enums.EnumHandshakeStatus;
import sam_testclient.enums.EnumKindOfMessage;
import sam_testclient.exceptions.NotAHandshakeException;
import sam_testclient.services.HistoricizationService;
import sam_testclient.sources.FileManager;
import static sam_testclient.sources.FileManager.storeValueInPropertiesFile;
import sam_testclient.sources.MessageWrapper;
import sam_testclient.sources.ValidationManager;
import sam_testclient.ui.components.SettingsFrame;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class MainUI extends javax.swing.JFrame {

    private Client client;
    private ClientMainBean cmb;

    public String getPW() {
        return this.tf_password.getText();
    }

    public static final String CLIENTPROPERTIES = "resources/properties/client.properties";
    SettingsFrame sf;

    public MainUI() {
        sf = new SettingsFrame(client, this, jTextField1, messageArea);
        this.add(sf);
        initComponents();

        cmb = ClientMainBean.getInstance();
        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);
        jRadioButton1.doClick();
        updateAvatar();
        jTextField2.setText(FileManager.getValueOfPropertyByKey(CLIENTPROPERTIES, "announcementName"));
    }

    public void updateAvatar() {
        lb_avatar.setIcon(new ImageIcon(FileManager.getValueOfPropertyByKey(CLIENTPROPERTIES, "avatar")));
    }

    private void prepareUI() {
        tf_memberName.setEnabled(false);
        tf_password.setEnabled(false);
        btn_send.setEnabled(true);
        btn_register.setEnabled(false);

    }

    public void acceptBuddyRequest(Message m) throws NotAHandshakeException {
        m.getHandshake().setAnswer(true);
        m.getHandshake().setStatus(EnumHandshakeStatus.END);

        m.setReceiverId(m.getSenderId());
        m.setSenderId(this.client.getId());
        try {
            cmb.getBuddyList().put(m.getReceiverId(), m.getContent());
            client.createBuddyDir(m.getContent());
            client.sendStatusRequest();
            client.writeMessage(MessageWrapper.createJSON(m));
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void denyBuddyRequest(Message m) throws NotAHandshakeException {
        m.getHandshake().setAnswer(false);
        m.getHandshake().setStatus(EnumHandshakeStatus.END);

        m.setReceiverId(m.getSenderId());
        m.setSenderId(this.client.getId());
        try {
            client.writeMessage(MessageWrapper.createJSON(m));
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void acceptFileRequest(Message m) throws NotAHandshakeException {
        m.getHandshake().setAnswer(true);
        m.getHandshake().setStatus(EnumHandshakeStatus.END);

        m.setReceiverId(m.getSenderId());
        m.setSenderId(this.client.getId());
        try {
            client.writeMessage(MessageWrapper.createJSON(m));
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void denyFileRequest(Message m) throws NotAHandshakeException {
        m.getHandshake().setAnswer(false);
        m.getHandshake().setStatus(EnumHandshakeStatus.END);

        m.setReceiverId(m.getSenderId());
        m.setSenderId(this.client.getId());
        try {
            client.writeMessage(MessageWrapper.createJSON(m));
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void changeAvatarAction(String pathOfProperties) {
        AvatarImage avatar;

        JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();

            // Validation
            avatar = new AvatarImage(f.getAbsolutePath());
            if (ValidationManager.isValid(avatar)) {
                storeValueInPropertiesFile(pathOfProperties, "avatar", f.getAbsolutePath());
            } else {
                messageArea.append(Utilities.getLogTime() + "Error: The selected Avatar is not valid\n");
            }
        }
    }

    @Deprecated
    public void showSaveDialog(Message m) {
        JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(chooser.getSelectedFile() + File.pathSeparator + m.getMediaStorage().getFileName());
                fos.write(m.getMediaStorage().getContent());
                fos.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fos.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public int returnSelectedIDFromBuddy() {
        return client.getIdFromBuddy(list_buddies.getSelectedValue().toString().split(": ")[1]);
    }

    public JTextArea getArea() {
        return this.messageArea;
    }

    public JList getBuddyList() {
        return this.list_buddies;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tf_message = new javax.swing.JTextField();
        btn_send = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_buddies = new javax.swing.JList();
        jPanel2 = new javax.swing.JPanel();
        lb_name = new javax.swing.JLabel();
        tf_memberName = new javax.swing.JTextField();
        lb_password = new javax.swing.JLabel();
        tf_password = new javax.swing.JPasswordField();
        tgl_login = new javax.swing.JToggleButton();
        btn_register = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jButton2 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        btn_searchFriend = new javax.swing.JButton();
        btn_sendFile = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lb_avatar = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btn_send.setText("Send");
        btn_send.setEnabled(false);
        btn_send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Buddies"));

        list_buddies.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "No Buddies" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        list_buddies.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                list_buddiesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(list_buddies);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Register and Login"));

        lb_name.setText("Name:");

        lb_password.setText("Password:");

        tgl_login.setText("Login");
        tgl_login.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tgl_loginActionPerformed(evt);
            }
        });

        btn_register.setText("Register");
        btn_register.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_registerActionPerformed(evt);
            }
        });

        jLabel3.setText("Serverpassword:");

        jRadioButton1.setText("Login");
        jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton1ActionPerformed(evt);
            }
        });

        jRadioButton2.setText("New Registration");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jButton2.setText("Settings");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_password)
                            .addComponent(lb_name)
                            .addComponent(jLabel3)
                            .addComponent(btn_register, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tgl_login, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(tf_password)
                            .addComponent(tf_memberName)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 86, Short.MAX_VALUE)
                        .addComponent(jRadioButton2)
                        .addGap(35, 35, 35))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_name)
                    .addComponent(tf_memberName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lb_password)
                    .addComponent(tf_password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tgl_login)
                    .addComponent(btn_register)
                    .addComponent(jButton2))
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Messages"));

        messageArea.setColumns(20);
        messageArea.setRows(5);
        jScrollPane1.setViewportView(messageArea);

        jScrollPane3.setViewportView(jScrollPane1);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Settings"));

        jLabel1.setText("Add a Friend");

        btn_searchFriend.setText("Search");
        btn_searchFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchFriendActionPerformed(evt);
            }
        });

        btn_sendFile.setText("Browse");
        btn_sendFile.setEnabled(false);
        btn_sendFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_sendFileActionPerformed(evt);
            }
        });

        jLabel2.setText("Send a File:");

        jLabel4.setText("Avatar:");

        lb_avatar.setAlignmentX(50.0F);
        lb_avatar.setAlignmentY(50.0F);
        lb_avatar.setMaximumSize(new java.awt.Dimension(50, 50));
        lb_avatar.setMinimumSize(new java.awt.Dimension(50, 50));
        lb_avatar.setPreferredSize(new java.awt.Dimension(50, 50));

        jButton1.setText("Check Buddy Status");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel5.setText("Announcement name:");

        jTextField2.setEditable(false);
        jTextField2.setText("#{autoFilled}");

        jSeparator6.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(28, 28, 28)
                        .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(btn_sendFile))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jTextField1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btn_searchFriend))))
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(jLabel4)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(btn_sendFile))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_searchFriend))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator6))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tf_message, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_send))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_registerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_registerActionPerformed
        String md5 = Utilities.getHash(jPasswordField1.getText());
        client = new Client();
        try {
            client.connect();
            prepareUI();
            new CommunicationThread(this.client, this, client.getId()).start();
            messageArea.append(Utilities.getLogTime() + " Try to register...\n");
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        Handshake hs = new Handshake(1, EnumHandshakeStatus.START, EnumHandshakeReason.REGISTER, false, md5);
        Message m = new Message(this.client.getId(), 0, EnumKindOfMessage.HANDSHAKE, "", "");
        m.setHandshake(hs);
        try {
            client.writeMessage(MessageWrapper.createJSON(m));
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_registerActionPerformed

    private void btn_sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendActionPerformed
        int receiverID = client.getIdFromBuddy(list_buddies.getSelectedValue().toString().split(": ")[1]);
        Message m = new Message(client.getId(), receiverID, EnumKindOfMessage.MESSAGE, tf_message.getText(), "");
        String formattedMessage_forMe = Utilities.getTime() + " " + list_buddies.getSelectedValue().toString().split(": ")[1] + ": " + m.getContent(); // contains Name of otherMember
        String formattedMessage_forOther = Utilities.getTime() + " " + tf_memberName.getText() + ": " + m.getContent(); // contains my Name
        try {
            m.setContent(formattedMessage_forOther);
            client.writeMessage(MessageWrapper.createJSON(m));
            messageArea.append("\n" + formattedMessage_forOther);
            tf_message.setText("");
            if (cmb.getSettings().isSaveLocaleHistory()) {
                m.setContent(formattedMessage_forMe);
                HistoricizationService.historizeMessage(m, true);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_sendActionPerformed

    private void tgl_loginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tgl_loginActionPerformed
        if (tgl_login.isSelected()) {
            client = new Client();
            try {
                messageArea.append(Utilities.getLogTime() + " Try to login...\n");
                client.connect();
                prepareUI();
                new CommunicationThread(this.client, this, client.getId()).start();
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            Message m = new Message(0, 0, EnumKindOfMessage.LOGIN, tf_memberName.getText(), tf_password.getText());
            try {
                client.writeMessage(MessageWrapper.createJSON(m));
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            tgl_login.setText("Logout");
        } else {
            Message m = new Message(client.getId(), 0, EnumKindOfMessage.LOGOUT, tf_memberName.getText(), "");
            try {
                client.writeMessage(MessageWrapper.createJSON(m));
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            tgl_login.setText("Login");
        }
    }//GEN-LAST:event_tgl_loginActionPerformed

    private void btn_searchFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchFriendActionPerformed
        // Handshake- content: Name of searched buddy, message-content: own name
        Handshake hs = new Handshake(0, EnumHandshakeStatus.START, EnumHandshakeReason.BUDDY_REQUEST, false, jTextField1.getText());
        Message message = new Message(this.client.getId(), 0, EnumKindOfMessage.HANDSHAKE, tf_memberName.getText(), "");
        message.setHandshake(hs);
        try {
            client.writeMessage(MessageWrapper.createJSON(message));
            messageArea.append(Utilities.getLogTime() + " Buddyrequest sended\n");
            jTextField1.setText("");
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btn_searchFriendActionPerformed

    private void list_buddiesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_list_buddiesValueChanged
        if (list_buddies.getSelectedValue() == null) {
            btn_send.setEnabled(false);
            btn_sendFile.setEnabled(false);
        } else {
            btn_send.setEnabled(true);
            btn_sendFile.setEnabled(true);
        }
    }//GEN-LAST:event_list_buddiesValueChanged

    private void btn_sendFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_sendFileActionPerformed
        JFileChooser chooser = new JFileChooser();
        int returnValue = chooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File sendFile = chooser.getSelectedFile();
            byte[] content = FileManager.returnBytesOfFile(sendFile);
            String name = sendFile.getName();

            MediaFile mf = new MediaFile();
            mf.setFileName(name);
            mf.setContent(content);
            mf.setType(MediaFile.getEnumTypeOfFile(sendFile));
            mf.setDescription("FileTest");
            cmb.setLastFile(mf);

            Message request = new Message(this.client.getId(), returnSelectedIDFromBuddy(), EnumKindOfMessage.HANDSHAKE, "", "");
            Handshake hs = new Handshake(1, EnumHandshakeStatus.START, EnumHandshakeReason.FILE_REQUEST, false, mf.getFileName());
            request.setHandshake(hs);

            try {
                this.client.writeMessage(MessageWrapper.createJSON(request));
            } catch (IOException ex) {
                Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (returnValue == JFileChooser.CANCEL_OPTION) {
            chooser.cancelSelection();
        }

    }//GEN-LAST:event_btn_sendFileActionPerformed

    private void jRadioButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton1ActionPerformed
        btn_register.setEnabled(false);
        jPasswordField1.setEnabled(false);
        jLabel3.setEnabled(false);
        tgl_login.setEnabled(true);
    }//GEN-LAST:event_jRadioButton1ActionPerformed

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        btn_register.setEnabled(true);
        jPasswordField1.setEnabled(true);
        jLabel3.setEnabled(true);
        tgl_login.setEnabled(false);
    }//GEN-LAST:event_jRadioButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        client.sendStatusRequest();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        sf.setVisible(true);
        try {
            sf.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_register;
    private javax.swing.JButton btn_searchFriend;
    private javax.swing.JButton btn_send;
    private javax.swing.JButton btn_sendFile;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JLabel lb_avatar;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_password;
    private javax.swing.JList list_buddies;
    public javax.swing.JTextArea messageArea;
    private javax.swing.JTextField tf_memberName;
    private javax.swing.JTextField tf_message;
    private javax.swing.JPasswordField tf_password;
    private javax.swing.JToggleButton tgl_login;
    // End of variables declaration//GEN-END:variables
}

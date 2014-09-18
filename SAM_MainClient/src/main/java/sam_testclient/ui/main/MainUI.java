package sam_testclient.ui.main;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import net.jan.poolhandler.resourcepoolhandler.ResourcePoolHandler;
import org.apache.log4j.BasicConfigurator;
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
import sam_testclient.enums.EnumMessageStatus;
import sam_testclient.exceptions.NotAHandshakeException;
import sam_testclient.services.ClientResoucesPool;
import sam_testclient.services.FileSubmitService;
import sam_testclient.services.HistoricizationService;
import sam_testclient.sources.FileManager;
import sam_testclient.sources.MessageWrapper;
import sam_testclient.sources.ValidationManager;
import sam_testclient.ui.components.SettingsFrame;
import sam_testclient.ui.components.StatusFrame;
import sam_testclient.utilities.Utilities;

/**
 *
 * @author janhorak
 */
public class MainUI extends javax.swing.JFrame {

    private Client client;
    private ClientMainBean cmb;
    private static int progressStep = 0;
    private static int progressBuffer = 0;

    public String getPW() {
        return this.tf_password.getText();
    }

    private SettingsFrame sf;

    private StatusFrame statusFrame;

    public MainUI() {
        BasicConfigurator.configure();
        ResourcePoolHandler.loadFileResources(ClientResoucesPool.class);
//        Utilities.replaceDB("SAMClient.db");
        sf = new SettingsFrame(client, this, jTextField1, messageArea);
        
        statusFrame = new StatusFrame();
        this.add(sf);
        this.add(statusFrame);
        initComponents();

        cmb = ClientMainBean.getInstance();
        HistoricizationService.loadCurrentHistoryInMemory();
        ButtonGroup bg = new ButtonGroup();
        bg.add(jRadioButton1);
        bg.add(jRadioButton2);
        jRadioButton1.doClick();
        updateAvatar();

        jTextField2.setText(((Properties) ResourcePoolHandler.getResource("clientProperties")).getProperty("announcementName"));

        /*Currently no uiThread needed */
//        UIUpdateThread uiThread = new UIUpdateThread(this);
//        uiThread.start();
        lb_filesubmitPercent.setText("0%");
        lb_fileLabel_incoming.setText("");

        initTabPane(cmb.getBuddyList());
    }

    public void updateAvatar() {
        lb_avatar.setIcon(new ImageIcon(((Properties) ResourcePoolHandler.getResource("clientProperties")).getProperty("avatar")));
    }

    private void prepareUI() {
        tf_memberName.setEnabled(false);
        tf_password.setEnabled(false);
        btn_send.setEnabled(true);
        btn_register.setEnabled(false);
    }

    /**
     * mapps the buddylist to the tabs of the UI
     *
     * @param buddyList
     */
    public void initTabPane(Map<Integer, String> buddyList) {
        tab_messages.removeAll();
        for (String buddyName : buddyList.values()) {
            JTextArea area = new JTextArea();
            if (HistoricizationService.isHistoryPresent(buddyName)) {
                String content = HistoricizationService.getContentFromHistMessagesByName(buddyName);
                area.append(content);
            } else {
                area.append("You are friends now");
            }
            tab_messages.addTab(buddyName, area);
        }
        tab_messages.addTab("System", messageArea);
        tab_messages.setSelectedIndex(0);
    }

    public void distributeMessageToAreas(Message m) {
        String content = m.getContent();
        String buddyName = content.split(":")[1];
        buddyName = buddyName.split(" ")[1];
        int buddyIndex = tab_messages.indexOfTab(buddyName);
        if (tab_messages.getSelectedIndex() != buddyIndex) {
            cmb.addMessageToUnreadList(buddyIndex, m);
            tab_messages.setIconAt(buddyIndex, new ImageIcon(ResourcePoolHandler.getPathOfResource("notice_gif")));
            Utilities.playSound("resources/sounds/notice.wav");
        } else {
            CommunicationThread.fireStatusMessage(m, EnumMessageStatus.READ);
            m.setMessageStatus(EnumMessageStatus.READ);

        }

        ((JTextArea) tab_messages.getComponentAt(buddyIndex)).append(content);
    }

    private void addMyMessageToMessageArea(String buddyName, String content) {
        ((JTextArea) tab_messages.getComponentAt(tab_messages.indexOfTab(buddyName))).append(content);
    }

    public void acceptBuddyRequest(Message m) throws NotAHandshakeException {
        m.getHandshake().setAnswer(true);
        m.getHandshake().setStatus(EnumHandshakeStatus.END);

        m.setReceiverId(m.getSenderId());
        m.setSenderId(this.client.getId());
        try {
            cmb.getBuddyList().put(m.getReceiverId(), m.getContent());
            FileManager.serialize(cmb.getBuddyList(), "resources/buddyList.data");
            client.createBuddyDir(m.getContent());
            client.sendStatusRequest();
            client.writeMessage(MessageWrapper.createJSON(m));
            HistoricizationService.createEmptyHistFile(m.getContent());
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

        this.showSaveDialog(m);
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
                ResourcePoolHandler.PropertiesHelper.setValueInProperties("clientProperties", "avatar", f.getAbsolutePath());
            } else {
                messageArea.append(Utilities.getLogTime() + "Error: The selected Avatar is not valid\n");
            }
        }
    }

    /**
     * Message request = new Message(this.client.getId(),
     * returnSelectedIDFromBuddy(), EnumKindOfMessage.HANDSHAKE, id_UUID, name);
     *
     * @param m
     */
    private void showSaveDialog(Message m) {

        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(m.getOthers()));
        String result = "";
        String key = m.getContent();
        String parts = "";
        try {
            parts = m.getHandshake().getContent();
        } catch (NotAHandshakeException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        int returnValue = chooser.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            result = chooser.getSelectedFile().getAbsolutePath();
        }
        FileSubmitService.startWaitingService(key, result, Integer.decode(parts), Integer.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "waitingServiceTimeout")));
        try {
            client.writeMessage(MessageWrapper.createJSON(m));
        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }

        prepareFileProgressbar(result, parts);
    }

    public void prepareFileProgressbar(String fileName, String parts) {
        jProgressBar1.setValue(0);
        jProgressBar1.setIndeterminate(true);
        jProgressBar1.setBorderPainted(true);
        jProgressBar1.setStringPainted(true);
        lb_filesubmitPercent.setText("0%");
        lb_fileLabel_incoming.setText("File: " + new File(fileName).getName() + "...");
        progressBuffer = 100 / Integer.decode(parts);
        progressStep = progressBuffer;
    }

    public static void filePartSubmitted() {
        jProgressBar1.setIndeterminate(false);
        jProgressBar1.setValue(progressStep);
        lb_filesubmitPercent.setText(progressStep + "%");
        progressStep += progressBuffer;
    }

    public static void filePartSubmitEnd() {
        progressBuffer = 0;
        progressStep = 0;
        jProgressBar1.setValue(0);
        lb_filesubmitPercent.setText("0%");
        lb_fileLabel_incoming.setText("Completed.");
    }

    public int returnSelectedIDFromBuddy() {
        return client.getIdFromBuddy(list_buddies.getSelectedValue().toString().split(": ")[0]);
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
        jProgressBar1 = new javax.swing.JProgressBar();
        lb_filesubmitPercent = new javax.swing.JLabel();
        lb_fileLabel_incoming = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tab_messages = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

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
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 329, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        lb_filesubmitPercent.setText("#{%}");

        lb_fileLabel_incoming.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        lb_fileLabel_incoming.setText("#{text}");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lb_fileLabel_incoming, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addGap(28, 28, 28)
                                        .addComponent(lb_avatar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jProgressBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel4Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
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
                                        .addComponent(btn_searchFriend)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_filesubmitPercent)))
                .addContainerGap())
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
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jProgressBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lb_filesubmitPercent))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lb_fileLabel_incoming)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_searchFriend)))
                    .addComponent(jSeparator6))
                .addContainerGap())
        );

        tab_messages.setAutoscrolls(true);
        tab_messages.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tab_messagesStateChanged(evt);
            }
        });

        jScrollPane1.setAutoscrolls(true);

        messageArea.setEditable(false);
        messageArea.setColumns(20);
        messageArea.setRows(5);
        jScrollPane1.setViewportView(messageArea);

        tab_messages.addTab("System", jScrollPane1);

        jScrollPane3.setViewportView(tab_messages);

        jButton3.setText("Smileys");

        jButton4.setText("Status");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(tf_message, javax.swing.GroupLayout.PREFERRED_SIZE, 451, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_send, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)))
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
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tf_message, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_send)
                            .addComponent(jButton3)
                            .addComponent(jButton4))))
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
        int receiverID = client.getIdFromBuddy(list_buddies.getSelectedValue().toString().split(": ")[0]);
        Message m = new Message(client.getId(), receiverID, EnumKindOfMessage.MESSAGE, tf_message.getText(), "");

        String buddyName = list_buddies.getSelectedValue().toString().split(": ")[0];

        String formattedMessage_forMe = "\n" + Utilities.getTime() + " " + buddyName + ": \t" + m.getContent(); // contains Name of otherMember
        String formattedMessage_forOther = "\n" + Utilities.getTime() + " " + tf_memberName.getText() + ": \t" + m.getContent(); // contains my Name

        try {
            m.setContent(formattedMessage_forOther);
            m.setMessageStatus(EnumMessageStatus.SENT);
            client.writeMessage(MessageWrapper.createJSON(m));
            addMyMessageToMessageArea(buddyName, formattedMessage_forOther);
            tf_message.setText("");

        } catch (IOException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
        m.setContent(formattedMessage_forMe);
        cmb.initMessageStatus(m, buddyName);

        if (cmb.getSettings().isSaveLocaleHistory()) {
            m.setContent(formattedMessage_forMe);
            HistoricizationService.addMessageToCurrentHistory(m, true);
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
            if (sendFile.length() > Integer.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "fileMaxSize"))) {
                JOptionPane.showMessageDialog(this, "Filesize is higher or equals 1 MB!", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
                String name = sendFile.getName();
                String id_UUID = Utilities.generateRandomUUID().toString();
                int parts = Integer.decode(ResourcePoolHandler.PropertiesHelper.getValueOfKey("clientProperties", "waitingServiceParts"));
                Message request = new Message(this.client.getId(), returnSelectedIDFromBuddy(), EnumKindOfMessage.HANDSHAKE, id_UUID, name);
                Handshake hs = new Handshake(1, EnumHandshakeStatus.START, EnumHandshakeReason.FILE_REQUEST, false, String.valueOf(parts));
                request.setHandshake(hs);
                MediaFile mf = new MediaFile();
                mf.setId(id_UUID);
                mf.setFilePath(sendFile.getAbsolutePath());
                mf.setFileName(name);
                mf.setType(MediaFile.getEnumTypeOfFile(sendFile));
                mf.setDescription("FileTest");
                cmb.setLastFile(mf);
                try {
                    this.client.writeMessage(MessageWrapper.createJSON(request));
                } catch (IOException ex) {
                    Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
                }
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

    private void tab_messagesStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tab_messagesStateChanged
        int index = tab_messages.getSelectedIndex();
        cmb = ClientMainBean.getInstance();
        if (index >= 0) {
            if (!cmb.getUnreadMessagesAtTabMap().isEmpty()) {
                if (cmb.getUnreadMessagesAtTabMap().containsKey(index)) {
                    for (Message me : cmb.getUnreadMessagesAtTabMap().get(index)) {
                        CommunicationThread.fireStatusMessage(me, EnumMessageStatus.READ);

                        me.setMessageStatus(EnumMessageStatus.READ);
                        cmb.updateMessageStatus(me);
                    }
                    cmb.removeMessagesFromUnreadList(index);
                }
                tab_messages.setIconAt(index, new ImageIcon());
            }
        }
    }//GEN-LAST:event_tab_messagesStateChanged

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        statusFrame.reloadMessages();
        statusFrame.setVisible(true);
        try {
            statusFrame.setSelected(true);
        } catch (PropertyVetoException ex) {
            Logger.getLogger(MainUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

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
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPasswordField jPasswordField1;
    private static javax.swing.JProgressBar jProgressBar1;
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
    private static javax.swing.JLabel lb_fileLabel_incoming;
    private static javax.swing.JLabel lb_filesubmitPercent;
    private javax.swing.JLabel lb_name;
    private javax.swing.JLabel lb_password;
    private javax.swing.JList list_buddies;
    public javax.swing.JTextArea messageArea;
    public javax.swing.JTabbedPane tab_messages;
    public javax.swing.JTextField tf_memberName;
    private javax.swing.JTextField tf_message;
    private javax.swing.JPasswordField tf_password;
    private javax.swing.JToggleButton tgl_login;
    // End of variables declaration//GEN-END:variables
}

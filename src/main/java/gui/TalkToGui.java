package gui;

import consumers.GuiListener;
import models.UserJoined;
import models.UserText;
import org.apache.activemq.ActiveMQConnectionFactory;
import producers.UserChannelActions;

import javax.jms.JMSException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TalkToGui {

    // Components
    private JTextField userText;
    private JButton enterButton;
    private JCheckBox redCheckBox;
    private JCheckBox blueCheckBox;
    private JCheckBox greenCheckBox;
    private JTable chatTable;
    private JList usersList;
    private JPanel mainPanel;
    private JLabel componentName;

    // Manual
    private final ActiveMQConnectionFactory connectionFactory;
    private final String self;
    private UserChannelActions userChannelActions;
    private final Set<UserJoined> usersJoined;
    private boolean master = false;

    public TalkToGui() {
        usersJoined = new HashSet<>();
        self = this.getClass().getSimpleName() + " " + LocalDateTime.now().toString();

        this.initComponent();


        this.connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        try{
            init();
        }catch(JMSException e){
            e.printStackTrace();
        }

        enterButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!userText.getText().isEmpty()){
                    try{
                        userChannelActions.sendMessage(new UserText(self,userText.getText()));
                        userText.setText("");
                    }catch(JMSException err){
                        err.printStackTrace();
                    }

                }
            }
        });
    }

    // BEGIN INITIALIZER SECTION

    /**
     * Initializes the models with default values
     */
    private void initComponent(){

        // Sets up table
        Object[][] data1 = new Object[][]{};
        Object[] column = new Object[]{"User","Message"};
        DefaultTableModel defaultTableModel = new DefaultTableModel(data1,column);
        chatTable.setModel(defaultTableModel);

        // Sets up list
        DefaultListModel defaultListModel = new DefaultListModel();
        usersList.setModel(defaultListModel);
    }

    /**
     * Initalizes the Consumers and Producers
     * @throws JMSException
     */
    private void init() throws JMSException{
        // Starts up Consumer
        Thread guiListener = new Thread(new GuiListener(this.connectionFactory,this));
        guiListener.start();

        // Starts up Producer
        userChannelActions = new UserChannelActions(this.connectionFactory,"CLIENT_ACTION");
        userChannelActions.sendMessage(new UserJoined(this.self));
    }

    // END INITIALIZER SECTION

    // BEGIN UPDATE MODELS SECTION

    /**
     * Updates the table with the passed object
     * @param message
     */
    public void updateTableWithMessages(UserText message){
        DefaultTableModel defaultTableModel = (DefaultTableModel)chatTable.getModel();
        List<String> messageList = new ArrayList<>();
        messageList.add(message.getUser());
        messageList.add(message.getMessage());
        defaultTableModel.addRow(messageList.toArray());
    }

    /**
     * Updates the users list with the userjoined
     * @param userJoined
     */
    public void updateUsersList(Set<UserJoined> userJoined){
        DefaultListModel defaultListModel = (DefaultListModel)usersList.getModel();
        defaultListModel.clear();
        userJoined.stream().forEach(user ->{
            defaultListModel.addElement(user.getUser());
        });
    }


    /**
     * Updates the users list with the joined user
     */
    public void updateUsersList(){
        DefaultListModel defaultListModel = (DefaultListModel)usersList.getModel();
        defaultListModel.clear();
        System.out.println("Users list length" + usersJoined.toArray().length);
        usersJoined.stream().forEach(user ->{
            defaultListModel.addElement(user.getUser());
        });
    }

    // HELPER METHODS

    /**
     * Returns the current panel
     * @return
     */
    public String getUser(){
        return this.self;
    }

    /**
     * Returns list of users Joined
     * @return
     */
    public Set<UserJoined> getUsersJoined(){
        return usersJoined;
    }

    /**
     * Adding user to the list
     * @param user
     */
    public void addUser(UserJoined user){
        usersJoined.add(user);
    }


    /**
     * Sets the current panel as the master
     */
    public void setAsMaster(){
        this.master = true;
        this.componentName.setText("Master");
    }

    /**
     * Sets the current panel as a Receiver
     */
    public void notMaster(){
        this.master = false;
        this.componentName.setText("Receiver");
    }

    /**
     * Checks if the current panel is master
     * @return
     */
    public boolean isMaster() {
        return master;
    }

    public static void main(String [] args){
        JFrame frame = new JFrame("TalkToGui");
        frame.setContentPane(new TalkToGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

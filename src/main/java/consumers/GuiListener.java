package consumers;

import gui.TalkToGui;
import models.ListOfUsers;
import models.UserJoined;
import models.UserText;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

public class GuiListener extends CustomListener implements Runnable {

    public GuiListener(ActiveMQConnectionFactory connectionFactory, TalkToGui talkToGui) {
       super(connectionFactory,talkToGui);
    }

    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    public void run() {
        try{
            setupConnection("CLIENT_ACTION");

            while(true){
                // Gets message
                Message message = messageConsumer.receive();

                if (message instanceof  ObjectMessage){
                   Serializable obj = ((ObjectMessage)message).getObject();
                    if(obj instanceof Boolean){
                        System.out.println("Boolean sent: " + obj);
                    }else if(obj instanceof UserText){
                        UserText userText = (UserText)obj;
                        System.out.printf("User Typed: " + userText.toString());
                        this.talkToGui.updateTableWithMessages(userText);
                    }else if(obj instanceof UserJoined){

                        UserJoined userJoined = (UserJoined)obj;
                        System.out.println("User Joined: " + userJoined.getUser());

                        /*
                            Checks to see if the users list is empty, if so then
                            sets the current user as the master
                         */
                        if(this.talkToGui.getUsersJoined().isEmpty()){
                            this.talkToGui.addUser(userJoined);
                            this.talkToGui.setAsMaster();
                        }else{
                            this.talkToGui.notMaster();
                        }

                        if(this.talkToGui.isMaster()){
                            this.talkToGui.addUser(userJoined);
                            this.talkToGui.updateUsersList();
                            sendMessage(new ListOfUsers(this.talkToGui.getUser(),this.talkToGui.getUsersJoined()));
                        }
                    }else if(obj instanceof ListOfUsers){
                        if(!this.talkToGui.isMaster()){
                            this.talkToGui.updateUsersList(((ListOfUsers)obj).getListOfUsers());
                        }
                    }
                }
            }
        }catch (JMSException e){
            e.printStackTrace();
        }
    }
}

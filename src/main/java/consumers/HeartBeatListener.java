package consumers;

import gui.TalkToGui;
import models.HeartBeat;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

public class HeartBeatListener extends CustomListener implements Runnable {

    private String currentUser;

    public HeartBeatListener(ActiveMQConnectionFactory connectionFactory, TalkToGui talkToGui) {
        super(connectionFactory, talkToGui);
        currentUser = this.talkToGui.getUser();
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
    @Override
    public void run() {
        try{
            setupConnection("HEART_BEAT");
            while(true){
                // Gets message
                Message message = messageConsumer.receive();
                if(message instanceof ObjectMessage){
                    Serializable obj = ((ObjectMessage)message).getObject();
                    if(obj instanceof HeartBeat){

                    }
                }
            }
        }catch(JMSException e){
            e.printStackTrace();
        }
    }
}

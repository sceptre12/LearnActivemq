package producers;

import gui.TalkToGui;
import models.HeartBeat;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.JMSException;

public class HearBeatProducer extends UserChannelActions implements Runnable{
    private final TalkToGui talkToGui;

    public HearBeatProducer(ActiveMQConnectionFactory connectionFactory,String topicName, TalkToGui talkToGui) throws JMSException {
        super(connectionFactory,topicName);
        this.talkToGui = talkToGui;
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
        while(true){
            try{
                this.sendMessage(new HeartBeat(this.talkToGui.getUser()));
                Thread.sleep(1000);
            }catch(JMSException e){
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

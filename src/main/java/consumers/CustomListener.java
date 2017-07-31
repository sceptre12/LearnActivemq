package consumers;

import gui.TalkToGui;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

public abstract class CustomListener {
    private final ActiveMQConnectionFactory connectionFactory;
    private Session session;
    private Connection connection;
    private Destination topicDestination;
    protected final TalkToGui talkToGui;
    protected MessageConsumer messageConsumer;

    public CustomListener(ActiveMQConnectionFactory connectionFactory, TalkToGui talkToGui) {
        this.connectionFactory = connectionFactory;
        this.talkToGui = talkToGui;
    }

    public void sendMessage(Serializable obj) throws JMSException{
        // Create a MessageProducer from the session to the topic or queue
        MessageProducer producer = session.createProducer(topicDestination);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        ObjectMessage objectMessage = session.createObjectMessage(obj);

        producer.send(objectMessage);
        producer.close();
    }

    public void setupConnection(String topicName) throws JMSException {
        // Creating a connection
        connection = connectionFactory.createConnection();
        connection.start();

        // Creating a session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // Creates a topic, if the topic exists it will return it
        topicDestination = session.createTopic(topicName);

        // Create a message producer from the session to the topic or queue
        messageConsumer = session.createConsumer(topicDestination);
    }
}

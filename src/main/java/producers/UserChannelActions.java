package producers;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.Serializable;

public class UserChannelActions{

    private Connection connection;
    private Session session;
    private Destination clientActionTopic;

    public UserChannelActions(){}

    public UserChannelActions(ActiveMQConnectionFactory connectionFactory,String topicName) throws JMSException {
        connection = connectionFactory.createConnection();
        connection.start();
        // Creates a new session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        clientActionTopic = session.createTopic(topicName);
    }

    public void sendMessage(String message) throws JMSException {
        // Create a MessageProducer from the session to the topic or queue
        MessageProducer producer = session.createProducer(clientActionTopic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        // Creates a string message to be sent
        TextMessage txtMessage  = session.createTextMessage(message);

        // Send the message to the topic
        producer.send(txtMessage);
    }


    public void sendMessage(Boolean bool) throws JMSException{
        // Create a MessageProducer from the session to the topic or queue
        MessageProducer producer = session.createProducer(clientActionTopic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        ObjectMessage objectMessage = session.createObjectMessage(bool);

        producer.send(objectMessage);
    }


    public void sendMessage(Serializable obj) throws JMSException{
        // Create a MessageProducer from the session to the topic or queue
        MessageProducer producer = session.createProducer(clientActionTopic);
        producer.setDeliveryMode(DeliveryMode.PERSISTENT);

        ObjectMessage objectMessage = session.createObjectMessage(obj);

        producer.send(objectMessage);
    }


    public void closeSession() throws JMSException {
        session.close();
    }

}

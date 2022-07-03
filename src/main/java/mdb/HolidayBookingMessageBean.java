package mdb;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Message-Driven Bean implementation class for: HolidayBookingMessageBean
 */
@MessageDriven(
		activationConfig = { @ActivationConfigProperty(
				propertyName = "destination", propertyValue = "java:/jms/HolidayBookingQueue"), @ActivationConfigProperty(
				propertyName = "destinationType", propertyValue = "javax.jms.Queue")
		}, 
		mappedName = "java:/jms/HolidayBookingQueue")
public class HolidayBookingMessageBean implements MessageListener {

    /**
     * Default constructor. 
     */
    public HolidayBookingMessageBean() {
        // TODO Auto-generated constructor stub
    }
	
	/**
     * @see MessageListener#onMessage(Message)
     */
    public void onMessage(Message message) {
    	System.out.println("Message received by MDB");
    	
    	try {
    		MapMessage ReqMsg = (MapMessage) message;
			String username = ReqMsg.getString("username");
//			int id = insertBook(title);
			
			System.out.println(String.format("The title: %s with ID %d ", username));
			
			deliverResult(ReqMsg);
						
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
    
    public void deliverResult(MapMessage ReqMsg) throws JMSException, NamingException {
		Context jndiContext = new InitialContext();
		ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("java:/ConnectionFactory");
		
		Topic resultTopic = (Topic) jndiContext.lookup("java:/jms/HolidayBookingTopic");
		Connection connect = connectionFactory.createConnection();
		Session session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);

		String username = ReqMsg.getString("username");

		MessageProducer sender = session.createProducer(resultTopic);
		TextMessage message = session.createTextMessage();

		message.setText(String.format("New Request: %s with", username));
		sender.send(message);
		connect.close();
	}

}

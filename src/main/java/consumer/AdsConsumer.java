package consumer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class AdsConsumer implements MessageListener {

	public AdsConsumer()
	{
		Context jndiContext;
		
		try {
			jndiContext = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory)jndiContext.lookup ("java:/ConnectionFactory");
	      
		    Topic resultTopic = (Topic) jndiContext.lookup ("java:/jms/HolidayBookingTopic");
		    
		    Connection connect = factory.createConnection();      
			Session session = connect.createSession(false,Session.AUTO_ACKNOWLEDGE);      
			MessageConsumer receiver = session.createConsumer(resultTopic); 
			
			receiver.setMessageListener(this);
			
			System.out.println ("AdsConsumer Listening for messages on java:/jms/HolidayBookingTopic...");
			connect.start ();
			    
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onMessage(Message message) {

		try {
			TextMessage objMsg = (TextMessage)message;
			String msg = objMsg.getText();
			
			System.out.println ("Advertise for: " + msg);
			
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

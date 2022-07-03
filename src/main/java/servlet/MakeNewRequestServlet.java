package servlet;

import java.io.IOException;
import java.sql.Date;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import dao.HolidayBookingDTO;
import model.User;

/**
 * Servlet implementation class MakeNewRequestServlet
 */
@WebServlet("/MakeNewRequestServlet")
public class MakeNewRequestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private HolidayBookingDTO hbDTO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MakeNewRequestServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		User employee = hbDTO.getEmployee(username);
		
//		get current date
		long millis = System.currentTimeMillis();  
	    Date date = new Date(millis);
		
		// TODO Auto-generated method stub
		HttpSession session1 = request.getSession();
		session1.setAttribute("date", date);
		session1.setAttribute("employee", employee);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("makeNewRequest.jsp");
		dispatcher.forward(request, response);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		try {
			Context jndiContext = new InitialContext();
			ConnectionFactory factory = (ConnectionFactory) jndiContext.lookup("java:/ConnectionFactory");
			Queue calculationQueue = (Queue) jndiContext.lookup("java:/jms/HolidayBookingQueue");
			Connection connect = factory.createConnection();

			Session session = connect.createSession(false, Session.AUTO_ACKNOWLEDGE);
			MessageProducer sender = session.createProducer(calculationQueue);
			MapMessage message = session.createMapMessage();
			
			System.out.println("Sending message");
			
			// TODO Auto-generated method stub
			String username = request.getParameter("username");
			Date fromDate = Date.valueOf(request.getParameter("fromDate"));
			Date toDate = Date.valueOf(request.getParameter("toDate"));
			String subject = request.getParameter("subject");
			
			// check if break constraint
			boolean isEntitled = hbDTO.isEntitled(username);
			if(isEntitled) {
				String breakConstraint = null;
				hbDTO.insertEmployeeLeaveApplication(username, fromDate, toDate, subject, breakConstraint);
			} else {
				String breakConstraint = "Insufficient leave balance";
				hbDTO.insertEmployeeLeaveApplication(username, fromDate, toDate, subject, breakConstraint);
			}
			
			RequestDispatcher dispatcher = request.getRequestDispatcher("dashboard.jsp");
			dispatcher.forward(request, response);
			
		
			message.setString("username", username);
			sender.send(message);
			connect.close();

			
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

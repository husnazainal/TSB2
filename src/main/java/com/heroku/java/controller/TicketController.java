package com.heroku.java.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.heroku.java.model.feedback;

@PostMapping("/addTickets")
try (Connection connection = dataSource.getConnection()) {
	String sql = "INSERT INTO public.ticket(tickettype,ticketprice) VALUES(?,?)";
	final var statement = connection.prepareStatement(sql);






@Controller
public class TicketController {
    private final DataSource dataSource;

    @Autowired
    public TicketController(DataSource dataSource) {
        this.dataSource = dataSource;
    }


	int feedbackID;
	String comments,timeCreate;
	Date dateCreate;
	int star;
	
    /**
     * @see HttpServlet#HttpSerstarvlet()
     */
    public AddFeedbackController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//retrieve input from HTML
		timeCreate = request.getParameter("timeCreate");
		dateCreate = request.getParameter("dateCreate");
		star = request.getParameter("star");
		comments = request.getParameter("comments");
	

		
		try {			
			//call getConnection() method
			con = ConnectionManager.getConnection();

			//3. create statement
			String sql = "INSERT INTO feedback ( timeCreate, dateCreate, star, comments;)VALUES(?,?,?,?)";
			ps = con.prepareStatement(sql);
			
			ps.setString(1, timeCreate);
			ps.setDate(2, dateCreate);
			ps.setInt(3, star);
			ps.setString(4, comments);
		
			
			//4. execute query
			ps.executeUpdate();

			//5. close connection
			con.close();

		}catch(Exception e) {
			e.printStackTrace();
		}	
		
		//Obtain the RequestDispatcher from the request object. The the pathname to the resource is index.html
		RequestDispatcher req = request.getRequestDispatcher("index.html");
		
		//Dispatch the request to another resource using forward() methods of the RequestDispatcher
		req.forward(request, response);
		
	}
}

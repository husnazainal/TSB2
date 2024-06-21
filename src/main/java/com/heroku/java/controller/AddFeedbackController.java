package com.heroku.java.controller;

// kalau visitor yang input kan still have to do controller ke?


/*
 * Author: FES 
 * March 2024
 */
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import connection.ConnectionManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class AddFeedbackServlet
 */
public class AddFeedbackController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Connection con = null;
	static PreparedStatement ps = null;
	static Statement stmt = null;
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

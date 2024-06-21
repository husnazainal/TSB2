package com.heroku.java.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tsb.connection.ConnectionManager;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class DeletePlantController
 */
public class DeletePlantController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Connection con = null;
	static PreparedStatement ps = null;
	static Statement stmt = null;
	int id;
	String plantSciname, plantComname, plantType, plantHabitat, plantSpecies,plantDesc ;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePlantController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		//get id from input parameter
		id = Integer.parseInt(request.getParameter("id"));
		
		try {			
			//call getConnection() method
			con = ConnectionManager.getConnection();

			//3. create statement
			String sql = "DELETE FROM plant WHERE id=?";
			ps = con.prepareStatement(sql);
			
			ps.setInt(1, id);
			
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
	}

}

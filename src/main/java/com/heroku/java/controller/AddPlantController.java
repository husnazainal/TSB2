package com.heroku.java.controller;

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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Servlet implementation class AddShawlServlet
 */
public class AddPlantController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Connection con = null;
	static PreparedStatement ps = null;
	static Statement stmt = null;
	String plantSciname, plantComname, plantType, plantHabitat, plantSpecies,plantDesc ;
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddPlantController() {
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
		plantSciname = request.getParameter("plantSciname");
		plantComname = request.getParameter("plantComname");
		plantType = request.getParameter("plantType");
		plantHabitat = request.getParameter("plantHabitat");
		plantSpecies = request.getParameter("plantSpecies");
		plantDesc = request.getParameter("plantDesc");

		
		
		
		try {			
			//call getConnection() method
			con = ConnectionManager.getConnection();

			//3. create statement
			String sql = "INSERT INTO shawl (plantSciname,plantComname,plantType,plantHabitat,plantSpecies,plantDesc)VALUES(?,?,?,?,?,?)";
			ps = con.prepareStatement(sql);
			
			ps.setString(1, plantSciname);
			ps.setString(2, plantComname);
			ps.setString(3, plantType);
			ps.setString(4, plantHabitat);
			ps.setString(5, plantSpecies);
			ps.setString(6, plantDesc);
			
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

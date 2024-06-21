package com.heroku.java;

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
import tsb.connection.ConnectionManager;
import shawl.model.Shawl; /// tak faham tukar mana apa 
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Servlet implementation class UpdatePlantController
 */
public class UpdatePlantController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	static Connection con = null;
	static PreparedStatement ps = null;
	static Statement stmt = null;
	static ResultSet rs = null;
	int id, quantity;
	String plantSciname, plantComname, plantType, plantHabitat, plantSpecies,plantDesc ;
	
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdatePlantController() {
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
			ps = con.prepareStatement("SELECT * FROM plant WHERE id=?");
			ps.setInt(1, id);

			//4. execute query
			rs = ps.executeQuery();
			if(rs.next()) {
			id = rs.getInt("id");
			plantSciname = rs.getString("plantSciname");
			plantComname = rs.getString("plantComname");
			plantType = rs.getString("plantHabitat");
			plantHabitat = rs.getString("plantHabitat");
			plantSpecies = rs.getString("plantSpecies");
			plantDesc = rs.getString("plantDesc");
			}

			//5. close connection
			con.close();

		}catch(Exception e) {
			e.printStackTrace();	
		}	

		//set attribute to a servlet request. Set the attribute for each
		request.setAttribute("id", id);
		request.setAttribute("plantSciname", plantSciname);
		request.setAttribute("plantComname", plantComname);
		request.setAttribute("plantType", plantType);
		request.setAttribute("plantHabitat", plantHabitat);
		request.setAttribute("plantSpecies", plantSpecies);
		request.setAttribute("plantDesc", plantDesc);
		
		//Obtain the RequestDispatcher from the request object. The the pathname to the resource is addShawl.html
		RequestDispatcher req = request.getRequestDispatcher("updatePlant.jsp"); //// tambah

		//Dispatch the request to another resource using forward() methods of the RequestDispatcher
		req.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		//retrieve input from HTML
		id = Integer.parseInt(request.getParameter("id"));
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
			ps = con.prepareStatement("UPDATE plant SET plantSciname=?,plantComname=?,plantType=?,plantHabitat=?,plantSpecies=?,plantDesc=? WHERE id=?");

			ps.setString(1, plantSciname);
			ps.setString(2, plantComname);
			ps.setString(3, plantType);
			ps.setString(4, plantHabitat);
			ps.setString(5, plantSpecies);
			ps.setString(6, plantDesc);
			ps.setInt(7, id);

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
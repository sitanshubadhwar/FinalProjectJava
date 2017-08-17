/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.group3.servlets;

import com.group3.util.DbManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Manu
 */
@WebServlet(name = "TestServlet", urlPatterns = {"/TestServlet"})
public class UpdateServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session=request.getSession(false);
        String UserSess=(String)session.getAttribute("log"); 
               
        ServletContext context=getServletContext();
        
        DbManager dbMgr=(DbManager)context.getAttribute("DbMgr");
        
        Connection con=dbMgr.getConnection();
        
        RequestDispatcher rd=null;
        
        String butPar=request.getParameter("button");
          
        if(butPar.equals("Update"))
        {
        String email=request.getParameter("email");
        String country=request.getParameter("country");
        
        String updateQuery = "UPDATE users SET email =?,country=?,stuid=? WHERE username =?";
        String sQuery="update course_detail set stuid=? where username=?";
        try{
        PreparedStatement ps=con.prepareStatement(updateQuery);
        ps.setString(1,email);
        ps.setString(2,country);
        ps.setString(3,UserSess+email);
        ps.setString(4,UserSess);
        ps.executeUpdate();
        
        PreparedStatement pss=con.prepareStatement(sQuery);
        pss.setString(1,UserSess+email);
        pss.setString(2,UserSess);
        pss.executeUpdate();
        rd = context.getRequestDispatcher("/Login.html");
           PrintWriter out = response.getWriter();
              out.println("<font color=green> <h1><center>Update Completed! "
                      + "please login</center></h1></font>");
              rd.include(request, response);
       }catch(SQLException e)
       {System.out.print("Error");}
       }
        else if(butPar.equals("See Details"))
        {
           try{
           
           String selectQuery="select email,country,course_code,course_name from users inner join course_detail on users.username=? and course_detail.username=?";
           PreparedStatement ps=con.prepareStatement(selectQuery);
           ps.setString(1,UserSess);
           ps.setString(2,UserSess);
           ResultSet rs=ps.executeQuery();
           
           String em="",cn="",cc="",cnn="";
           
           while(rs!=null & rs.next()){
           em=rs.getString("email");
           cn=rs.getString("country");
           cc=rs.getString("course_code");
           cnn=rs.getString("course_name");
           }
           System.out.println("EMail:"+em);
           System.out.println("Country:"+cn);
           response.setContentType("text/html;charset=UTF-8");
           
           rd=context.getRequestDispatcher("/Update.html");
           PrintWriter out = response.getWriter(); 
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Details</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1><center>Find below the user details</center></h1><br>");
            out.print("<table align= 'center' border='2'><tr><th>Usename</th><th>Email</th><th>Country</th><th>Course Code</th><th>Course Name</th></tr>");
            out.println("<tr><td>"+UserSess+"</td><td>"+em+"</td><td>"+cn+"</td><td>"+cc+"</td><td>"+cnn+"</td></tr></table>");
            out.println("</body>");
            out.println("</html>");
           
           rd.include(request, response);
          }catch(SQLException e){
              System.out.println("Error Has Occured");
          }
        }
        
        
        
        /*
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // TODO output your page here. You may use following sample code.
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");*/
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

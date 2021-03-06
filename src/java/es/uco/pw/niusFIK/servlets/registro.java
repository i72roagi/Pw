/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uco.pw.niusFIK.servlets;

import es.uco.pw.niusFIK.dao.registroDAO;
import es.uco.pw.niusFIK.dao.loginDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author janthonyo
 */
public class registro extends HttpServlet {

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
        RequestDispatcher rd = request.getRequestDispatcher("/views/registro.jsp");
        rd.include(request,response);
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
                response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        boolean allRight = true;
        Hashtable<String, String> register = new Hashtable<String, String>();
        
        String name = request.getParameter("name");
        String lastname = request.getParameter("lastname");
        String birthdate = request.getParameter("birth-date");
        String email = request.getParameter("correo");
        String phone = request.getParameter("phone");
        String user = request.getParameter("user");
        String passwd = request.getParameter("pass");
        
        // Comprueba si hay campos nulos en el registro
        if(name == "")     {allRight = false;}
        if(lastname == "") {allRight = false;}
        if(birthdate == ""){allRight = false;}
        if(email == "")    {allRight = false;}
        if(phone == "")    {allRight = false;}
        if(user == "")     {allRight = false;}
        if(passwd == "")   {allRight = false;}
        
        if(allRight == false){
            // Si es asi, se le redirige a una pagina de error
            RequestDispatcher rd = request.getRequestDispatcher("/views/registroErrorDatos.jsp");
            rd.include(request,response);
        }
        
        // Se comprueba si ya existe ese login en la base de datos
        else if(registroDAO.checkUserExist(user))
        {
            // Si es asi, se le redirige a una pagina de error
            RequestDispatcher rd = request.getRequestDispatcher("/views/registroErrorLogin.jsp");
            rd.include(request,response);
        }
        
        // Se comprueba si el email ya esta siendo usado por alguien registrado
        else if(registroDAO.checkEmailExist(email))
        {
            // Si es asi, se le redirige a una pagina de error
            RequestDispatcher rd = request.getRequestDispatcher("/views/registroErrorEmail.jsp");
            rd.include(request,response);
        }
        
        // Si no falta / falla nada, el usuario es insertado
        else{
            registroDAO.insertUserData(name,lastname,birthdate,email,phone,user,passwd);
            
            // Se busca el usuario para obtener el ID que se le ha asignado
            while(!loginDAO.checkUser(user)){};
            
            // Se obtienen los datos del usuario y se guardan en su sesion
            Hashtable<String, String> data = loginDAO.queryByUser(user);
              
            request.getSession().setAttribute("uName", name + " " + lastname);
            request.getSession().setAttribute("uLogin", user);
            request.getSession().setAttribute("uID", data.get("id"));
            request.getSession().setAttribute("justRegistered", "true");
            
            // Se le redirige al perfil/mod para que cumplimente su Curriculum
            response.sendRedirect("perfil/mod");
        }
   
        out.close();
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

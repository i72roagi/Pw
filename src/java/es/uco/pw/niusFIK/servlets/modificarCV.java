/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uco.pw.niusFIK.servlets;

import es.uco.pw.niusFIK.dao.curriculumDAO;
import es.uco.pw.niusFIK.dao.loginDAO;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

/**
 *
 * @author skrotex
 */
@MultipartConfig
public class modificarCV extends HttpServlet {

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
        try {
            //------------------------------------------------------------------ 
            //Prueba para comprobar si los valores de getSession llegan.
            
            //PrintWriter out = response.getWriter();
            //out.print(request.getSession().getAttribute("uID"));
            //out.close();
            
            //Integer user_ID = (Integer)request.getSession().getAttribute("uID"); <-- Aqui es donde peta
            //------------------------------------------------------------------
            
            response.setContentType("text/html;charset=UTF-8");
            HashMap<String, Object> result = curriculumDAO.queryByUserID(19);
            request.setAttribute("curriculum", result);
        } catch (Exception e) {
            System.out.print(e);
        }
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
        request.getRequestDispatcher("/views/modificar_perfil.jsp").forward(request, response);
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
        try {
            HashMap<String, Object> updateMap = new HashMap<String, Object>();
            Map<String, String[]> parameters = (Map<String, String[]>) request.getParameterMap();
            updateMap.put("id", 1);
            for (String parameter : parameters.keySet()) {
                String[] values = parameters.get(parameter);
                if (!values[0].equals("") && !parameter.equals("valPass")) {
                    updateMap.put(parameter, values[0]);
                }
            }
            if (updateMap.containsKey("lastPass")) {
                if (!loginDAO.checkPassword(1, (String) updateMap.get("lastPass"))) {
                    updateMap.remove("password");
                    request.setAttribute("passChanged", false);
                } else {
                    request.setAttribute("passChanged", true);
                }
                updateMap.remove("lastPass");
            }
            if (request.getPart("imagen") != null) {
                InputStream inputStream = null;
                Part filePart = request.getPart("imagen");
                if (filePart != null) {
                    inputStream = filePart.getInputStream();
                }
                updateMap.put("imagen", inputStream);
            }
            curriculumDAO.updateCV(updateMap);
            processRequest(request, response);
            request.getRequestDispatcher("/views/modificar_perfil.jsp").forward(request, response);
        } catch (Exception e) {
            System.out.println(e);
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uco.pw.niusFIK.servlets;

import es.uco.pw.niusFIK.dao.amigosDAO;
import es.uco.pw.niusFIK.dao.publicacionesDAO;
import es.uco.pw.niusFIK.dao.curriculumDAO;
import es.uco.pw.niusFIK.dao.loginDAO;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import static java.util.Objects.isNull;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author skrotex
 */
public class perfil extends HttpServlet {

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
        // Marcamos el contentType como html y con codificación UTF-8.
        response.setContentType("text/html;charset=UTF-8");
        ArrayList<Hashtable<String, String>> resultPb = null;
        HashMap<String, Object> resultCV = null;
        boolean friends = false;
        // Si no recibimos id, supuestamente imposible, tomamos primero el uID.
        if (request.getParameter("id") == null) {
            // Obtenemos de las clases DAO respectivas la información necesaria.
            resultPb
                    = publicacionesDAO.queryByUserID(Integer.parseInt((String) request.getSession().getAttribute("uID")));
            resultCV
                    = curriculumDAO.queryByUserID(Integer.parseInt((String) request.getSession().getAttribute("uID")));
        } else {
            // Obtenemos de las clases DAO respectivas la información necesaria.
            resultPb
                    = publicacionesDAO.queryByUserID(Integer.parseInt(request.getParameter("id")));
            resultCV
                    = curriculumDAO.queryByUserID(Integer.parseInt(request.getParameter("id")));
            //Comprobamos si el id pasado por parametro y el id del usuario son el mismo
            try {
                if (request.getParameter("id").equals(request.getSession().getAttribute("uID"))) {
                } else {
                    //si no son el mismo se comprueba si los usuarios son amigos y se asigna el resultado a una variable mediante la clase DAO
                    friends = amigosDAO.checkIfFriends(Integer.parseInt((String) request.getSession().getAttribute("uID")), Integer.parseInt((String) request.getParameter("id")));
                }
            } catch (Exception e) {
                System.out.print(e);
            }
        }
        // Añadimos los atributos a la request y le mandamos el control al jsp del perfil.
        request.setAttribute("friends", friends);
        request.setAttribute("publicaciones", resultPb);
        request.setAttribute("curriculum", resultCV);
        request.getRequestDispatcher("/views/perfil.jsp").forward(request, response);
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
        // Bloque try-catch para prevención de errores
        try {
            // Comprueba si existe el usuario pasado como parámetro, en caso negativo se comprueba si existe el usuario supuestamente logueado.
            // En caso negativo de ambos se manda a página de error.
            if (loginDAO.existsUserID(Integer.parseInt((String) request.getParameter("id")))) {
                processRequest(request, response);
            } else if (loginDAO.existsUserID(Integer.parseInt((String) request.getSession().getAttribute("uID")))) {
                processRequest(request, response);
            } else {
                request.getRequestDispatcher("/views/perfilError.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.getRequestDispatcher("/views/perfilError.jsp").forward(request, response);
        }

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
        /*
        Si detecta que el boton de publicar ha sido pulsado, es decir, es distinto
        de null, pues publica la publicacion y accede a ella.
        */
        if (!isNull(request.getParameter("botonPublicar"))) {
            request.setAttribute("botonPublicar", null);
            String cuerpo = request.getParameter("Publicacion");
            String nombre = request.getParameter("Titulo");
            String idUsuario = (String) request.getSession().getAttribute("uID");
            Date f = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String fecha = formatter.format(f);
            int id = publicacionesDAO.publicarPublicacion(Integer.parseInt(idUsuario), nombre, cuerpo, fecha, 0);
            response.sendRedirect(request.getContextPath() + "/publicacion?idP=" + id);
        }
        
        //Si en la vista se activa el boton de añadir un amigo se activa esta parte del código obteniendo los parámetros  
        //pasados por la vista y añadiendo las id de los dos usuarios a la base de datos mediante la clase DAO
        if (!isNull(request.getParameter("botonAnadir"))) {
            request.setAttribute("botonAnadir", null);
            String idAmigo = request.getParameter("id");
            amigosDAO.insertUserFriend((String) request.getSession().getAttribute("uID"), idAmigo);
            response.sendRedirect(request.getContextPath() + "/perfil?id=" + idAmigo);
        }
        //Si en la vista se activa el boton de borrar un amigo se activa esta parte del código obteniendo los parámetros  
        //pasados por la vista y borrando las id de los dos usuarios de la base de datos mediante la clase DAO
        if (!isNull(request.getParameter("botonEliminar"))) {
            request.setAttribute("botonEliminar", null);
            String idAmigo = request.getParameter("id");
            amigosDAO.deleteUserFriend((String) request.getSession().getAttribute("uID"), idAmigo);
            response.sendRedirect(request.getContextPath() + "/perfil?id=" + idAmigo);
        }

        //RequestDispatcher rd = request.getRequestDispatcher("/views/perfil.jsp");
        //rd.include(request,response);
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

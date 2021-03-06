/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.uco.pw.niusFIK.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 *
 * @author janthonyo
 */
public class loginDAO {

    /***************************************************************
     * getConnection()
     *
     * Inicia la conexion con la base de datos.
     *
     ***************************************************************/
    public static Connection getConnection() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://oraclepr.uco.es:3306/i72saraf", "i72saraf", "poketo");
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    
    /**************************************************************
    checkUser(String user)
    
    * Comprueba si existe o no el usuario pasado a la funcion en la base 
    * de datos. 
    * 
    * Devuelve true en el caso de que exista y false si no.
    
    ***************************************************************/
    public static boolean checkUser(String user) {
        boolean status = false;
        PreparedStatement ps = null;
        Connection con = getConnection();
        try {
            ps = con.prepareStatement(
                    "select * from usuarios where usuarios.usuario=? ");
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();
            rs.next();

            if (user.equals(rs.getString("usuario"))) {
                status = true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    
    /**************************************************************
    existsUserID(int userID)
    
    * Comprueba si existe o no el ID de usuario pasado a la funcion en la base 
    * de datos. 
    * 
    * Devuelve true en el caso de que exista y false si no.
    
    ***************************************************************/
    public static boolean existsUserID(int userID) {
        boolean status = false;
        PreparedStatement ps = null;
        Connection con = getConnection();
        try {
            ps = con.prepareStatement("select * from usuarios where id=?");
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            status = rs.next();
        } catch (Exception e) {
            System.out.print(e);
        }
        return status;
    }

    
    /**************************************************************
    checkLogin(String user, String passwd)
    
    * Comprueba si el login del usuario es correcto 
    * 
    * Devuelve true en el caso de que lo sea y false si no.
    
    ***************************************************************/
    public static boolean checkLogin(String user, String passwd) {
        boolean status = false;
        PreparedStatement ps = null;
        Connection con = getConnection();
        try {
            ps = con.prepareStatement(
                    "select * from usuarios where usuario=? "
                    + "and password=?");
            ps.setString(1, user);
            ps.setString(2, passwd);

            ResultSet rs = ps.executeQuery();
            rs.next();
            if (rs.getString("usuario").equals(user) && rs.getString("password").equals(passwd)) {
                status = true;
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    
    /**************************************************************
    checkPassword(int id, String passwd)
    
    * Comprueba si el password del usuario con la id indicada es correcto 
    * 
    * Devuelve true en el caso de que lo sea y false si no.
    
    ***************************************************************/
    public static boolean checkPassword(int id, String password) {
        boolean status = false;
        PreparedStatement ps;
        Connection con = getConnection();
        try {
            ps = con.prepareStatement("select password from usuarios where id=?");
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            rs.next();
            if (password.equals(rs.getString("password"))) {
                status = true;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return status;
    }

    /**************************************************************
    queryByUser(String user)
    
    * Obtiene los datos del usuario con el usuario indicado 
    * 
    * Devuelve un Hashtable con los datos del usuario en cuestion.
    
    ***************************************************************/
    public static Hashtable<String, String> queryByUser(String user) {
        Hashtable<String, String> res = null;
        PreparedStatement ps = null;
        Connection con = getConnection();
        try {
            ps = con.prepareStatement(
                    "select * from usuarios where usuarios.usuario=? ");
            ps.setString(1, user);

            ResultSet rs = ps.executeQuery();

            rs.next();
            String id = rs.getString("usuarios.id");
            String nombre = rs.getString("usuarios.nombre");
            String apellidos = rs.getString("usuarios.apellidos");
            String usuario = rs.getString("usuarios.usuario");

            res = new Hashtable<String, String>();

            res.put("id", id);
            res.put("nombre", nombre + apellidos);
            res.put("user", usuario);

        } catch (Exception e) {
            System.out.println(e);
        }
        return res;
    }

    // Hacer queryByLogin 
}

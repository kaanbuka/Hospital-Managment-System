/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Project;
import java.sql.*;
import javax.swing.JOptionPane;
/**
 *
 * @author eskil
 */
public class ConnectionProvider {
    public static Connection getCon()
    {
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/loyalhospital", "root", "M3z@rruzgari");
            return con;
        }
        catch(Exception e)
        {
            JOptionPane.showMessageDialog(null, "Disconnect!");
        }
        return null;
    }
}

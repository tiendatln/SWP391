/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Admin
 */
public class DBConnection {

    /**
     *
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public static Connection connect() throws ClassNotFoundException, SQLException {
        //Khai bao driver
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        //Tao doi tuong Connection
        Connection conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=SWP391;user=sa;password=1234;encrypt=true;trustServerCertificate=true;");
        return conn;
    }
}

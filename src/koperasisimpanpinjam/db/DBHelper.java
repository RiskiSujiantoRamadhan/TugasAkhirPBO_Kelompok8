/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package koperasisimpanpinjam.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DBHelper {
    private static final String SQCONN = "jdbc:sqlite:koperasi_db.sql";

    public static Connection getConnection(String driver) throws SQLException {
        Connection conn = null;
        switch (driver) {
            case "SQLITE": {
                try {
                    Class.forName("org.sqlite.JDBC");
                    conn = DriverManager.getConnection(SQCONN);
                    createTable(conn, driver);
                } catch (ClassNotFoundException ex) {
                    System.out.println("Library tidak ada");
                    Logger.getLogger(DBHelper.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

        return conn;
    }

    public static void createTable(Connection conn, String driver) throws SQLException {
        String sqlCreate = "";
        switch (driver) {
            case "SQLITE": {
                sqlCreate = "CREATE TABLE IF NOT EXISTS  nasabah ("
                        + "    rekening INT (10)      PRIMARY KEY ,"
                        + "    nama      VARCHAR (100),"
                        + "    alamat   VARCHAR (100) "
                        + ");"
                        + "CREATE TABLE IF NOT EXISTS rekening ("
                        + "    noRekening INT (10)       PRIMARY KEY,"
                        + "    saldo    DOUBLE (16, 2),"
                        + "    rekening  INT (10)        REFERENCES nasabah (rekening) ON DELETE RESTRICT"
                        + "                                                                    ON UPDATE CASCADE"
                        + ");"
                        + "CREATE TABLE IF NOT EXISTS perusahaan ("
                        + "    rekening INT (10)      PRIMARY KEY "
                        + "                            REFERENCES nasabah (rekening) ON DELETE RESTRICT"
                        + "                                                                  ON UPDATE CASCADE,"
                        + "    nib   VARCHAR (100) "
                        + ");"
                        + "CREATE TABLE IF NOT EXISTS  individu ("
                        + "    rekening INT (10)     PRIMARY KEY "
                        + "                           REFERENCES nasabah (rekening) ON DELETE RESTRICT"
                        + "                                                                 ON UPDATE CASCADE,"
                        + "    nik    BIGINT (255),"
                        + "    npwp BIGINT(255)"
                        + ");";
                break;
            }
        }
        String sqls[] = sqlCreate.split(";");
        for (String sql : sqls) {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.execute();
        }
    }
}
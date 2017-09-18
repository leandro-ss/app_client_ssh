package br.com.inmetrics.database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import br.com.inmetrics.util.FlagEnum;

//import br.com.inmetrics.encrypter.Encrypter;
//import br.com.inmetrics.encrypter.EncrypterImpl;

public class ConnectionSQL {
    
    private static final String SQL = "SELECT USUARIO, HOSTNAME||DOMINIO, HOSTNAME_PONTE||DOMINIO, TIPO_SO FROM CAPACITY.TSERVIDOR WHERE HOSTNAME = ?";
    private static final String SQL_UDPATE_1 = "UPDATE CAPACITY.TSERVIDOR SET DATA_ULTIMO_ACESSO = ?, ACESSO = ? WHERE HOSTNAME = ?";
    private static final String SQL_UDPATE_2 = "UPDATE CAPACITY.TSERVIDOR SET TIPO_SO = ?, DATA_MODIFICACAO=?, ANALISTA_ID='GENERAL' WHERE HOSTNAME= ?";

    private static final Map<String, Map<String, String>> map;
    private static final ResourceBundle                   resource;

    private static PreparedStatement statement;    
    private static PreparedStatement statement_update1;
    private static PreparedStatement statement_update2;

    private static Connection conn;
    private ResultSet result;    

    static {
        map = new HashMap<String, Map<String, String>>();
        resource = ResourceBundle.getBundle("config");
        
        try {
            conn = DriverManager.getConnection(resource.getString("url"), resource.getString("user"), resource.getString("pass"));
            
            statement = conn.prepareStatement(SQL);
            statement_update1 = conn.prepareStatement(SQL_UDPATE_1);
            statement_update2 = conn.prepareStatement(SQL_UDPATE_2);

        } catch (SQLException e) {
            new Throwable(e);
        }
    }

    /* obtendo instancia de criptografia/descriptografia */

    public Map<String, String> getUserPassByHostname(String host) throws SQLException{
        
        if (!map.containsKey(host)) 
        {
            statement.setMaxRows(1);
            statement.setString(1, host.toUpperCase());
            
            setResult(statement.executeQuery());
            
            if (getResult().next()){
                map.put(host, new HashMap<String, String>());
                map.get(host).put("user", getResult().getString(1));
                map.get(host).put("hostname", getResult().getString(2));
                map.get(host).put("hostnamePonte", getResult().getString(3));
                map.get(host).put("tipoSO", getResult().getString(4));
            }
        }
        return map.get(host);
    }
    
    public int recordAcess(String hostname, FlagEnum flag) throws SQLException {
        statement_update1.setTimestamp(0, new Timestamp(new Date().getTime()));
        statement_update1.setString(1, flag.getFlag());
        statement_update1.setString(2, hostname);
        
        statement_update1.close();
        return statement_update1.executeUpdate(SQL_UDPATE_1);
    }
        
    public int updateSO(String hostname, String tipoSO) throws SQLException {
        statement_update2.setTimestamp(1, new Timestamp(new Date().getTime()));
        statement_update2.setString(2, tipoSO);
        statement_update2.setString(3, hostname);
        
        statement_update2.close();
        return statement_update2.executeUpdate(SQL_UDPATE_2);
    }
   

    private ResultSet getResult() {
        return result;
    }

    private void setResult(ResultSet result) {
        this.result = result;
    }
}
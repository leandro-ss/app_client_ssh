package br.com.inmetrics.database;

import java.util.Map;

import org.junit.Test;

import br.com.inmetrics.util.FlagEnum;





public class ConnectionSQLTest {

    @Test
    public void getUserPassByHostnameTest () throws Exception {
        ConnectionSQL conn = new ConnectionSQL();

        Map<String, String> map = conn.getUserPassByHostname("RJOLNX472");

        System.out.println(map );
    }

    @Test
    public void recordAcessTest () throws Exception {
        ConnectionSQL conn = new ConnectionSQL();

        int result = conn.recordAcess("RJOLNX472", FlagEnum.YES);
        
        System.out.println("Altered Register Count - "+result);
    }
}
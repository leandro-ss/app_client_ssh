package br.com.inmetrics.ssh;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Session;

/**
 * Classe respons�vel pela cria��o de sess�es ssh e exewcu��o de
 * comandos
 * 
 * @author T3356103 [Leandro Pinto]
 *
 */
public class SshHelper extends RemoteConnection {

    public void executeSSHCommand(String commands) throws Exception {
        executeSSHCommand(commands, null, null);
    }
 
    public void executeSSHCommand(String commands, String hostname) throws Exception {
        executeSSHCommand(commands, hostname, null);
    }

    public void executeSSHCommand(String commands, String hostname, Session ssh) throws Exception {

        if (connection == null) {
            createConnection(hostname);
        }

        if (connection == null || !connection.isAuthenticationComplete()) {
            throw new IOException("Erro na conexao com o servidor ${RemoteConnection.host} \n");
        }

        String uname = "";
        String saidaConsole = "";

        if (commands == null || commands.isEmpty()) {
            throw new Exception();
        }

        Integer count = 0;

        for (String command : commands.split("SEPARADOR")) {

            ssh = connection.openSession();
            ssh.execCommand(command);
            ssh.waitForCondition(ChannelCondition.EXIT_STATUS, 0);

            if (ssh.getExitStatus() != 0) {
                saidaConsole = "Erro ao executar o comando no servidor ${RemoteConnection.host}: [ ${command} ]";
            } else {
                BufferedReader br = new BufferedReader(new InputStreamReader(ssh.getStdout(), Charset.forName("UTF-8")));

                String line = br.readLine();
                do {
                    line = uname += line += "\n";
                    saidaConsole += line;
                    line = br.readLine();
                } while (line != null);

            }
            saidaConsole += "\n";
            ssh.close();

            count++;
        }

        System.out.println(saidaConsole);
    }
    
    public String executeSSH2GetSO() throws Exception {
        return executeSSH2GetSO( null, null);
    }
 
    public String executeSSH2GetSO( String hostname) throws Exception {
        return executeSSH2GetSO( hostname, null);
    }
    
    public String executeSSH2GetSO (String hostname, Session ssh) throws Exception{
        
        if (connection == null) {
            createConnection(hostname);
        }

        if (connection == null || !connection.isAuthenticationComplete()) {
            throw new IOException("Erro na conexao com o servidor ${RemoteConnection.host} \n");
        }

        ssh = connection.openSession();
        ssh.execCommand("uname -s -m");
        ssh.waitForCondition(ChannelCondition.EXIT_STATUS, 0);

        if (ssh.getExitStatus() != 0) {
            throw new IOException("Erro ao obter o SO do servidor ${RemoteConnection.host}");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(ssh.getStdout(), Charset.forName("UTF-8")));

        String result = br.readLine();
        return result.split(" ")[0];
    }
    
}

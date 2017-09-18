package br.com.inmetrics.console;

import br.com.inmetrics.ssh.RemoteConnection;
import br.com.inmetrics.ssh.SshHelper;
import br.com.inmetrics.util.TipoSoEnum;

public class ConsoleConnection {
	
    RemoteConnection rc = new RemoteConnection();
	String hostname;
	String comando;
	String line;
	Integer x = 0;

	public void executeCommandByConsole() throws Exception {

		System.out.println("Digite um hostname valido:");  

			if (line.equalsIgnoreCase("sair")) {

				/* encerrando conexao*/
				rc.closeConnection();
				System.exit(0);
				
			} else if (line.equalsIgnoreCase("novo")) {

				/* chamada recursiva */
				executeCommandByConsole();
				
			} else if (x == 0) {
				hostname = line;
				hostname = hostname.toUpperCase();

				try {
					
					rc.createConnection(hostname);
					
					/* obtendo dados do servidor */
					if ( RemoteConnection.getConnection() !=  null && rc.getConnection().isAuthenticationComplete()){
						executeCommandByConsole();						
					}
					
				} catch (Exception e) {
					System.out.println(e.getMessage()); 

					/* recursivo */
					executeCommandByConsole();
				}

				System.out.println("Digite um comando valido:");
			} else if (x != 0) {
				comando = line;
				new SshHelper().executeSSHCommand(comando);
//                conn.updateSO(hostname, TipoSoEnum.valueOf(super.tipoSO).getSO());
				System.out.println("Digite um comando valido:"); 
			}

			x++;
		}
	}

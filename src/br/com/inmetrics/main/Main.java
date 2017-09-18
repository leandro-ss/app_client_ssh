package br.com.inmetrics.main;

import java.util.HashMap;
import java.util.Map;

import br.com.inmetrics.console.ConsoleConnection;
import br.com.inmetrics.database.ConnectionSQL;
import br.com.inmetrics.putty.OpenSessionInPutty;
import br.com.inmetrics.ssh.RemoteConnection;
import br.com.inmetrics.ssh.SftpHelper;
import br.com.inmetrics.ssh.SshHelper;
import br.com.inmetrics.util.OperationEnum;
import br.com.inmetrics.util.TipoSoEnum;

public class Main {
	public static void imprimeManual() {
		
		
		System.out.println("\n######## Inmetrics SSH/SFTP ########");

		System.out.println("\nPara abrir o putty utilize os seguintes argumentos:");
		System.out.println("[0] putty");
		System.out.println("[1] hostname");
		System.out.println("Obs: � necess�rio que o aplicativo putty.exe esteja no diret�rio C:\\");
			
		System.out.println("\nPara SSH utilize os seguintes argumentos:");
		System.out.println("[0] ssh");
		System.out.println("[1] hostname");
		System.out.println("[2] command (para multimplos comandos, utilizar o separador ${sep} que pode ser alterado no arquivo resources/config.properties)");

		System.out.println("\nPara enviar um arquivo via SFTP utilize os seguintes argumentos:");
		System.out.println("[0] sftp_envia");
		System.out.println("[1] hostname");
		System.out.println("[2] caminho (path) do arquivo de origem");
		System.out.println("[3] caminho (path) do diretorio destino");

		System.out.println("\nPara receber um arquivo via SFTP utilize os seguintes argumentos:");
		System.out.println("[0] sftp_recebe");
		System.out.println("[1] hostname");
		System.out.println("[2] caminho (path) do arquivo de origem");
		System.out.println("[3] caminho (path) do diretorio destino");
		
		System.out.println("\n######## FIM ########\n");


	}
	
	public static void main(String[] args) throws Exception {
    			Integer size = args.length;
    			OperationEnum type = null;
    			String hostname=""; 
    			String command="";
    			String pathArquivoOrigem="";
    			String pathDiretorioDestino="";
    			ConnectionSQL conn = new ConnectionSQL();
    			Map <String, String> mapHostname = new HashMap<String, String>();
    			Map <String, String> mapHostnamePonte = new HashMap<String, String>();

    			if (size == 0) {
    				new ConsoleConnection().executeCommandByConsole();
    				
    			} else if (args[0].equals("man")) {
    				imprimeManual();
    				
    			} else if (size == 2) {
    				type = OperationEnum.valueOf(args[0]);
    				hostname = args[1].toUpperCase();
    				
    			} else if (size == 3) {
    				type = OperationEnum.valueOf(args[0]);
    				hostname = args[1].toUpperCase();
    				command = args[2];
    				
    			} else if (size == 4) {
    				type = OperationEnum.valueOf(args[0]);
    				hostname = args[1].toUpperCase();
    				pathArquivoOrigem = args[2];
    				pathDiretorioDestino = args[3];
    				
    			} else {
    				imprimeManual();
    				System.exit(0);
    			}

    			/* obtendo dados do servidor */
    			mapHostname = conn.getUserPassByHostname(hostname);

    			if (! mapHostname.get("HOSTNAME_PONTE").isEmpty()) {
    				mapHostnamePonte = conn.getUserPassByHostname(mapHostname.get("HOSTNAME_PONTE"));
    			}
    			String saidaConsole;

    			switch (type) {

    				case PUTTY :
    					saidaConsole = new OpenSessionInPutty().openPutty(hostname, mapHostname.get("USUARIO"),
    							mapHostname.get("SENHA"), mapHostname.get("HOSTNAME_PONTE"), mapHostnamePonte.get("USUARIO"), mapHostnamePonte.get("SENHA"));
    					break;
    				
    				case  SSH:
    				    if(command == null || command.isEmpty()){
    				        String tipoSO = new SshHelper().executeSSH2GetSO(hostname);
    				        conn.updateSO(hostname, TipoSoEnum.valueOf(tipoSO).getSO());
    				    }else{
    				        new SshHelper().executeSSHCommand(command, hostname);    
    				    }
    					break;

    				case UPDATE_DB:
                        //conn.updateSO(hostname, TipoSoEnum.valueOf(super.tipoSO).getSO());
    					//conn.recordAcess(hostname, FlagEnum.NO);
    					break;
    					
    				case  SFTP_SEND:
    					saidaConsole = new SftpHelper().sendFileToServer(hostname, pathArquivoOrigem, pathDiretorioDestino);
    					System.out.println( saidaConsole);
    					break;

    				case SFTP_RECEBE:
    					saidaConsole = new SftpHelper().getFileFromServer(hostname, pathArquivoOrigem, pathDiretorioDestino);
    					System.out.println(saidaConsole);
    					break;

    				default :
    					imprimeManual();
    			}

    			RemoteConnection.closeConnection();        
    }
}
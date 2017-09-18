package br.com.inmetrics.ssh;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.LocalPortForwarder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;



import br.com.inmetrics.database.ConnectionSQL;
import br.com.inmetrics.ssh.exception.AuthenticateWithPasswordExeption;
import br.com.inmetrics.util.HostnameUtil;
import br.com.inmetrics.util.Constant;


public class RemoteConnection {

	private static LocalPortForwarder localPortForwarder;
	protected static Connection         connection;
	
    protected String                    host;
    protected String                    tipoSO;
    protected static final Integer      port = 22;

    protected static final Map<String, Connection> poolConnections;
    
    static {
        poolConnections = new HashMap<String, Connection>();
        
    }

    public static void closeConnection() throws IOException {
        if(getLocalPortForwarder() != null)getLocalPortForwarder().close();
        if(getConnection() != null)getConnection().close();
    }

    public void createConnection(String host) throws Exception {
        
        ConnectionSQL conn = new ConnectionSQL();
        this.host = host;

        if (!poolConnections.containsKey(host)) {

            
            /* obtendo dados da conexao remota */
            Map<String, String> mapHost = conn.getUserPassByHostname(host);

            if (!mapHost.isEmpty()) {

                /* verificando se o servidor remoto necessita de ponte */
                if (mapHost.get("hostnamePonte ") != null) {

                    /*
                     * obtendo dados do servidor que ser� utilizado como ponte
                     */
                    Map<String, String> mapPonte = conn.getUserPassByHostname(mapHost.get("hostnamePonte "));

                    if (!mapPonte.isEmpty()) {
                        // /* abrindo conex�o com a ponte */
                        createConnection(mapPonte.get("hostname"), mapPonte.get("user"), mapPonte.get("pass"), port);

                        // /* criando conex�o via t�nel */
                        createTunnel(mapHost.get("hostname"), mapHost.get("user"), mapHost.get("pass"), port);

                    } else {

                        createConnection(mapHost.get("hostname"), mapHost.get("user"), mapHost.get("pass"), port);

                    }
                }
            }
        }
    }
    
    public void createConnection(String hostname, String user, String pass, Integer port) throws Exception {
        createConnection(hostname, user, pass, port, null);
    }

    public void createConnection(String hostname, String user, String pass, Integer port, String remoteHostname) throws IOException, AuthenticateWithPasswordExeption{
        
        try {

            setConnection(new Connection(hostname, port));
            getConnection().connect();

            hostname = HostnameUtil.removeDomain(hostname);
            hostname = hostname.equals("localhost") ? host : hostname;

            if (!getConnection().authenticateWithPassword(user, pass)) {
                throw new AuthenticateWithPasswordExeption("Erro na autenticacao do servidor ${hostname}! Usuario ou senha invalidos...\n");
            }
            
        } catch (IOException e) {
            throw new IOException("Erro na conexao com o servidor ${hostname}! Mensagem: " + e.getMessage() + "\n");
        }        
        /* coloco a conexao pool de conexoes ativas */
        poolConnections.put(hostname, getConnection());
    }

    public void createTunnel(String hostname, String user, String pass, Integer port) throws IOException, AuthenticateWithPasswordExeption {
        try {
            setLocalPortForwarder(getConnection().createLocalPortForwarder(Constant.PORT_TUNNEL, hostname, port));
            getConnection().requestRemotePortForwarding(Constant.LOCALHOST, Constant.PORT_TUNNEL, hostname, port);
            createConnection(Constant.LOCALHOST, user, pass, Constant.PORT_TUNNEL, hostname);
        } catch (IOException e) {
            System.out.println("Erro na criacao do tunel para o servidor ${hostname}! Mensagem:" + e.getMessage() + "\n");
        }
    }

	public static Connection getConnection() {
		return connection;
	}

	public static void setConnection(Connection connection) {
		RemoteConnection.connection = connection;
	}

    public static LocalPortForwarder getLocalPortForwarder() {
        return localPortForwarder;
    }

    public static void setLocalPortForwarder(LocalPortForwarder localPortForwarder) {
        RemoteConnection.localPortForwarder = localPortForwarder;
    }

}
package br.com.monitoring.ssh.getters;

import br.com.monitoring.ssh.writers.Writer;

public interface Getter {
    
    public void execute( String userName, String password, String hostname, Integer port, String command, Writer writer) throws Exception ;


}
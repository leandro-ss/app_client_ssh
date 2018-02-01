package br.com.monitoring.ssh.getters;

import br.com.monitoring.ssh.writers.Writer;
import com.jcraft.jsch.Session;

public interface Getter {
    
    public void execute( Session sesConnection, String command, Writer writer) throws Exception ;


}
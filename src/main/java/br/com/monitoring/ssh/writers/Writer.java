package br.com.monitoring.ssh.writers;

public interface Writer {
    
    public void execute(Object[] objArray) throws Exception;
}
package br.com.inmetrics.properties;

import java.io.File;
import java.util.ResourceBundle;


public class InProperties {
	
    private static ResourceBundle                   commands;
    private static ResourceBundle                   config;
    
    static {

        setCommands(ResourceBundle.getBundle("commands"));
        

        for (AlternativeConfig alternativePath :  AlternativeConfig.values()){
        	File file = new File(alternativePath.getPath());
        	
        	if(file.exists()){
        		setConfig(ResourceBundle.getBundle(file.getAbsolutePath()));
        	}else {
        		setConfig(ResourceBundle.getBundle("configs"));		
        	}
        }

        
        for (AlternativeCommand alternativePath :  AlternativeCommand.values()){
        	File file = new File(alternativePath.getPath());
        	
        	if(file.exists()){
        		setCommands(ResourceBundle.getBundle(file.getAbsolutePath()));
        	}else {
        		setCommands(ResourceBundle.getBundle("commands"));		
        	}
        }
    }

    /* UNIX */
    public enum AlternativeConfig{
    	
    	UNIX_CONFIG ("/perf_01/coleta/config/resources/config.properties"),
    	WIN_CONFIG ("C:\\config\\resources\\commands.properties");
    	
    	private String path;
    	
    	AlternativeConfig(String path){
    		this.setPath(path);
    	}
		public String getPath() {
			return path;
		}
		
		private void setPath(String path) {
			this.path = path;
		}
    }
    
    public enum AlternativeCommand{
    	
    	UNIX_COMMANDS ("/perf_01/coleta/config/resources/commands.properties"),
    	WIN_COMMANDS ("C:\\config\\resources\\config.properties");
    	
    	private String path;
    	AlternativeCommand(String path){
    		this.setPath(path);
    	}
		public String getPath() {
			return path;
		}
		
		private void setPath(String path) {
			this.path = path;
		}
    }

	public static ResourceBundle getCommands() {
		return commands;
	}

	private static void setCommands(ResourceBundle commands) {
		InProperties.commands = commands;
	}

	public static ResourceBundle getConfig() {
		return config;
	}

	private static void setConfig(ResourceBundle config) {
		InProperties.config = config;
	}	
}
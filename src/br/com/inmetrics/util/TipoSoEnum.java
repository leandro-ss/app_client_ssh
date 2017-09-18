package br.com.inmetrics.util;


public enum TipoSoEnum {
	HPU("HP-UX"),
	LNX("Linux"),
	TRU("OSF1"),
	SUN("SunOS"),
	AIX("AIX");
    
    private String so;
	
	private TipoSoEnum(String so) {
		this.so = so;
	}
	
	public String getSO(){
	    return so;
	}
	
}
	
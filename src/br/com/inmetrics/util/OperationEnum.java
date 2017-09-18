package br.com.inmetrics.util;

public enum OperationEnum{
	
	
	PUTTY("putty"),
	SSH("ssh"),
	UPDATE_DB("atualiza_db"),
	SFTP_SEND("sftp_envia"),
	SFTP_RECEBE("sftp_recebe");
	
	private String operation;
	
	private OperationEnum(String operation){
		this.setOperation(operation);
	}

	public String getOperation() {
		return operation;
	}

	private void setOperation(String operation) {
		this.operation = operation;
	}
}


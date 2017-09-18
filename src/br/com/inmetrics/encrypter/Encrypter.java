package br.com.inmetrics.encrypter;

public interface Encrypter {
	public String criptografar(byte[] key, String value) throws EncryptorException;
	public String descriptografar(byte[] key, String encrypted) throws EncryptorException;
}

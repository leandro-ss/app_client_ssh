package br.com.inmetrics.main;

import br.com.inmetrics.encrypter.EncrypterImpl;
import br.com.inmetrics.encrypter.EncryptorException;

public class TesteCriptografia {

	public static void main(String[] args) throws EncryptorException {
		
		EncrypterImpl encrypter = new EncrypterImpl();
		
		String cript = encrypter.criptografar(EncrypterImpl.CHAVE, "intim#2011");
		System.out.println(cript);
		
		String decript = encrypter.descriptografar(EncrypterImpl.CHAVE, cript);
		System.out.println(decript);

	}

}

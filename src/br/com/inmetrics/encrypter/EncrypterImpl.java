package br.com.inmetrics.encrypter;

import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.spec.SecretKeySpec;


 
public class EncrypterImpl implements Encrypter {
 
    private static final String METODO_ENCRIPTACAO = "AES";
    public static final byte[] CHAVE = {-108, 116, -9, 9, 78, 117, -113, -116, 69, 122, -4, 27, 124, 29, -64, -20};
 
    public String criptografar(byte[] key, String value)       throws EncryptorException {
 
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, METODO_ENCRIPTACAO);
 
            Cipher cipher = Cipher.getInstance(METODO_ENCRIPTACAO);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
           
            return Base64.encodeBase64(value.getBytes()).toString();
        }
        catch (Exception e) {
            throw new EncryptorException("Erro ao criptografar informa��es " + e.getMessage());
        }
    }
 
    public String descriptografar(byte[] key, String encrypted)
            throws EncryptorException {
 
        byte[] decrypted = null;
 
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key, METODO_ENCRIPTACAO);
 
            byte[] decoded = Base64.decodeBase64(encrypted);
            

            Cipher cipher = Cipher.getInstance(METODO_ENCRIPTACAO);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            decrypted = cipher.doFinal(decoded);
        }
        catch (Exception e) {
            throw new EncryptorException("Erro ao descriptografar:" + e.getMessage());
        }
 
        return new String(decrypted);
    }
}
package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class SenhaHash {
	
	 public static String encriptografarSenha(String senha) {
		 
	        try {
	            MessageDigest md = MessageDigest.getInstance("md5");
	            
	            byte[] hashedPassword = md.digest(senha.getBytes());
	            
	            return Base64.getEncoder().encodeToString(hashedPassword);
	            
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("Erro ao encriptografar a senha", e);
	        }
	 }
}

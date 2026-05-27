package utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
    public static String hashSHA384(String password){
        if (password == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-384");
            byte[] hashBytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Алгоритм SHA-384 не найден", e);
        }
    }
}

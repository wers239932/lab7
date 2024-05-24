package client;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.DataTruncation;

public class Encrypter {
    private static String pepper = "-35g35rgj38";
    public static String encrypt(String line) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(pepper.getBytes());
            byte[] messageDigest = md.digest(line.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashText = no.toString(16);
            while (hashText.length() < 32) {
                hashText = "0" + hashText;
            }
            return hashText;
        }
       catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}

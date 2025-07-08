package utils;

import java.security.MessageDigest;

public class HashTool {
    public static void main(String[] args) throws Exception {
        String password = "yourPlainPassword";
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder hex = new StringBuilder();
        for (byte b : hash) {
            hex.append(String.format("%02x", b));
        }
        System.out.println(hex.toString());
    }
}

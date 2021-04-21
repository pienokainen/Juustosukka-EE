package com.example.juustosukka_ee;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHash {


    public static String getPassWordHash(String toHash, String salt)
    {
        for (int i = 0; i < 100000; i++) {
            toHash = SHA512once(toHash+salt);
        }
        return SHA512once(toHash);
    }
    private static String SHA512once(String toHash) {
        MessageDigest mes_dig;
        String hash = toHash;
        try
        {
            mes_dig= MessageDigest.getInstance("SHA-512");
            mes_dig.update(hash.getBytes());
            byte[] mb = mes_dig.digest();
            String out = "";
            for (int i = 0; i < mb.length; i++) {
                byte temp = mb[i];
                String s = Integer.toHexString(new Byte(temp));
                while (s.length() < 2)
                {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }
            return(out);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("ERROR: " + e.getMessage());
        }
        return "error";
    }

    public static String salt(String toSalt, String salt)
    {
        return "";
    }

}

/*
 * Copyright (C) 2011 Byron Nevins
 */
package com.elf.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Byron Nevins
 */
public class Hasher {

    public static void main(String[] args) {
        for(String hashee : args) {
            System.out.println(new Hasher(hashee).toString());
        }
    }
    private byte[] hashMD5Bytes;
    private byte[] hashSHA256Bytes;
    private String hashMD5String;
    private String hashSHA256String;
    private String hashee;

    public Hasher(String hashee) {
        try {
            this.hashee = hashee;
            byte[] inBytes = hashee.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            hashMD5Bytes = md5.digest(inBytes);
            hashSHA256Bytes = sha256.digest(inBytes);
            
            setHumanReadable();
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Hasher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setHumanReadable() {
        StringBuilder sb = new StringBuilder();

        for (byte b : getHashMD5Bytes())
            sb.append(String.format("%02x", b));

        hashMD5String = sb.toString();

        sb = new StringBuilder();

        for (byte b : getHashSHA256Bytes())
            sb.append(String.format("%02x", b));

        hashSHA256String = sb.toString();
    }

    public byte[] getHashMD5Bytes() {
        return hashMD5Bytes;
    }

    public byte[] getHashSHA256Bytes() {
        return hashSHA256Bytes;
    }

    public String getHashMD5String() {
        return hashMD5String;
    }

    public String getHashSHA256String() {
        return hashSHA256String;
    }
    @Override
    public String toString() {
        return hashee + "\nMD5: " + hashMD5String + "\nSHA-256: " + hashSHA256String;
    }
}

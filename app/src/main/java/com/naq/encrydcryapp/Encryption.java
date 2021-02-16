package com.naq.encrydcryapp;
// This activity of code is the implementation of both encryption algorithms:
// 1. AES AND THE SECOND ONE IS 2.BLOWFISH.

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Encryption {

    // variables initialization
    private String algorithmType;
    private String vaultPath;
    private String decryptedPath;

    // parameterize constructor for Alog_type,vaultpath(where encrypted file store),Decryptpath(where decrypted file is store)
    public Encryption(String ALGOTHERM_TYPE, String vaultPath, String decryptedPath) {

        this.algorithmType = ALGOTHERM_TYPE;
        this.vaultPath = vaultPath;
        this.decryptedPath = decryptedPath;
    }
    // AES to follow 16 byte of code
    public byte[] getIV() {
        byte[] iv = null;
        // SecureRandom random = new SecureRandom();
        if (algorithmType.equalsIgnoreCase("AES")) {
            iv = "1234567891234567".getBytes();
            // Blowfish follow 8 byte of code
        } else if (algorithmType.equalsIgnoreCase("BLOWFISH")) {
            iv = "12345678".getBytes();

        }
        return iv;
    }
    // code for encrypting the file
    public void encrypt(byte[] key, String filepath) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        // FileInputStream is meant for reading streams of raw bytes such as image data.
        final FileInputStream fileInputStream = new FileInputStream(filepath);


        // filepath for storing
        String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
        String vaultPath = "/storage/emulated/0/Vault/";


        final FileOutputStream fileOutputStream = new FileOutputStream(vaultPath + filename);
        // This class specifies an initialization vector (IV).
        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIV());

        // Construct a SecretKey from a byte array, without having to go through a (provider-based) SecretKeyFactory.
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,
                algorithmType);
        // This class provides the functionality of a cryptographic cipher for encryption and decryption.
        // It forms the core of the Java Cryptographic Extension (JCE) framework. A transformation always includes the name of a cryptographic algorithm
        Cipher cipher = Cipher.getInstance(algorithmType + "/CBC/PKCS5PADDING");

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        // convert the file  into an encrypted form
        final CipherOutputStream cipherOutputStream = new CipherOutputStream(fileOutputStream, cipher);
        // Write bytes




        int b;
        byte[] d = new byte[1024];
        long progress = 1024;
        try {
            while ((b = fileInputStream.read(d)) != -1) {
                cipherOutputStream.write(d, 0, b);


            }


            cipherOutputStream.flush();
            cipherOutputStream.close();
            fileInputStream.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // using this code we decrypt the file
    public void decrypt(byte[] key, String filepath) throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {

        // filepath
        String filename = filepath.substring(filepath.lastIndexOf("/") + 1);
        String inputPath = vaultPath + filename;
        //FileInputStream is meant for reading streams of raw bytes such as image data.
        final FileInputStream fileInputStream = new FileInputStream(inputPath);

        String fileoutputpath = decryptedPath + filename;

        // FileOutputStream show the file in decrypted form
        final FileOutputStream fileOutputStream = new FileOutputStream(fileoutputpath);

        // This class specifies an initialization vector (IV).
        IvParameterSpec ivParameterSpec = new IvParameterSpec(getIV());

        // It can be used to construct a SecretKey from a byte array, without having to go through a (provider-based) SecretKeyFactory.
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,
                algorithmType);

        // This class provides the functionality of a cryptographic cipher for encryption and decryption.
        // It forms the core of the Java Cryptographic Extension (JCE) framework. A transformation always includes the name of a cryptographic algorithm
        Cipher cipher = Cipher.getInstance(algorithmType + "/CBC/PKCS5PADDING");

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

        final CipherInputStream cipherInputStream = new CipherInputStream(fileInputStream, cipher);


        byte[] d = new byte[1024];
        int b;
        try {
            while ((b = cipherInputStream.read(d)) != -1) {
                fileOutputStream.write(d, 0, b);

            }
            fileOutputStream.flush();
            fileOutputStream.close();
            cipherInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public byte[] hashGenerator(String pass) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, UnsupportedEncodingException, InvalidKeyException {

        byte[] keysecret = (pass).getBytes("UTF-8");
        // MessageDigest sha = MessageDigest.getInstance("SHA-256"); // couldn't make it work using SHA-256
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] key = messageDigest.digest(keysecret);
        return key;
    }

}
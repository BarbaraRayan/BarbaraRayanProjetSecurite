/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryptages;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.util.Base64;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Heraxia
 */
public class PC {

    static ServerSocket server;
    static int clientID = 0;

    public static void main(String ard[]) {

        try {
            System.out.println("Genération d'une clef de chiffrement...");
            //We create a text file named "key"
            File f = new File("key.txt");
            //We generate a key
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            String k2w;
            byte[] encoded = key.getEncoded();
            k2w = Base64.getEncoder().encodeToString(encoded);
            //We save the generated key in the text file
            FileWriter fw = new FileWriter(f.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(k2w);
            bw.close();
            fw.close();
            System.out.println("Clef: " + k2w);
            server = new ServerSocket(4444, 5);
            go();
        } catch (Exception e) {
        }
    }
    
    //Method starting the server
    public static void go() {

        try {
            Thread t = new Thread(new Runnable() {
                public void run() {
                    while (true)
                    {
                        try {
                            Socket client = server.accept();
                            Thread tAccueil = new Thread(new ChatServer(client, clientID));
                            tAccueil.start();
                            clientID++;
                        } catch (Exception e) {
                        }
                    }
                }
            });
            t.start();
        } catch (Exception i) {
            System.out.println("Impossible d'ecouter sur le port 4444: serait-il occupe?");
            i.printStackTrace();
        }

    }
    
    //Method decrypting the message
    public static String decrypt(final String encryptedMessage, String encodedKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] decryptedMessage = Base64.getDecoder().decode(encryptedMessage);
        byte[] key = Base64.getDecoder().decode(encodedKey);
        SecretKey cle = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, cle);
        return new String(cipher.doFinal(decryptedMessage));
    }
    
    //Method encrypting the message
    public static byte[] encrypt(final String message, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedMessage = message.getBytes();
        return cipher.doFinal(encryptedMessage);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryptages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Heraxia
 */
public class ChatServer implements Runnable {

    PrintWriter out;
    BufferedReader in;
    Socket s;
    Scanner keyboard;
    int index;
    String input;
    boolean doRun = true;
    static SecretKey key;

    public ChatServer(Socket a, int u) {
        s = a;
        keyboard = new Scanner(System.in);
        index = u;
    }

    public static String decrypt(final String encodedMessage, String encodedKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        byte[] donnees = Base64.getDecoder().decode(encodedMessage);
        byte[] key = Base64.getDecoder().decode(encodedKey);
        SecretKey cle = new SecretKeySpec(key, 0, key.length, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, cle);
        return new String(cipher.doFinal(donnees));
    }

    public void run() {
        File f = new File("key.txt");
        FileReader fr;
        try {
            fr = new FileReader(f.getAbsoluteFile());
            BufferedReader br = new BufferedReader(fr);
            String cle = br.readLine();
            br.close();
            fr.close();

            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream());

                System.out.println("connexion de " + s.getInetAddress().toString() + " sur le port " + s.getPort());
                String talk = in.readLine();

                while (doRun) {
                    while (talk == null) {
                        talk = in.readLine();
                    }
                    System.out.println("Encoded message : " + talk);
                    String dec = decrypt(talk, cle);
                    System.out.println("Decoded message: " + dec);
                    if (talk.compareToIgnoreCase("bye") == 0) {
                        System.out.println("Shutting down following remote request");
                        doRun = false;
                    } else {
                        System.out.print("To client#" + index + "> ");
                        input = keyboard.nextLine();

                        out.println(input);
                        out.flush();
                        if (input.compareToIgnoreCase("Bye") == 0) {
                            System.out.println("Server shutting down");
                            doRun = false;
                        } else {
                            talk = in.readLine();
                        }
                    }
                }
                s.close();
            } catch (Exception e) {
                System.out.println("Raaah! what did u forget this time?");
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

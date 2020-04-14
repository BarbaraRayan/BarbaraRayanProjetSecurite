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
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 *
 * @author Heraxia
 */
public class PB {

    public static byte[] encrypt(final String message, SecretKey key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] donnees = message.getBytes();
        return cipher.doFinal(donnees);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, FileNotFoundException, IOException {
        InetAddress addr;
        Socket client;
        PrintWriter out;
        BufferedReader in;
        String input;
        String userInput;
        boolean doRun = true;
        //We recover and read the key present in the text file
        File f = new File("key.txt");
        FileReader fr = new FileReader(f.getAbsoluteFile());
        BufferedReader br = new BufferedReader(fr);
        String cle = br.readLine();
        br.close();
        fr.close();
        Scanner k = new Scanner(System.in);
        try {
            client = new Socket("localhost", 4444);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
            byte[] key = Base64.getDecoder().decode(cle);
            SecretKey key2 = new SecretKeySpec(key, 0, key.length, "AES");
            System.out.print("Enter the message that needs to be crypted: ");
            userInput = k.nextLine();
            byte[] u = encrypt(userInput, key2);
            String output = Base64.getEncoder().encodeToString(u);
            out.println(output);
            out.flush();
            if (userInput.compareToIgnoreCase("Bye") == 0) {
                System.out.println("Shutting down");
                doRun = false;
            } else {
                while (doRun) {
                    input = in.readLine();
                    while (input == null) {
                        input = in.readLine();
                    }

                    System.out.println(input);
                    if (input.compareToIgnoreCase("Bye") == 0) {
                        System.out.println("Client shutting down from server request");
                        doRun = false;
                    } else {
                        System.out.print("Enter your message: ");
                        userInput = k.nextLine();
                        out.flush();
                        if (userInput.compareToIgnoreCase("Bye") == 0) {
                            System.out.println("Shutting down");
                            doRun = false;
                        }

                    }
                }
            }
            client.close();
            k.close();
        } catch (UnknownHostException e) {
        } catch (IOException ioe) {
        }
    }
}

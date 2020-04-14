/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Cryptages;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Heraxia
 */
public class Cryptage {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {

        String messageToEncrypt = "a";

        try {
            //We create a key
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            // We choose method AES in order to encode message
            Cipher cipher = Cipher.getInstance("AES");
            //We enter the encoding phase of the message
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //We transform the encoded message from String to Byte
            byte[] m1 = cipher.doFinal(messageToEncrypt.getBytes());
            //We transform the encoded message from Byte to String. Here we have a coded message that needs the key to be read
            String encryptedMessage = Base64.getEncoder().encodeToString(m1);
            //We enter the decoding phase of the message
            cipher.init(Cipher.DECRYPT_MODE, key);
            //We decode the encoded message
            byte[] m2 = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
            //We display the decoded message
            String decryptedMessage = new String(m2);
            //We display the message sent at the beggin
            System.out.println("Message to encrypt:" + messageToEncrypt);
            //We display the encoded message
            System.out.println("Encrypted message:" + encryptedMessage);
            //We display the decoded message
            System.out.println("Decrypted message:" + decryptedMessage);
            //We recover the key 
            byte[] keyByte = key.getEncoded();
            //We display the key
            System.out.println(Base64.getEncoder().encodeToString(keyByte));
        } catch (Exception ex) {
        }

    }

}

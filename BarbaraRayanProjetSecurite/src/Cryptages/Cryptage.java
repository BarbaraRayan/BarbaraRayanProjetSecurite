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
    String encryptedValue;
    String secKey;

    public Cryptage(String encryptedValue, String secKey) {
        this.encryptedValue = encryptedValue;
        this.secKey = secKey;
    }

    public static void main(String[] args) {

        String messageToEncode = "a";

        try {
            //We create a key
            SecretKey key = KeyGenerator.getInstance("AES").generateKey();
            // We choose method AES in order to encode message
            Cipher cipher = Cipher.getInstance("AES");
            //We enter the encoding phase of the message
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //We transform the encoded message from String to Byte
            byte[] res = cipher.doFinal(messageToEncode.getBytes());
            //We transform the encoded message from Byte to String. Here we have a coded message that needs the key to be read
            String codedMessage = Base64.getEncoder().encodeToString(res);
            //We enter the decoding phase of the message
            cipher.init(Cipher.DECRYPT_MODE, key);
            //We decode the encoded message
            byte[] res2 = cipher.doFinal(Base64.getDecoder().decode(codedMessage));
            //We display the decoded message
            String decodedMessage = new String(res2);
            //We display the message sent at the beggin
            System.out.println("Message to code:" + messageToEncode);
            //We display the encoded message
            System.out.println("Encoded message:" + codedMessage);
            //We display the decoded message
            System.out.println("Decoded message:" + decodedMessage);
            //We recover the key 
            byte[] keyByte = key.getEncoded();
            //We display the key
            System.out.println(Base64.getEncoder().encodeToString(keyByte));
        } catch (Exception ex) {
        }

    }

}

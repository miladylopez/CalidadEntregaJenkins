package edu.unac;

import edu.unac.exception.ConsecutiveCharacterException;
import edu.unac.exception.DuplicateSaltException;
import edu.unac.exception.InvalidLengthException;
import edu.unac.exception.RepeatedCharacterException;

import java.security.SecureRandom;

public class Main {
    //prueba jenkins 1 github
    //prueba jenkins 2 github
    //prueba jenkins 3 github
    //prueba jenkins 4 github
    //prueba jenkins 5 github
    //fwsgfvsfskahffha

    public static void main(String[] args) {
        SimpleRandomProvider secureRandom = new SimpleRandomProvider();
        SaltGenerator saltGenerator = new SaltGenerator(secureRandom);

        int length = 8; // Length of the salt
        try {
            String salt = saltGenerator.generateSalt(length);
            System.out.println("Generated Salt: " + salt);
        } catch (RepeatedCharacterException | ConsecutiveCharacterException | InvalidLengthException |
                 DuplicateSaltException e) {
            System.err.println("Error generating salt: " + e.getMessage());
        }
    }
}
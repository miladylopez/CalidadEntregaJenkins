package edu.unac;

import edu.unac.exception.ConsecutiveCharacterException;
import edu.unac.exception.DuplicateSaltException;
import edu.unac.exception.InvalidLengthException;
import edu.unac.exception.RepeatedCharacterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SaltGeneratorTest {
    private SaltGenerator saltGenerator;

    @Mock
    private SimpleRandomProvider mockRandomProvider;

    @BeforeEach
    void setUp() {
        mockRandomProvider = mock(SimpleRandomProvider.class);
        saltGenerator = new SaltGenerator(mockRandomProvider);
    }

    @Test
    void generateSaltWithValidLength() throws Exception {
        when(mockRandomProvider.nextInt(62)).thenReturn(0, 3, 5, 22, 18, 10, 25, 9);

        String salt = saltGenerator.generateSalt(8);
        assertNotNull(salt);
        assertEquals(8, salt.length());
        assertEquals("ADFWSKZJ", salt); // Based on mapped CHARACTERS indexes
    }

    @Test
    void generateSaltWithInvalidLength() {
        InvalidLengthException exception = assertThrows(InvalidLengthException.class, () -> {
            saltGenerator.generateSalt(2);
        });

        assertEquals("The length must be greater than 2.", exception.getMessage());
    }

    @Test
    void generateSaltWithRepeatedCharacters() {
        when(mockRandomProvider.nextInt(62)).thenReturn(0, 0, 0); // Intentionally produce "AAA"

        RepeatedCharacterException exception = assertThrows(RepeatedCharacterException.class, () -> {
            saltGenerator.generateSalt(3);
        });

        assertEquals("The salt contains 3 consecutively repeated characters: A", exception.getMessage());
    }

    @Test
    void generateSaltWithRepeatedCharactersOnlyTwo() throws ConsecutiveCharacterException, InvalidLengthException, DuplicateSaltException, RepeatedCharacterException {
        when(mockRandomProvider.nextInt(62)).thenReturn(0, 0, 1); // Intentionally produce "AAA"

        String salt = saltGenerator.generateSalt(3);
        assertNotNull(salt);
        assertEquals(3, salt.length());
        assertEquals("AAB", salt);
    }

    @Test
    void generateSaltWithConsecutiveCharacters() {
        when(mockRandomProvider.nextInt(62)).thenReturn(0, 1, 2); // Intentionally produce "ABC"

        ConsecutiveCharacterException exception = assertThrows(ConsecutiveCharacterException.class, () -> {
            saltGenerator.generateSalt(3);
        });

        assertEquals("The salt contains 3 consecutive characters: A, B, C", exception.getMessage());
    }

    @Test
    void generateSaltWithConsecutiveCharactersOnlyTwo() throws ConsecutiveCharacterException, InvalidLengthException, DuplicateSaltException, RepeatedCharacterException {
        when(mockRandomProvider.nextInt(62)).thenReturn(0, 1, 3); // Intentionally produce "AAA"

        String salt = saltGenerator.generateSalt(3);
        assertNotNull(salt);
        assertEquals(3, salt.length());
        assertEquals("ABD", salt);
    }

    @Test
    void generateSaltWithDuplicateSalt() throws Exception {
        when(mockRandomProvider.nextInt(62)).thenReturn(0, 3, 5, 22, 18, 10, 25, 9);

        String salt = saltGenerator.generateSalt(8);
        assertNotNull(salt);
        assertEquals("ADFWSKZJ", salt);

        when(mockRandomProvider.nextInt(62)).thenReturn(0, 3, 5, 22, 18, 10, 25, 9); // Same salt

        DuplicateSaltException exception = assertThrows(DuplicateSaltException.class, () -> {
            saltGenerator.generateSalt(8);
        });

        assertEquals("The salt has already been generated previously: ADFWSKZJ", exception.getMessage());
    }

    @Test
    void generateDifferentSalts() throws Exception {
        when(mockRandomProvider.nextInt(62))
                .thenReturn(0, 3, 5, 22, 18, 10, 25, 9)  // First salt "AdFmwKjJ"
                .thenReturn(1, 4, 6, 23, 19, 11, 26, 8);  // Second salt "BeGnxLkI"

        String salt1 = saltGenerator.generateSalt(8);
        String salt2 = saltGenerator.generateSalt(8);

        assertNotEquals(salt1, salt2);
        assertEquals("ADFWSKZJ", salt1);
        assertEquals("BEGXTLaI", salt2);
    }
}

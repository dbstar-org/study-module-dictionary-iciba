package io.github.dbstarll.study.dictionary.iciba;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class WordModelTest {
    @Test
    void isCri() {
        final WordModel word = new WordModel();
        assertFalse(word.isCri());

        word.setCri(1);
        assertTrue(word.isCri());

        word.setCri(2);
        assertFalse(word.isCri());

        word.setCri(0);
        assertFalse(word.isCri());
    }
}
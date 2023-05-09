package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.study.entity.Word;
import io.github.dbstarll.study.entity.enums.PartKey;
import io.github.dbstarll.study.entity.enums.PhoneticKey;
import io.github.dbstarll.study.entity.ext.Part;
import io.github.dbstarll.study.entity.ext.Phonetic;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class DictionaryUtilsTest {
    @Test
    void parseWordNoCri() throws IOException {
        assertNull(DictionaryUtils.parseWord(new WordModel()));
    }

    @Test
    void parseNullExchangeAndSymbols() throws IOException {
        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("word");

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNull(word.getExchanges());
        assertNull(word.getParts());
        assertNull(word.getPhonetics());
    }

    @Test
    void parseEmptySymbol() throws IOException {
        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("word");

        final SymbolModel symbolModel = new SymbolModel();
        wordModel.setSymbols(Collections.singletonList(symbolModel));

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNull(word.getParts());
        assertNull(word.getPhonetics());
    }

    @Test
    void parseEmptyMp3() throws IOException {
        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("word");

        final SymbolModel symbolModel = new SymbolModel();
        symbolModel.setAm("word2");
        wordModel.setSymbols(Collections.singletonList(symbolModel));

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNull(word.getParts());
        assertNotNull(word.getPhonetics());
        assertEquals(1, word.getPhonetics().size());
        final Phonetic phonetic = word.getPhonetics().iterator().next();
        assertNotNull(phonetic);
        assertEquals(PhoneticKey.AM, phonetic.getKey());
        assertEquals("word2", phonetic.getSymbol());
        assertNull(phonetic.mp3());
    }

    @Test
    void parseMp3() throws IOException {
        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("fly");

        final SymbolModel symbolModel = new SymbolModel();
        symbolModel.setAm("flai");
        symbolModel.setAmMp3("http://res.iciba.com/resource/amp3/1/0/af/17/af17bc3b4a86a96a0f053a7e5f7c18ba.mp3");
        wordModel.setSymbols(Collections.singletonList(symbolModel));

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNull(word.getParts());
        assertNotNull(word.getPhonetics());
        assertEquals(1, word.getPhonetics().size());
        final Phonetic phonetic = word.getPhonetics().iterator().next();
        assertNotNull(phonetic);
        assertEquals(PhoneticKey.AM, phonetic.getKey());
        assertEquals("flai", phonetic.getSymbol());
        assertNotNull(phonetic.mp3());
        assertEquals(16088, phonetic.mp3().length);
    }

    @Test
    void parseParts() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("fly");

        final SymbolModel symbolModel = new SymbolModel();
        final PartModel partModel = new PartModel();
        partModel.setPart("vi.");
        partModel.setMeans(Collections.singletonList(mapper.getNodeFactory().textNode("abc")));
        symbolModel.setParts(Collections.singletonList(partModel));
        wordModel.setSymbols(Collections.singletonList(symbolModel));

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNotNull(word.getParts());
        assertEquals(1, word.getParts().size());
        final Part part = word.getParts().iterator().next();
        assertEquals(Collections.singletonList(PartKey.VI), part.getKey());
        assertEquals(Collections.singletonList("abc"), part.getMeans());
    }

    @Test
    void parsePartsKeyWithoutDot() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("fly");

        final SymbolModel symbolModel = new SymbolModel();
        final PartModel partModel = new PartModel();
        partModel.setPart("vi");
        partModel.setMeans(Collections.singletonList(mapper.getNodeFactory().textNode("abc")));
        symbolModel.setParts(Collections.singletonList(partModel));
        wordModel.setSymbols(Collections.singletonList(symbolModel));

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNull(word.getParts());
    }

    @Test
    void parsePartsNoMeans() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();

        final WordModel wordModel = new WordModel();
        wordModel.setCri(1);
        wordModel.setName("fly");

        final SymbolModel symbolModel = new SymbolModel();
        final PartModel partModel = new PartModel();
        partModel.setPart("vi.");
        symbolModel.setParts(Collections.singletonList(partModel));
        wordModel.setSymbols(Collections.singletonList(symbolModel));

        final Word word = DictionaryUtils.parseWord(wordModel);
        assertNotNull(word);
        assertNull(word.getParts());
    }
}
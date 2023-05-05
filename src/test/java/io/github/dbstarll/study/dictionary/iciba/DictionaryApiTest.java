package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.study.entity.Word;
import io.github.dbstarll.study.entity.enums.ExchangeKey;
import io.github.dbstarll.study.entity.enums.PartKey;
import io.github.dbstarll.study.entity.enums.PhoneticKey;
import io.github.dbstarll.study.entity.ext.Exchange;
import io.github.dbstarll.study.entity.ext.Part;
import io.github.dbstarll.study.entity.ext.Phonetic;
import io.github.dbstarll.utils.http.client.HttpClientFactory;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 测试DictionaryApi.
 */
class DictionaryApiTest {
    private void useApi(ThrowingConsumer<DictionaryApi> consumer) throws Throwable {
        try (CloseableHttpClient client = new HttpClientFactory().build()) {
            consumer.accept(new DictionaryApi(client, new ObjectMapper(), "7A39163AF82AEEC16FD0CC5F5BDFBE16"));
        }
    }

    @Test
    void empty() throws Throwable {
        useApi(api -> {
            final Exception e = assertThrowsExactly(IllegalArgumentException.class, () -> api.query(""));
            assertEquals("word not set", e.getMessage());
        });
    }

    @Test
    void whitespace() throws Throwable {
        useApi(api -> {
            final Exception e = assertThrowsExactly(IllegalArgumentException.class, () -> api.query("super man"));
            assertEquals("word contains whitespace: super man", e.getMessage());
        });
    }

    @Test
    void query() throws Throwable {
        useApi(api -> {
            final Word word = api.query("fly");
            assertNotNull(word);
            assertTrue(word.isCri());
            assertEquals(5, word.getExchanges().size());
            assertEquals(new HashSet<>(Arrays.asList(ExchangeKey.PL, ExchangeKey.THIRD, ExchangeKey.ING, ExchangeKey.DONE, ExchangeKey.PAST)),
                    word.getExchanges().stream().map(Exchange::getKey).collect(Collectors.toSet()));
            assertEquals(2, word.getPhonetics().size());
            assertEquals(new HashSet<>(Arrays.asList(PhoneticKey.AM, PhoneticKey.EN)),
                    word.getPhonetics().stream().map(Phonetic::getKey).collect(Collectors.toSet()));
            assertEquals(4, word.getParts().size());
            assertEquals(new HashSet<>(Arrays.asList(
                    Collections.singletonList(PartKey.VI),
                    Collections.singletonList(PartKey.N),
                    Collections.singletonList(PartKey.VT),
                    Arrays.asList(PartKey.VT, PartKey.VI))),
                    word.getParts().stream().map(Part::getKey).collect(Collectors.toSet()));
        });
    }

    @Test
    void queryNoCri() throws Throwable {
        useApi(api -> assertNull(api.query("忆苦思甜")));
    }
}
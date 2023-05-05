package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.study.classify.exchange.ExchangeUtils;
import io.github.dbstarll.study.entity.Word;
import io.github.dbstarll.study.entity.enums.ExchangeKey;
import io.github.dbstarll.study.entity.enums.PartKey;
import io.github.dbstarll.study.entity.enums.PhoneticKey;
import io.github.dbstarll.study.entity.ext.Exchange;
import io.github.dbstarll.study.entity.ext.Part;
import io.github.dbstarll.study.entity.ext.Phonetic;
import io.github.dbstarll.utils.http.client.request.RelativeUriResolver;
import io.github.dbstarll.utils.json.jackson.JsonApiClient;
import io.github.dbstarll.utils.lang.enums.EnumUtils;
import io.github.dbstarll.utils.lang.wrapper.IterableWrapper;
import io.github.dbstarll.utils.net.api.ApiException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.Validate.notBlank;

public final class DictionaryApi extends JsonApiClient {
    private final String key;

    /**
     * 构建DictionaryApi.
     *
     * @param httpClient   HttpClient
     * @param objectMapper ObjectMapper
     * @param key          key
     */
    public DictionaryApi(final HttpClient httpClient, final ObjectMapper objectMapper, final String key) {
        super(httpClient, true, objectMapper);
        setUriResolver(new RelativeUriResolver("http://dict-co.iciba.com"));
        this.key = notBlank(key, "key not set");
    }

    @Override
    protected ClassicRequestBuilder preProcessing(final ClassicRequestBuilder builder) throws ApiException {
        return super.preProcessing(builder)
                .addParameter("type", "json")
                .addParameter("key", key);
    }

    /**
     * 查询网络词典并组装成Word对象.
     *
     * @param word 待查的单词
     * @return 单词对应的Word对象，如果没有匹配的释义，则返回null
     * @throws IOException 网络异常
     */
    public Word query(final String word) throws IOException, ApiException {
        if (StringUtils.containsWhitespace(notBlank(word, "word not set"))) {
            throw new IllegalArgumentException("word contains whitespace: " + word);
        }
        return parseWord(execute(get("/api/dictionary.php")
                .addParameter("w", word.toLowerCase())
                .build(), JsonNode.class));
    }

    private static Word parseWord(final JsonNode node) throws IOException {
        final JsonNode cri = node.get("is_CRI");
        if (cri != null) {
            final Word word = EntityFactory.newInstance(Word.class);
            word.setCri(cri.asInt() == 1);
            word.setName(node.get("word_name").asText().trim());
            word.setExchanges(parseExchanges(word.getName(), node.get("exchange")));
            final JsonNode symbols = node.path("symbols");
            if (symbols.isArray() && symbols.size() > 0) {
                final JsonNode symbol = symbols.get(0);
                word.setPhonetics(parsePhonetics(symbol));
                word.setParts(parseParts(symbol.get("parts")));
            }
            return word;
        }
        return null;
    }

    private static Set<Exchange> parseExchanges(final String word, final JsonNode node) {
        final Set<Exchange> exchanges = new HashSet<>();
        if (node != null && !node.isNull()) {
            for (Map.Entry<String, JsonNode> entry : IterableWrapper.wrap(node.fields())) {
                final String key = entry.getKey();
                final JsonNode exchangeNode = entry.getValue();
                if (key.startsWith("word_") && exchangeNode.isArray() && exchangeNode.size() > 0) {
                    final ExchangeKey exchangeKey = EnumUtils.valueOf(ExchangeKey.class, key.substring(5));
                    final String exchangeValue = exchangeNode.get(0).asText().trim();
                    exchanges.add(new Exchange(exchangeKey, exchangeValue,
                            ExchangeUtils.classify(exchangeKey, word, exchangeValue)));
                }
            }
        }
        return exchanges.isEmpty() ? null : exchanges;
    }

    private static Set<Phonetic> parsePhonetics(final JsonNode node) throws IOException {
        final Set<Phonetic> phonetics = new HashSet<>();
        if (node != null && !node.isNull()) {
            for (PhoneticKey key : PhoneticKey.values()) {
                final JsonNode symbolNode = node.get("ph_" + EnumUtils.name(key));
                if (symbolNode != null && !symbolNode.isNull()) {
                    final String symbol = symbolNode.asText().trim();
                    if (StringUtils.isNotBlank(symbol)) {
                        final Phonetic phonetic = new Phonetic(key, symbol);
                        final JsonNode urlNode = node.get("ph_" + EnumUtils.name(key) + "_mp3");
                        if (urlNode != null && !urlNode.isNull()) {
                            final String url = urlNode.asText();
                            if (StringUtils.isNotBlank(url)) {
                                phonetic.mp3(IOUtils.toByteArray(new URL(url)));
                            }
                        }
                        phonetics.add(phonetic);
                    }
                }
            }
        }
        return phonetics.isEmpty() ? null : phonetics;
    }

    private static Set<Part> parseParts(final JsonNode node) {
        final Set<Part> parts = new HashSet<>();
        if (node != null && node.isArray()) {
            for (JsonNode partNode : node) {
                final String key = partNode.get("part").asText();
                final JsonNode meansNode = partNode.get("means");
                if (key.endsWith(".") && meansNode != null && meansNode.isArray() && meansNode.size() > 0) {
                    final List<PartKey> partKey = parsePartKey(key);
                    final List<String> means = new ArrayList<>(meansNode.size());
                    for (JsonNode mean : meansNode) {
                        means.add(mean.asText().trim());
                    }
                    parts.add(new Part(partKey, means));
                }
            }
        }
        return parts.isEmpty() ? null : parts;
    }

    private static List<PartKey> parsePartKey(final String key) {
        final List<PartKey> keys = new ArrayList<>();
        for (String k : StringUtils.split(key
                .replaceAll("[. ]", "")
                .replace('-', '_'), '&')) {
            keys.add(EnumUtils.valueOf(PartKey.class, k));
        }
        return keys;
    }
}

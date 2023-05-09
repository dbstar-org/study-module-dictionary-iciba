package io.github.dbstarll.study.dictionary.iciba;

import io.github.dbstarll.dubai.model.entity.EntityFactory;
import io.github.dbstarll.study.classify.exchange.ExchangeUtils;
import io.github.dbstarll.study.entity.Word;
import io.github.dbstarll.study.entity.enums.ExchangeKey;
import io.github.dbstarll.study.entity.enums.PartKey;
import io.github.dbstarll.study.entity.enums.PhoneticKey;
import io.github.dbstarll.study.entity.ext.Exchange;
import io.github.dbstarll.study.entity.ext.Part;
import io.github.dbstarll.study.entity.ext.Phonetic;
import io.github.dbstarll.utils.lang.enums.EnumUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class DictionaryUtils {
    private DictionaryUtils() {
        // 禁止实例化
    }

    static Word parseWord(final WordModel model) throws IOException {
        if (model.isCri()) {
            final Word word = EntityFactory.newInstance(Word.class);
            word.setName(model.getName());
            word.setCri(model.isCri());
            word.setExchanges(parseExchanges(word.getName(), model.getExchange()));
            if (CollectionUtils.isNotEmpty(model.getSymbols())) {
                final SymbolModel symbol = model.getSymbols().get(0);
                word.setPhonetics(parsePhonetics(symbol));
                word.setParts(parseParts(symbol.getParts()));
            }
            return word;
        } else {
            return null;
        }
    }

    private static Set<Exchange> parseExchanges(final String word, final ExchangeModel exchangeModel) {
        final Set<Exchange> exchanges = new HashSet<>();
        if (exchangeModel != null) {
            parseExchange(word, exchanges, ExchangeKey.PL, exchangeModel.getPl());
            parseExchange(word, exchanges, ExchangeKey.THIRD, exchangeModel.getThird());
            parseExchange(word, exchanges, ExchangeKey.PAST, exchangeModel.getPast());
            parseExchange(word, exchanges, ExchangeKey.DONE, exchangeModel.getDone());
            parseExchange(word, exchanges, ExchangeKey.ING, exchangeModel.getIng());
            parseExchange(word, exchanges, ExchangeKey.ER, exchangeModel.getEr());
            parseExchange(word, exchanges, ExchangeKey.EST, exchangeModel.getEst());
        }
        return exchanges.isEmpty() ? null : exchanges;
    }

    private static void parseExchange(final String word, final Set<Exchange> exchanges,
                                      final ExchangeKey key, final String exchange) {
        if (StringUtils.isNotBlank(exchange)) {
            exchanges.add(new Exchange(key, exchange, ExchangeUtils.classify(key, word, exchange)));
        }
    }

    private static Set<Phonetic> parsePhonetics(final SymbolModel symbolModel) throws IOException {
        final Set<Phonetic> phonetics = new HashSet<>();
        parsePhonetic(phonetics, PhoneticKey.AM, symbolModel.getAm(), symbolModel.getAmMp3());
        parsePhonetic(phonetics, PhoneticKey.EN, symbolModel.getEn(), symbolModel.getEnMp3());
        return phonetics.isEmpty() ? null : phonetics;
    }

    private static void parsePhonetic(final Set<Phonetic> phonetics, final PhoneticKey key,
                                      final String symbol, final String mp3) throws IOException {
        if (StringUtils.isNotBlank(symbol)) {
            final Phonetic phonetic = new Phonetic(key, symbol);
            if (StringUtils.isNotBlank(mp3)) {
                phonetic.mp3(IOUtils.toByteArray(new URL(mp3)));
            }
            phonetics.add(phonetic);
        }
    }

    private static Set<Part> parseParts(final List<PartModel> partModels) {
        final Set<Part> parts = new HashSet<>();
        if (CollectionUtils.isNotEmpty(partModels)) {
            for (PartModel partModel : partModels) {
                final String key = partModel.getPart();
                final List<String> means = partModel.getMeans();
                if (StringUtils.endsWith(key, ".") && CollectionUtils.isNotEmpty(means)) {
                    final List<PartKey> partKey = parsePartKey(key);
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

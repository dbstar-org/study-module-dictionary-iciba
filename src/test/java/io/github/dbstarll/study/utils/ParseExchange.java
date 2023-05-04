package io.github.dbstarll.study.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dbstarll.study.classify.exchange.ExchangeUtils;
import io.github.dbstarll.study.entity.enums.ExchangeKey;
import io.github.dbstarll.utils.lang.enums.EnumUtils;
import io.github.dbstarll.utils.lang.line.LineValidator;
import io.github.dbstarll.utils.lang.line.Lines;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class ParseExchange {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String file = "/Users/dbstar/git/github.com/dbstar-org/study-module/exchanges.jsonl";

    public static void main(String[] args) throws IOException {
        for (ExchangeKey key : ExchangeKey.values()) {
            parseExchangeKey(key);
        }
    }

    private static void parseExchangeKey(final ExchangeKey key) throws IOException {
        final String match = EnumUtils.name(key);
        final Set<String> classifies = new HashSet<>();
        for (String line : Lines.open(new File(file), StandardCharsets.UTF_8, LineValidator.ALL)) {
            final JsonNode json = mapper.readTree(line);
            final String word = json.get("name").asText();
            for (JsonNode node : json.get("exchanges")) {
                if (match.equals(node.get("key").asText())) {
                    final String exchange = node.get("word").asText();
                    final String classify = ExchangeUtils.classify(key, word, exchange);
//                    if (classifies.add(classify)) {
                    System.out.printf("%s\t%s\t%s\t%s\n", match, classify, word, exchange);
//                    }
                }
            }
        }
    }
}

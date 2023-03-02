package io.github.dbstarll.study.classify.exchange;

import io.github.dbstarll.study.entity.enums.ExchangeKey;

import java.util.HashMap;
import java.util.Map;

public final class ExchangeUtils {
  private static final Map<ExchangeKey, ExchangeClassifier> classifiers = new HashMap<>();

  static {
    classifiers.put(ExchangeKey.PL, new PlExchangeClassifier());
    classifiers.put(ExchangeKey.ING, new IngExchangeClassifier());
    classifiers.put(ExchangeKey.DONE, new DoneExchangeClassifier());
    classifiers.put(ExchangeKey.THIRD, classifiers.get(ExchangeKey.PL));
    classifiers.put(ExchangeKey.PAST, classifiers.get(ExchangeKey.DONE));
    classifiers.put(ExchangeKey.ER, new ErExchangeClassifier());
    classifiers.put(ExchangeKey.EST, new EstExchangeClassifier());
  }

  private ExchangeUtils() {
  }

  /**
   * 获得词态变化的分类.
   *
   * @param key      ExchangeKey
   * @param word     word
   * @param exchange exchange
   * @return 词态变化的分类
   */
  public static String classify(ExchangeKey key, String word, String exchange) {
    final ExchangeClassifier classifier = classifiers.get(key);
    if (classifier != null) {
      return classifier.classify(word, exchange);
    }
    return null;
  }
}

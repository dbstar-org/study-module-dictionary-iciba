package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import com.fasterxml.jackson.databind.type.LogicalType;
import io.github.dbstarll.study.entity.Word;
import io.github.dbstarll.utils.http.client.request.RelativeUriResolver;
import io.github.dbstarll.utils.json.jackson.JsonApiClient;
import io.github.dbstarll.utils.net.api.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;

import java.io.IOException;

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
        super(httpClient, true, customize(objectMapper));
        setUriResolver(new RelativeUriResolver("http://dict-co.iciba.com"));
        this.key = notBlank(key, "key not set");
    }

    private static ObjectMapper customize(final ObjectMapper objectMapper) {
        final ObjectMapper copy = objectMapper.copy();
        copy.coercionConfigFor(LogicalType.Array).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsEmpty);
        return copy;
    }

    @Override
    protected ClassicRequestBuilder preProcessing(final ClassicRequestBuilder builder) throws ApiException {
        return super.preProcessing(builder).addParameter("key", key);
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
        return DictionaryUtils.parseWord(execute(get("/api/dictionary.php")
                .addParameter("type", "json")
                .addParameter("w", word.toLowerCase())
                .build(), WordModel.class));
    }
}

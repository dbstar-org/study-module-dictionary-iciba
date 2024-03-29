package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.StringJoiner;

final class WordModel {
    @JsonProperty("word_name")
    private String name;

    @JsonProperty("is_CRI")
    private boolean cri;

    private ExchangeModel exchange;

    private List<SymbolModel> symbols;

    String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    boolean isCri() {
        return cri;
    }

    public void setCri(final int cri) {
        this.cri = cri == 1;
    }

    ExchangeModel getExchange() {
        return exchange;
    }

    public void setExchange(final ExchangeModel exchange) {
        this.exchange = exchange;
    }

    List<SymbolModel> getSymbols() {
        return symbols;
    }

    public void setSymbols(final List<SymbolModel> symbols) {
        this.symbols = symbols;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", WordModel.class.getSimpleName() + "[", "]")
                .add("name='" + getName() + "'")
                .add("cri=" + isCri())
                .add("exchange=" + getExchange())
                .add("symbols=" + getSymbols())
                .toString();
    }
}

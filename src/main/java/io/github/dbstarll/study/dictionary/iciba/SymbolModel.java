package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.StringJoiner;

@JsonIgnoreProperties({"word_symbol", "symbol_mp3"})
public final class SymbolModel {
    @JsonProperty("ph_en")
    private String en;
    @JsonProperty("ph_am")
    private String am;
    @JsonProperty("ph_other")
    private String other;
    @JsonProperty("ph_en_mp3")
    private String enMp3;
    @JsonProperty("ph_am_mp3")
    private String amMp3;
    @JsonProperty("ph_tts_mp3")
    private String ttsMp3;

    private List<PartModel> parts;

    public String getEn() {
        return en;
    }

    public void setEn(final String en) {
        this.en = en;
    }

    public String getAm() {
        return am;
    }

    public void setAm(final String am) {
        this.am = am;
    }

    public String getOther() {
        return other;
    }

    public void setOther(final String other) {
        this.other = other;
    }

    public String getEnMp3() {
        return enMp3;
    }

    public void setEnMp3(final String enMp3) {
        this.enMp3 = enMp3;
    }

    public String getAmMp3() {
        return amMp3;
    }

    public void setAmMp3(final String amMp3) {
        this.amMp3 = amMp3;
    }

    public String getTtsMp3() {
        return ttsMp3;
    }

    public void setTtsMp3(final String ttsMp3) {
        this.ttsMp3 = ttsMp3;
    }

    public List<PartModel> getParts() {
        return parts;
    }

    public void setParts(final List<PartModel> parts) {
        this.parts = parts;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SymbolModel.class.getSimpleName() + "[", "]")
                .add("en='" + getEn() + "'")
                .add("am='" + getAm() + "'")
                .add("other='" + getOther() + "'")
                .add("enMp3='" + getEnMp3() + "'")
                .add("amMp3='" + getAmMp3() + "'")
                .add("ttsMp3='" + getTtsMp3() + "'")
                .add("parts=" + getParts())
                .toString();
    }
}

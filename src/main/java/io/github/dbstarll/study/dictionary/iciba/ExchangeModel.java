package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.ArrayUtils;

import java.util.StringJoiner;

final class ExchangeModel {
    @JsonProperty("word_pl")
    private String pl;
    @JsonProperty("word_third")
    private String third;
    @JsonProperty("word_past")
    private String past;
    @JsonProperty("word_done")
    private String done;
    @JsonProperty("word_ing")
    private String ing;
    @JsonProperty("word_er")
    private String er;
    @JsonProperty("word_est")
    private String est;

    String getPl() {
        return pl;
    }

    public void setPl(final String... values) {
        this.pl = first(values);
    }

    String getThird() {
        return third;
    }

    public void setThird(final String... values) {
        this.third = first(values);
    }

    String getPast() {
        return past;
    }

    public void setPast(final String... values) {
        this.past = first(values);
    }

    String getDone() {
        return done;
    }

    public void setDone(final String... values) {
        this.done = first(values);
    }

    String getIng() {
        return ing;
    }

    public void setIng(final String... values) {
        this.ing = first(values);
    }

    String getEr() {
        return er;
    }

    public void setEr(final String... values) {
        this.er = first(values);
    }

    String getEst() {
        return est;
    }

    public void setEst(final String... values) {
        this.est = first(values);
    }

    private String first(final String... values) {
        return ArrayUtils.isNotEmpty(values) ? values[0] : null;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ExchangeModel.class.getSimpleName() + "[", "]")
                .add("pl='" + getPl() + "'")
                .add("third='" + getThird() + "'")
                .add("past='" + getPast() + "'")
                .add("done='" + getDone() + "'")
                .add("ing='" + getIng() + "'")
                .add("er='" + getEr() + "'")
                .add("est='" + getEst() + "'")
                .toString();
    }
}

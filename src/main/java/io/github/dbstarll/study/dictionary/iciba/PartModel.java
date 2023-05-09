package io.github.dbstarll.study.dictionary.iciba;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@JsonIgnoreProperties("part_name")
public final class PartModel {
    private String part;
    private List<String> means;

    public String getPart() {
        return part;
    }

    public void setPart(final String part) {
        this.part = part;
    }

    public List<String> getMeans() {
        return means;
    }

    public void setMeans(final List<JsonNode> means) {
        this.means = means.stream().filter(JsonNode::isTextual).map(JsonNode::asText).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PartModel.class.getSimpleName() + "[", "]")
                .add("part='" + getPart() + "'")
                .add("means=" + getMeans())
                .toString();
    }
}

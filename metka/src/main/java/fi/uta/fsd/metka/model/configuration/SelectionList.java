package fi.uta.fsd.metka.model.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.uta.fsd.metka.data.enums.SelectionListType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties("_comment")
public class SelectionList {
    @XmlElement private final String key;
    @XmlElement(name = "default") @JsonProperty("default") private String def = "0";
    @XmlElement private final List<Option> options = new ArrayList<>();
    @XmlElement private SelectionListType type;
    @XmlElement private String reference;
    @XmlElement private Boolean includeEmpty = true;
    @XmlElement private final List<String> freeText = new ArrayList<>();
    @XmlElement private String freeTextKey;

    @JsonCreator
    public SelectionList(@JsonProperty("key") String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public List<Option> getOptions() {
        return options;
    }

    public SelectionListType getType() {
        return type;
    }

    public void setType(SelectionListType type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getIncludeEmpty() {
        return (includeEmpty == null) ? false : includeEmpty;
    }

    public void setIncludeEmpty(Boolean includeEmpty) {
        this.includeEmpty = (includeEmpty == null) ? false : includeEmpty;
    }

    public List<String> getFreeText() {
        return freeText;
    }

    public String getFreeTextKey() {
        return freeTextKey;
    }

    public void setFreeTextKey(String freeTextKey) {
        this.freeTextKey = freeTextKey;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SelectionList that = (SelectionList) o;

        if (!key.equals(that.key)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }

    @Override
    public String toString() {
        String out = "selectionList["+key+"], options[";
        for(Option option : options) {
            out += option;
            out += ", ";
        }
        out += "]";
        return out;
    }
}
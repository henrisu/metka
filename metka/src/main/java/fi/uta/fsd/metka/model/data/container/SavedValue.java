package fi.uta.fsd.metka.model.data.container;

import fi.uta.fsd.metka.model.data.value.Value;
import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class SavedValue {
    @XmlElement private DateTime savedAt;
    @XmlElement private String savedBy;
    @XmlElement private Value value;

    public DateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(DateTime savedAt) {
        this.savedAt = savedAt;
    }

    public String getSavedBy() {
        return savedBy;
    }

    public void setSavedBy(String savedBy) {
        this.savedBy = savedBy;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return (value == null) ? null : value.toString();
    }

    public SavedValue copy() {
        SavedValue saved = new SavedValue();
        saved.setSavedAt(new DateTime(savedAt));
        saved.setSavedBy(savedBy);
        saved.value = value.copy();
        return saved;
    }
}
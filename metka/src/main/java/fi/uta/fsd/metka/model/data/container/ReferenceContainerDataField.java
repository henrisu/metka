package fi.uta.fsd.metka.model.data.container;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.ArrayList;
import java.util.List;

/**
 * List of references are saved through this
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ReferenceContainerDataField extends DataField {
    @XmlElement private final List<SavedReference> references = new ArrayList<>();

    @JsonCreator
    public ReferenceContainerDataField(@JsonProperty("key") String key) {
        super(key);
    }

    public List<SavedReference> getReferences() {
        return references;
    }

    @JsonIgnore public SavedReference getReference(Integer rowId) {

        if(rowId == null || rowId < 1) {
            // Row can not be found since no rowId given.
            return null;
        }
        for(SavedReference reference : references) {
            if(reference.getRowId().equals(rowId)) {
                return reference;
            }
        }
        // Given rowId was not found from this container
        return null;
    }

}

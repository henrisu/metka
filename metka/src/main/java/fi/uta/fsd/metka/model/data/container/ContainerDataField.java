package fi.uta.fsd.metka.model.data.container;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fi.uta.fsd.metka.enums.Language;
import fi.uta.fsd.metka.model.access.calls.ValueDataFieldCall;
import fi.uta.fsd.metka.model.access.enums.StatusCode;
import fi.uta.fsd.metka.model.data.change.Change;
import fi.uta.fsd.metka.model.data.change.ContainerChange;
import fi.uta.fsd.metka.model.data.change.RowChange;
import fi.uta.fsd.metka.model.data.value.Value;
import fi.uta.fsd.metka.model.general.DateTimeUserPair;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContainerDataField extends RowContainerDataField {
    /**
     * Contains rows for each language specified in Language enumerator.
     * Use of this must be careful
     */
    private final Map<Language, List<DataRow>> rows = new HashMap<>();

    @JsonCreator
    public ContainerDataField(@JsonProperty("key") String key, @JsonProperty("type") DataFieldType type, @JsonProperty("rowIdSeq") Integer rowIdSeq) {
        super(key, type, rowIdSeq);
    }

    public Map<Language, List<DataRow>> getRows() {
        return rows;
    }

    @JsonIgnore public List<DataRow> getRowsFor(Language language) {
        return rows.get(language);
    }

    @JsonIgnore public boolean hasRowsFor(Language language) {
        return rows.get(language) != null && !rows.get(language).isEmpty();
    }

    @JsonIgnore public boolean hasRows() {
        for(Language language : Language.values()) {
            if(hasRowsFor(language)) return true;
        }
        return false;
    }

    /**
     * Inserts a row to given language if row with same id doesn't exist yet in any language
     * @param language
     * @param row
     */
    @JsonIgnore public void addRow(Language language, DataRow row) {
        if(getRowWithId(row.getRowId()) != null) {
            // Can't add second row with same id. Possibly should inform user but this should never happen anyway
            return;
        }
        if(rows.get(language) == null) {
            rows.put(language, new ArrayList<DataRow>());
        }
        rows.get(language).add(row);
    }

    /**
     * Creates a new row and inserts it to this ContainerDataField and adds a change to changeMap
     * @param language For which the row is inserted
     * @param containerChange ContainerChange that will contain the change for the new row
     * @return
     */
    public Pair<StatusCode, DataRow> insertNewDataRow(Language language, ContainerChange containerChange) {
        if(language == null || containerChange == null) {
            return new ImmutablePair<>(StatusCode.INCORRECT_PARAMETERS, null);
        }

        DataRow row = DataRow.build(this);
        containerChange.put(language, new RowChange(row.getRowId()));
        containerChange.setChangeIn(language);
        addRow(language, row);
        return new ImmutablePair<>(StatusCode.NEW_ROW, row);
    }

    public Pair<StatusCode, DataRow> getRowWithIdFrom(Language language, Integer rowId) {
        if(!hasRowsFor(language)) {
            return new ImmutablePair<>(StatusCode.NO_ROW_WITH_ID, null);
        }
        for(DataRow row : getRowsFor(language)) {
            if(row.getRowId().equals(rowId)) {
                return new ImmutablePair<>(StatusCode.FOUND_ROW, row);
            }
        }
        return new ImmutablePair<>(StatusCode.NO_ROW_WITH_ID, null);
    }

    /**
     * Searches all languages for a row.
     * Since rowId is unique across all languages in a certain container there is
     * no need to define a language when searching with rowId
     * @param rowId Row id to be searched for amongst rows
     * @return DataRow matching given value or null if none found
     */
    public Pair<StatusCode, DataRow> getRowWithId(Integer rowId) {

        if(rowId == null || rowId < 1) {
            // Row can not be found since no rowId given.
            return new ImmutablePair<>(StatusCode.INCORRECT_PARAMETERS, null);
        }
        for(Language language : Language.values()) {
            Pair<StatusCode, DataRow> rowPair = getRowWithIdFrom(language, rowId);
            if(rowPair.getLeft() == StatusCode.FOUND_ROW) {
                return rowPair;
            }
        }
        // Given rowId was not found from this container
        return new ImmutablePair<>(StatusCode.NO_ROW_WITH_ID, null);
    }

    /**
     * Searches through a list of rows for a row containing given value in a field with given id.
     * Language needs to be defined since rows in different languages can have the same field value
     * and only the first according to Language.getValues() would ever be found.
     * NOTICE: null values are newer considered equal so you can't find rows with null field value
     * @param language The language for which the search is performed
     * @param key Field key of field where value should be found
     * @param value Value that is searched for
     * @return DataRow that contains given value in requested field, or null if not found.
     */
    public Pair<StatusCode, DataRow> getRowWithFieldValue(Language language, String key, Value value) {
        if(!hasRowsFor(language)) {
            return new ImmutablePair<>(StatusCode.NO_ROW_WITH_VALUE, null);
        }
        for(DataRow row : rows.get(language)) {
            Pair<StatusCode, ValueDataField> pair = row.dataField(ValueDataFieldCall.get(key));
            if(pair.getLeft() != StatusCode.FIELD_FOUND) {
                continue;
            }
            ValueDataField field = pair.getRight();
            if(field.valueForEquals(language, value.getValue())) {
                return new ImmutablePair<>(StatusCode.FOUND_ROW, row);
            }
        }
        return new ImmutablePair<>(StatusCode.NO_ROW_WITH_VALUE, null);
    }

    /**
     * Uses getRowWithFieldValue to search for existing row with given value in a given field.
     * If row is not found creates a new row and inserts it to the list.
     * Since it can be assumed that it's desirable to find the field with the given value from the rows list
     * the field is created on the row with the given value
     * Language needs to be defined since rows in different languages can have the same field value
     * and only the first according to Language.getValues() would ever be found and we need to know to which
     * language to create the row.
     * @param language The language for which the search is performed and where row is created
     * @param key Field key of the field where the value should be found
     * @param value Value that is searched for
     * @param changeMap Map where the container change containing this rows changes should reside
     * @param info DateTimeUserPair for possible creation of row and field. Can be null
     * @return Tuple of StatusCode and DataRow. StatusCode tells if the returned row is a new insert or not
     */
    public Pair<StatusCode, DataRow> getOrCreateRowWithFieldValue(Language language, String key, Value value, Map<String, Change> changeMap, DateTimeUserPair info) {
        if(changeMap == null || !value.hasValue()) {
            return new ImmutablePair<>(StatusCode.INCORRECT_PARAMETERS, null);
        }

        Pair<StatusCode, DataRow> pair = getRowWithFieldValue(language, key, value);
        if(pair.getLeft() == StatusCode.FOUND_ROW) {
            return pair;
        } else {
            if(info == null) {
                info = DateTimeUserPair.build();
            }
            DataRow row = DataRow.build(this);
            addRow(language, row);

            row.dataField(ValueDataFieldCall.set(key, value, language).setInfo(info).setChangeMap(changeMap));
            return new ImmutablePair<>(StatusCode.NEW_ROW, row);
        }
    }

    @Override
    public DataField copy() {
        ContainerDataField container = new ContainerDataField(getKey(), DataFieldType.CONTAINER, getRowIdSeq());
        for(Language language : Language.values()) {
            if(!hasRowsFor(language)) {
                continue;
            }
            if(hasRowsFor(language)) {
                for(DataRow row : rows.get(language)) {
                    container.addRow(language, row.copy());
                }
            }
        }
        return container;
    }

    @Override
    public void normalize() {
        List<DataRow> remove = new ArrayList<>();
        // If row is removed mark it for removal, otherwise normalize row
        for(Language language : Language.values()) {
            if(!hasRowsFor(language)) {
                continue;
            }
            remove.clear();
            List<DataRow> languageRows = getRowsFor(language);
            for(DataRow row : languageRows) {
                if(row.getRemoved()) {
                    remove.add(row);
                } else {
                    row.normalize();
                }
            }

            // Remove all rows marked for removal
            for(DataRow row : remove) {
                languageRows.remove(row);
            }
        }
    }
}

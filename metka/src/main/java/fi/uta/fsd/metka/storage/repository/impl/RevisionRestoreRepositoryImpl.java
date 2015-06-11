package fi.uta.fsd.metka.storage.repository.impl;

import fi.uta.fsd.metka.enums.ConfigurationType;
import fi.uta.fsd.metka.enums.Language;
import fi.uta.fsd.metka.model.access.calls.ReferenceContainerDataFieldCall;
import fi.uta.fsd.metka.model.access.calls.ValueDataFieldCall;
import fi.uta.fsd.metka.model.access.enums.StatusCode;
import fi.uta.fsd.metka.model.data.RevisionData;
import fi.uta.fsd.metka.model.data.container.ReferenceContainerDataField;
import fi.uta.fsd.metka.model.data.container.ReferenceRow;
import fi.uta.fsd.metka.model.data.container.ValueDataField;
import fi.uta.fsd.metka.names.Fields;
import fi.uta.fsd.metka.storage.entity.RevisionableEntity;
import fi.uta.fsd.metka.storage.repository.RevisionRepository;
import fi.uta.fsd.metka.storage.repository.RevisionRestoreRepository;
import fi.uta.fsd.metka.storage.repository.enums.RemoveResult;
import fi.uta.fsd.metka.storage.repository.enums.ReturnResult;
import fi.uta.fsd.metka.storage.response.RevisionableInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Restores removed revisionables back to usage.
 * Needs special permissions to use but those are checked on the interface
 * Successful result is SUCCESS_RESTORE, all other results are failures of some sort
 */
@Repository
public class RevisionRestoreRepositoryImpl implements RevisionRestoreRepository {
    @PersistenceContext(name = "entityManager")
    private EntityManager em;

    @Autowired
    private RevisionRepository revisions;

    @Override
    public RemoveResult restore(Long id) {
        Pair<ReturnResult, RevisionableInfo> pair = revisions.getRevisionableInfo(id);
        if(pair.getLeft() != ReturnResult.REVISIONABLE_FOUND) {
            return RemoveResult.NOT_FOUND;
        }
        if(!pair.getRight().getRemoved()) {
            return RemoveResult.NOT_REMOVED;
        }
        RevisionableEntity entity = em.find(RevisionableEntity.class, id);
        entity.setRemoved(false);
        entity.setRemovedBy(null);
        entity.setRemovalDate(null);

        if(entity.getType().equals(ConfigurationType.STUDY.toValue())) {
            propagateStudyRestore(entity);
        } else if(entity.getType().equals(ConfigurationType.STUDY_VARIABLES.toValue())) {
            propagateStudyVariablesRestore(entity);
        }

        List<Integer> nos = revisions.getAllRevisionNumbers(id);
        for(Integer no : nos) {
            Pair<ReturnResult, RevisionData> revPair = revisions.getRevisionData(id, no);
            if(revPair.getLeft() == ReturnResult.REVISION_FOUND) {
                revisions.indexRevision(revPair.getRight().getKey());
            }
        }

        return RemoveResult.SUCCESS_RESTORE;
    }

    private void propagateStudyRestore(RevisionableEntity entity) {
        Pair<ReturnResult, RevisionData> study = revisions.getLatestRevisionForIdAndType(entity.getId(), false, ConfigurationType.STUDY);
        if(study.getLeft() != ReturnResult.REVISION_FOUND) {
            // Should never happen unless someone screws up the database manually
            return;
        }
        RevisionData data = study.getRight();
        // WARNING: This can't separate between removals done through study removal and earlier manual removal operations
        Pair<StatusCode, ValueDataField> variablesPair = data.dataField(ValueDataFieldCall.get(Fields.VARIABLES));
        if(variablesPair.getLeft() == StatusCode.FIELD_FOUND && variablesPair.getRight().hasValueFor(Language.DEFAULT)) {
            // Variables found
            Pair<ReturnResult, RevisionData> variables = revisions.getLatestRevisionForIdAndType(variablesPair.getRight().getValueFor(Language.DEFAULT).valueAsInteger(), false, ConfigurationType.STUDY_VARIABLES);
            if(variables.getLeft() == ReturnResult.REVISION_FOUND) {
                restore(variables.getRight().getKey().getId());
            }
        }
        Pair<StatusCode, ReferenceContainerDataField> attachmentsPair = data.dataField(ReferenceContainerDataFieldCall.get(Fields.FILES));
        if(attachmentsPair.getLeft() == StatusCode.FIELD_FOUND && !attachmentsPair.getRight().getReferences().isEmpty()) {
            // Attachments found
            for(ReferenceRow row : attachmentsPair.getRight().getReferences()) {
                Pair<ReturnResult, RevisionData> attachment = revisions.getLatestRevisionForIdAndType(row.getReference().asInteger(), false, ConfigurationType.STUDY_ATTACHMENT);
                if(attachment.getLeft() == ReturnResult.REVISION_FOUND) {
                    restore(attachment.getRight().getKey().getId());
                }
            }

        }
    }

    private void propagateStudyVariablesRestore(RevisionableEntity entity) {
        Pair<ReturnResult, RevisionData> variables = revisions.getLatestRevisionForIdAndType(entity.getId(), false, ConfigurationType.STUDY_VARIABLES);
        if(variables.getLeft() != ReturnResult.REVISION_FOUND) {
            // Should never happen unless someone screws up the database manually
            return;
        }
        RevisionData data = variables.getRight();
        Pair<StatusCode, ReferenceContainerDataField> variablesPair = data.dataField(ReferenceContainerDataFieldCall.get(Fields.VARIABLES));
        if(variablesPair.getLeft() == StatusCode.FIELD_FOUND && !variablesPair.getRight().getReferences().isEmpty()) {
            // Attachments found
            for(ReferenceRow row : variablesPair.getRight().getReferences()) {
                Pair<ReturnResult, RevisionData> variable = revisions.getLatestRevisionForIdAndType(row.getReference().asInteger(), false, ConfigurationType.STUDY_VARIABLE);
                if(variable.getLeft() == ReturnResult.REVISION_FOUND) {
                    restore(variable.getRight().getKey().getId());
                }
            }
        }
    }
}

package fi.uta.fsd.metka.mvc.services;

import fi.uta.fsd.metka.enums.ConfigurationType;
import fi.uta.fsd.metka.model.data.RevisionData;
import fi.uta.fsd.metka.storage.repository.VariablesRepository;
import fi.uta.fsd.metka.storage.repository.enums.ReturnResult;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VariablesService {
    @Autowired
    private VariablesRepository repository;

    @Autowired
    private GeneralService general;

    public Pair<ReturnResult, RevisionData> getVariablesByStudyId(Long id) {
        // We can get the variables object and the latest revision for that straight away
        return repository.getVariablesByStudyId(id);
    }

    public Pair<ReturnResult, RevisionData> getVariablesById(Long id) { // TODO: Set correct return type
        return general.getRevisionData(id, ConfigurationType.STUDY_VARIABLES);
    }

    public Pair<ReturnResult, RevisionData> getVariablesRevision(Long id, Integer no) {
        return general.getRevisionData(id, no, ConfigurationType.STUDY_VARIABLES);
    }

    public Pair<ReturnResult, RevisionData> getVariableById(Long id) { // TODO: Set correct return type
        return general.getRevisionData(id, ConfigurationType.STUDY_VARIABLE);
    }

    public Pair<ReturnResult, RevisionData> getVariableRevision(Long id, Integer no) {
        return general.getRevisionData(id, no, ConfigurationType.STUDY_VARIABLE);
    }

    public Pair<ReturnResult, RevisionData> getEditableVariables(Long id) {
        return repository.getEditableVariablesData(id);
        // Check whether variables is applicable for editing by the requesting user.
        // This can fail for example if there is no current DRAFT for the study the variables object is linked to

        // If so then try to get an editable revision for variables
        // This should automatically create a new DRAFT revision if one is not yet present
    }

    public Pair<ReturnResult, RevisionData> getEditableVariable(Long id) {
        // Check whether variable is applicable for editing by the requesting user.
        // This can fail for example if there is no current DRAFT for the study the variable is linked to

        // If so then try to get an editable revision for variable
        // This should automatically create a new DRAFT revision if one is not yet present

        return repository.getEditableVariableData(id);
    }

    public void saveVariables() { // TODO: Include needed parameters and set correct return type
        // Check whether variables object can be saved by the current user
        // This can fail for example if there is no current DRAFT for study or if the user is not the current handler

        // Save changes in variables object

        return;
    }

    public void saveVariable() { // TODO: Include needed parameters and set correct return type
        // Check whether variable object can be saved by the current user
        // This can fail for example if there is no current DRAFT for study or if the user is not the current handler

        // Save changes in variables object

        return;
    }

    // TODO: Handle removal of variables collection (which should be removed if the file from which the variables are brought is removed)
    //       as well as having DRAFTs being possible to remove. Also handle removal of individual variables where you can remove DRAFTs
    //       and perform logical removals, which should also remove the variable from groupings and variables list on study variables object
    //       as well as removing both DRAFT (if present) and logically removing the variable (if approved revisions exist) when removed from
    //       study variables objects variables list.
}
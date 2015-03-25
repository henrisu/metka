package fi.uta.fsd.metka.mvc.services;

import codebook25.CodeBookDocument;
import com.fasterxml.jackson.databind.JsonNode;
import fi.uta.fsd.metka.enums.Language;
import fi.uta.fsd.metka.model.transfer.TransferData;
import fi.uta.fsd.metka.storage.repository.enums.ReturnResult;
import fi.uta.fsd.metka.transfer.revision.RevisionSearchResponse;
import fi.uta.fsd.metka.transfer.settings.JSONListEntry;
import fi.uta.fsd.metka.transfer.study.StudyErrorsResponse;
import fi.uta.fsd.metka.transfer.study.StudyVariablesStudiesResponse;
import fi.uta.fsd.metkaAuthentication.Permission;
import fi.uta.fsd.metkaAuthentication.PermissionCheck;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;

@PreAuthorize("hasPermission('"+ Permission.Values.CAN_VIEW_REVISION +"', 'PERMISSION')")
@Transactional(readOnly = true)
public interface StudyService {
    StudyVariablesStudiesResponse collectStudiesWithVariables();

    RevisionSearchResponse collectAttachmentHistory(TransferData transferData);

    StudyErrorsResponse getStudiesWithErrors();

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_EXPORT_REVISION+"', '" + PermissionCheck.Values.PERMISSION + "')")
    Pair<ReturnResult, CodeBookDocument> exportDDI(Long id, Integer no, Language language);

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_IMPORT_REVISION+"', '" + PermissionCheck.Values.PERMISSION + "') " +
            "and hasPermission(#transferData, '" + PermissionCheck.Values.IS_HANDLER + "')")
    @Transactional(readOnly = false) ReturnResult importDDI(TransferData transferData, String path);

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_ADD_ORGANIZATIONS +"', 'PERMISSION')")
    String getOrganizations();

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_ADD_ORGANIZATIONS +"', 'PERMISSION')")
    @Transactional(readOnly = false) ReturnResult uploadOrganizations(JsonNode misc);
}

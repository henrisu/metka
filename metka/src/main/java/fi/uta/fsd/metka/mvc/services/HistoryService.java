package fi.uta.fsd.metka.mvc.services;

import fi.uta.fsd.metka.mvc.services.requests.ChangeCompareRequest;
import fi.uta.fsd.metka.mvc.services.simple.history.ChangeCompareSO;
import fi.uta.fsd.metka.mvc.services.simple.history.RevisionSO;
import fi.uta.fsd.metkaAuthentication.Permission;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@PreAuthorize("hasPermission('"+ Permission.Values.CAN_VIEW_REVISION +"', 'PERMISSION')")
public interface HistoryService {
    List<RevisionSO> getRevisionHistory(Long id);

    ChangeCompareSO compareRevisions(ChangeCompareRequest request);
}

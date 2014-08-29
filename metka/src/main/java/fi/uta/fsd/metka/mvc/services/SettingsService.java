package fi.uta.fsd.metka.mvc.services;

import fi.uta.fsd.metka.storage.repository.enums.ReturnResult;
import fi.uta.fsd.metka.transfer.settings.APIUserListResponse;
import fi.uta.fsd.metka.transfer.settings.NewAPIUserRequest;
import fi.uta.fsd.metkaAuthentication.Permission;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

//@PreAuthorize("hasPermission('canViewSettingsPage', 'PERMISSION')")
@PreAuthorize("hasPermission('"+ Permission.Values.CAN_VIEW_SETTINGS_PAGE +"', 'PERMISSION')")
public interface SettingsService {

    // Uses report repository to generate example report
    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_GENERATE_REPORTS +"', 'PERMISSION')")
    String generateReport();

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_VIEW_API_USERS +"', 'PERMISSION')")
    APIUserListResponse listAPIUsers();

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_EDIT_API_USERS +"', 'PERMISSION')")
    APIUserListResponse newAPIUser(NewAPIUserRequest request);

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_EDIT_API_USERS +"', 'PERMISSION')")
    ReturnResult removeAPIUser(String publicKey);

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_UPLOAD_CONFIGURATIONS +"', 'PERMISSION')")
    void uploadDataConfig(MultipartFile file) throws IOException;

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_UPLOAD_CONFIGURATIONS +"', 'PERMISSION')")
    void uploadGuiConfig(MultipartFile file) throws IOException;

    @PreAuthorize("hasPermission('"+ Permission.Values.CAN_UPLOAD_JSON +"', 'PERMISSION')")
    void uploadJson(MultipartFile file) throws IOException;
}

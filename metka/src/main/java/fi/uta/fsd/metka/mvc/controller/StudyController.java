package fi.uta.fsd.metka.mvc.controller;

import fi.uta.fsd.metka.data.enums.ChoicelistType;
import fi.uta.fsd.metka.data.enums.RevisionState;
import fi.uta.fsd.metka.model.configuration.Choicelist;
import fi.uta.fsd.metka.model.configuration.Configuration;
import fi.uta.fsd.metka.mvc.domain.ConfigurationService;
import fi.uta.fsd.metka.mvc.domain.GeneralService;
import fi.uta.fsd.metka.mvc.domain.StudyService;
import fi.uta.fsd.metka.mvc.domain.simple.ErrorMessage;
import fi.uta.fsd.metka.mvc.domain.simple.RevisionViewDataContainer;
import fi.uta.fsd.metka.mvc.domain.simple.TransferObject;
import fi.uta.fsd.metka.mvc.domain.simple.study.StudySearchData;
import fi.uta.fsd.metka.mvc.domain.simple.study.StudySearchResultSO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

/**
 * Handles all requests for study operations such as view and save.
 * All requests contain base address /study
 */
@Controller
@RequestMapping("/study")
public class StudyController {
    private static final String REDIRECT_SEARCH = "redirect:/study/search";
    private static final String REDIRECT_VIEW = "redirect:/study/view/";
    private static final String VIEW = "study/view";
    private static final String SEARCH = "study/search";
    private static final String MODIFY = "study/modify";

    @Autowired
    private StudyService studyService;
    @Autowired
    private GeneralService generalService;
    @Autowired
    private ConfigurationService configService;
    /*
    * View single study
    * Use search functions to find relevant revision for the requested study. Then redirect to viewing that specific revision.
    * If no revision is found then return to search page with an error message.
    */
    @RequestMapping(value = "view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        Integer revision = studyService.findSingleRevisionNo(id);
        if(revision != null) {
            return REDIRECT_VIEW+id+"/"+revision;
        } else {
            redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.noViewableRevision("study", id));
            return REDIRECT_SEARCH;
        }
    }

    /*
    * View single study
    * Use search functions to find the requested study revision.
    * Service will form a viewable object that can be added to the model.
    * If the returned single object is in DRAFT state and the current user is the handler
    * then show modify page, otherwise show view page.
    */
    @RequestMapping(value = "view/{id}/{revision}", method = RequestMethod.GET)
    public String viewRevision(Model model,
                               @PathVariable Integer id, @PathVariable Integer revision,
                               RedirectAttributes redirectAttributes) {
        TransferObject single = null;
        Configuration config = null;
        if(model.asMap().get("single") == null || model.asMap().get("configuration") == null) {
            RevisionViewDataContainer revData = studyService.findSingleRevision(id, revision);
            model.asMap().put("single", revData.getTransferObject());
            model.asMap().put("configuration", revData.getConfiguration());
            single = revData.getTransferObject();
            config = revData.getConfiguration();
        } else {
            single = (TransferObject)model.asMap().get("single");
            config = (Configuration)model.asMap().get("configuration");
        }

        if(single == null) {
            redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.noSuchRevision("study", id, revision));
            return REDIRECT_SEARCH;
        }

        try {
            for(Choicelist list : config.getChoicelists().values()) {
                if(list.getType().equals(ChoicelistType.REFERENCE)) {
                    generalService.fillOptions(list, config.getReferences().get(list.getReference()));
                }
            }
        } catch(IOException ex) {
            // TODO: Notify user that some choicelist could not be filled
            ex.printStackTrace();
        }

        model.asMap().put("page", "study");
        if(single.getState() == RevisionState.DRAFT) {
            // TODO: this should check if the user is the handler for this revision.
            return MODIFY;
        } else {
            return VIEW;
        }
    }

    /*
    * Search for study using query
    * Use search functions to get a list of studies matching the user defined query.
    * If there is only one result then redirect to view.
    * Otherwise show search page with the result in the model.
    */
    @RequestMapping(value="search", method = {RequestMethod.GET, RequestMethod.POST})
    public String search(Model model, @ModelAttribute("searchData")StudySearchData searchData) {
        if(searchData.getQuery() != null) {
            List<StudySearchResultSO> results = studyService.searchForStudies(searchData.getQuery());
            if(results.size() == 1) {
                return REDIRECT_VIEW+results.get(0).getId()+"/"+results.get(0).getRevision();
            }
            searchData.setResults(results);
            searchData.setQuery(searchData.getQuery());
        }

        model.asMap().put("searchData", searchData);

        if(searchData.getQuery() != null && searchData.getResults().size() == 0) {
            model.asMap().put("errorContainer", ErrorMessage.noResults("series"));
        }

        model.asMap().put("page", "study");
        return SEARCH;
    }

    /*
    * Add new study
    * Request a new study from the service then display MODIFY page for that study.
    * Only a DRAFT revision can be edited and only the newest revision can be a draft revision
    * so you can always modify by using only the id for the series.
    */
    @RequestMapping(value="add/{acquisition_number}", method = {RequestMethod.GET})
    public String add(@PathVariable Integer acquisition_number, RedirectAttributes redirectAttributes) {
        RevisionViewDataContainer revData = studyService.newStudy(acquisition_number);
        if(revData == null || revData.getTransferObject() == null || revData.getConfiguration() == null) {
            // TODO: Show error if no new series could be created
            return REDIRECT_SEARCH;
        } else {
            redirectAttributes.addFlashAttribute("single", revData.getTransferObject());
            redirectAttributes.addFlashAttribute("configuration", revData.getConfiguration());
            return REDIRECT_VIEW+revData.getTransferObject().getId()+"/"+revData.getTransferObject().getRevision();
        }
    }

    /*
    * Edit study
    * Requests an editable revision for the study. Everything required to get an editable
    * revision for the user is done further down the line (e.g. checking if new revision is
    * actually required or is there already an open DRAFT revision).
    */
    @RequestMapping(value = "edit/{id}", method = {RequestMethod.GET})
    public String edit(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        RevisionViewDataContainer revData = studyService.editStudy(id);
        if(revData == null || revData.getTransferObject() == null || revData.getConfiguration() == null) {
            // TODO: Notify user that no editable revision could be found or created
            return REDIRECT_VIEW+id;
        } else {
            redirectAttributes.addFlashAttribute("single", revData.getTransferObject());
            redirectAttributes.addFlashAttribute("configuration", revData.getConfiguration());
            return REDIRECT_VIEW+revData.getTransferObject().getId()+"/"+revData.getTransferObject().getRevision();
        }
    }

    /*
    * Save study
    * Tell service to save given study. It will be validated and checked for changes
    * further along the line so on this point the assumption can be made that changes exist.
    * Return to the modify page after including the success status of the operation.
    */
    @RequestMapping(value="save", method = {RequestMethod.POST})
    public String save(@ModelAttribute("single")TransferObject single, RedirectAttributes redirectAttributes) {
        boolean success = studyService.saveStudy(single);

        if(success) {
            redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.saveSuccess());
        } else {
            redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.saveFail());
        }

        return REDIRECT_VIEW+single.getId()+"/"+single.getRevision();
    }

    /*
    * Approve study
    * First makes sure that study is saved and if successful then requests study approval.
    * Since only DRAFTs can be approved and only the latest revision can be a DRAFT
    * only the study id is needed for the approval process. All required validation is done
    * later in the approval process.
    */
    @RequestMapping(value="approve", method = {RequestMethod.POST})
    public String approve(@ModelAttribute("single")TransferObject single, RedirectAttributes redirectAttributes) {
        boolean success = studyService.saveStudy(single);

        if(!success) {
            redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.approveFailSave());
        } else {
            success = studyService.approveStudy(single);

            if(!success) {
                redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.approveFailValidate());
            } else {
                redirectAttributes.addFlashAttribute("errorContainer", ErrorMessage.approveSuccess());
            }
        }

        return REDIRECT_VIEW+single.getId()+"/"+single.getRevision();
    }
}

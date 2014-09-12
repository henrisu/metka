package fi.uta.fsd.metkaSearch.commands.searcher.series;

import fi.uta.fsd.metka.enums.ConfigurationType;
import fi.uta.fsd.metka.enums.Language;
import fi.uta.fsd.metkaSearch.LuceneConfig;
import fi.uta.fsd.metkaSearch.commands.searcher.RevisionSearchCommandBase;
import fi.uta.fsd.metkaSearch.directory.DirectoryManager;
import fi.uta.fsd.metkaSearch.enums.IndexerConfigurationType;
import fi.uta.fsd.metkaSearch.results.ResultHandler;
import fi.uta.fsd.metkaSearch.results.ResultList;
import fi.uta.fsd.metkaSearch.results.RevisionResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.queryparser.flexible.core.QueryNodeException;
import org.apache.lucene.queryparser.flexible.standard.StandardQueryParser;
import org.apache.lucene.queryparser.flexible.standard.config.NumericConfig;
import org.apache.lucene.search.Query;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides information and query necessary for performing basic series test from Series search page.
 */
@Deprecated
public final class SeriesBasicSearchCommand extends RevisionSearchCommandBase<RevisionResult> {
    public static SeriesBasicSearchCommand build(boolean allowApproved, boolean allowDraft, boolean allowRemoved,
                                                 Long revisionableId, String abbreviation, String name) throws UnsupportedOperationException, QueryNodeException {
        //checkPath(path, ConfigurationType.SERIES);
        DirectoryManager.DirectoryPath path = DirectoryManager.formPath(false, IndexerConfigurationType.REVISION, Language.DEFAULT, ConfigurationType.SERIES.toValue());
        return new SeriesBasicSearchCommand(path, allowApproved, allowDraft, allowRemoved, revisionableId, abbreviation, name);
    }

    private final Query query;
    private SeriesBasicSearchCommand(DirectoryManager.DirectoryPath path,
                                     boolean allowApproved, boolean allowDraft, boolean allowRemoved,
                                     Long revisionableId, String abbreviation, String name) throws QueryNodeException {
        super(path, ResultList.ResultType.REVISION);
        List<String> qrys = new ArrayList<>();
        Map<String, NumericConfig> nums = new HashMap<>();

        qrys.add(((!allowApproved)?"+":"")+"state.approved:"+allowApproved);
        qrys.add(((!allowDraft)?"+":"")+"state.draft:"+allowDraft);
        qrys.add(((!allowRemoved)?"+":"")+"state.removed:"+allowRemoved);

        if(revisionableId != null) qrys.add("+key.id:"+revisionableId);
        nums.put("key.id", new NumericConfig(LuceneConfig.PRECISION_STEP, new DecimalFormat(), FieldType.NumericType.LONG));

        if(StringUtils.isNotBlank(abbreviation)) {
            qrys.add("+seriesabbr:"+abbreviation);
            addWhitespaceAnalyzer("seriesabbr");
        }
        if(StringUtils.isNotBlank(name)) {
            qrys.add("+seriesname:"+name);
            addTextAnalyzer("seriesname");
        }

        String qryStr = StringUtils.join(qrys, " ");

        StandardQueryParser parser = new StandardQueryParser(getAnalyzer());
        parser.setNumericConfigMap(nums);
        query = parser.parse(qryStr, "general");
    }

    @Override
    public Query getQuery() {
        return query;
    }

    @Override
    public ResultHandler<RevisionResult> getResulHandler() {
        return new BasicRevisionSearchResultHandler();
    }
}

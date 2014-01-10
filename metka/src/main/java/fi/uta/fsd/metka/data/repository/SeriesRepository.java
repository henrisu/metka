package fi.uta.fsd.metka.data.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import fi.uta.fsd.metka.model.data.RevisionData;
import fi.uta.fsd.metka.mvc.domain.simple.series.SeriesSingleSO;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: lasseku
 * Date: 1/3/14
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional(readOnly = true)
public interface SeriesRepository {
    @Transactional(readOnly = false) public RevisionData getNew() throws IOException;
    @Transactional(readOnly = false) public boolean saveSeries(SeriesSingleSO so) throws IOException;
}

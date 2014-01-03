package fi.uta.fsd.metka.data.repository;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import fi.uta.fsd.metka.data.entity.ConfigurationEntity;
import fi.uta.fsd.metka.mvc.domain.model.configuration.Configuration;
import fi.uta.fsd.metka.mvc.domain.model.configuration.ConfigurationKey;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: lasseku
 * Date: 1/2/14
 * Time: 1:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional(readOnly = true)
public interface ConfigurationRepository {
    @Transactional(readOnly = false)
    public void insert(Configuration configuration)
            throws JsonProcessingException;
    public Configuration findConfiguration(ConfigurationKey key)
            throws IncorrectResultSizeDataAccessException, JsonParseException, JsonMappingException, IOException;
}

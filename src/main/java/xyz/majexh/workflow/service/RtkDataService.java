package xyz.majexh.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import xyz.majexh.workflow.domain.RtkDataSchema;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author majexh
 */
@Service
public class RtkDataService {

    private MongoTemplate template;

    @Autowired
    public void setTemplate(MongoTemplate template) {
        this.template = template;
    }

    public Set<String> getAllCollectionsName() {
        return template.getCollectionNames();
    }

    public List<RtkDataSchema> getRtkDataSchemaByTimeArrange(Double startTime, Double endTime, String collectionName, String type) {
        Criteria criteria = new Criteria("time");
        criteria.gte(startTime);
        criteria.lte(endTime);
        Query query = new Query();
        query.addCriteria(criteria);
        query.fields().include("time");
        query.fields().include(type);
        return template.find(query, RtkDataSchema.class, collectionName);
    }

    public Map<String, Double> getTimeRangeByCollectionName(String collectionName) {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "time"));

        List<RtkDataSchema> queryValues = template.find(query, RtkDataSchema.class, collectionName);

        return new HashMap<>(){{
            put("max", queryValues.get(queryValues.size() - 1).getTime());
            put("min", queryValues.get(0).getTime());
        }};
    }
}

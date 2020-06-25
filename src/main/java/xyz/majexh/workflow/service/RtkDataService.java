package xyz.majexh.workflow.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import xyz.majexh.workflow.domain.RtkDataSchema;
import xyz.majexh.workflow.exceptions.BaseException;
import xyz.majexh.workflow.exceptions.ExceptionEnum;

import java.util.*;

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

    public List<RtkDataSchema> getRtkDataSchemaByTimeArrange(Date startTime, Date endTime, String collectionName, String type) {
        Query query = new Query();
        query.addCriteria(Criteria.where("time").gte(startTime).lt(endTime));
        query.fields().include("time");
        query.fields().include(type);
        return template.find(query, RtkDataSchema.class, collectionName);
    }

    private RtkDataSchema getDataWithSort(String collectionName, Sort.Direction direction, String ...properties) {
        Query minQuery = new Query();
        minQuery.with(Sort.by(direction, properties));
        for (String property : properties) {
            minQuery.fields().include(property);
        }
        minQuery.limit(1);
        RtkDataSchema res = template.findOne(minQuery, RtkDataSchema.class, collectionName);
        if (res == null) {
            throw new BaseException(ExceptionEnum.CANNOT_FIND_TIME_RANGE);
        }
        return res;
    }

    public Map<String, Date> getTimeRangeByCollectionName(String collectionName) {
        RtkDataSchema min = getDataWithSort(collectionName, Sort.Direction.ASC, "time");
        RtkDataSchema max = getDataWithSort(collectionName, Sort.Direction.DESC, "time");
        return new HashMap<>(2){{
            put("max", max.getTime());
            put("min", min.getTime());
        }};
    }
}

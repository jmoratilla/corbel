package com.bq.corbel.resources.rem.dao.builder;

import com.bq.corbel.lib.queries.mongo.builder.CriteriaBuilder;
import com.bq.corbel.lib.queries.request.Pagination;
import com.bq.corbel.lib.queries.request.ResourceQuery;
import com.bq.corbel.resources.rem.dao.JsonRelation;
import com.bq.corbel.resources.rem.model.ResourceUri;
import com.bq.corbel.resources.rem.resmi.exception.InvalidApiParamException;

import java.util.*;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;

/**
 * @author Rubén Carrasco
 *
 */
public class MongoAggregationBuilder {

    public static final String REFERENCE = "first";
    public static final String DOCUMENT = "document";
    private final List<AggregationOperation> operations;

    public MongoAggregationBuilder() {
        operations = new ArrayList<>();
    }

    public MongoAggregationBuilder sort(String direction, String field) {
        operations.add(Aggregation.sort(Direction.fromString(direction), field));
        return this;
    }

    public MongoAggregationBuilder pagination(Pagination pagination) {
        operations.add(Aggregation.skip(pagination.getPage() * pagination.getPageSize()));
        operations.add(Aggregation.limit(pagination.getPageSize()));
        return this;
    }

    public MongoAggregationBuilder match(ResourceUri uri, Optional<List<ResourceQuery>> resourceQueries) {
        Criteria criteria = new Criteria();
        if (resourceQueries.isPresent()) {
            criteria = CriteriaBuilder.buildFromResourceQueries(resourceQueries.get());
        }
        if (uri.isRelation()) {
            criteria = criteria.and(JsonRelation._SRC_ID).is(uri.getTypeId());
        }
        operations.add(Aggregation.match(criteria));
        return this;
    }

    public MongoAggregationBuilder group(List<String> fields) {
        return group(fields, false);
    }

    public MongoAggregationBuilder group(List<String> fields, boolean first) {
        if (fields != null && !fields.isEmpty()) {
            GroupOperation group = Aggregation.group(fields.toArray(new String[fields.size()]));
            if (first) {
                group = group.first(Aggregation.ROOT).as(REFERENCE);
            }
            operations.add(group);
        }
        return this;
    }

    public MongoAggregationBuilder projection(String field, String expression) {
        Fields fields = Fields.from(Fields.field(DOCUMENT, Aggregation.ROOT));
        operations.add(Aggregation.project(fields).andExpression(expression).as(field));
        return this;
    }

    public Aggregation build() throws InvalidApiParamException {
        if (operations.isEmpty()) {
            throw new InvalidApiParamException("Cannot build aggregation without operations");
        }
        return Aggregation.newAggregation(operations.toArray(new AggregationOperation[operations.size()]));
    }
}

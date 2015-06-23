package com.bq.oss.corbel.resources.rem;

import java.io.InputStream;
import java.util.Collections;
import java.util.Optional;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import com.bq.oss.corbel.resources.rem.request.RequestParameters;
import com.bq.oss.corbel.resources.rem.request.ResourceId;
import com.bq.oss.corbel.resources.rem.request.ResourceParameters;
import com.bq.oss.corbel.resources.rem.service.RemService;
import com.bq.oss.lib.ws.api.error.ErrorResponseFactory;

public class ImageDeleteRem extends BaseRem<InputStream> {

    private static final Logger LOG = LoggerFactory.getLogger(ImageDeleteRem.class);

    private final RemService remService;
    private final String cacheCollection;
    private final ImageRemUtil imageRemUtil;

    public ImageDeleteRem(RemService remService, String cacheCollection, ImageRemUtil imageRemUtil) {
        this.remService = remService;
        this.cacheCollection = cacheCollection;
        this.imageRemUtil = imageRemUtil;
    }

    @Override
    public Response resource(String collection, ResourceId resourceId, RequestParameters<ResourceParameters> requestParameters,
            Optional<InputStream> entity) {

        @SuppressWarnings("unchecked")
        Rem<InputStream> restorDeleteRem = (Rem<InputStream>) remService.getRem(collection, requestParameters.getAcceptedMediaTypes(),
                HttpMethod.DELETE, Collections.singletonList(this));

        if (restorDeleteRem == null) {
            LOG.warn("RESTOR not found. May  be is needed to install it?");
            return ErrorResponseFactory.getInstance().notFound();
        }

        restorDeleteRem.collection(cacheCollection, imageRemUtil.getCollectionParametersWithPrefix(resourceId.getId(), requestParameters),
                null, Optional.empty());
        restorDeleteRem.resource(collection, resourceId, requestParameters, Optional.empty());

        return Response.noContent().build();
    }

    @Override
    public Class<InputStream> getType() {
        return InputStream.class;
    }
}

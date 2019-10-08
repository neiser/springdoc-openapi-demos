package org.springdoc.demo.app2;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.api.OpenApiCustomiser;
import org.springdoc.api.OpenApiResource;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.AbstractResponseBuilder;
import org.springdoc.core.GeneralInfoBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBodyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springdoc.core.Constants.API_DOCS_URL;
import static org.springdoc.core.Constants.APPLICATION_OPENAPI_YAML;
import static org.springdoc.core.Constants.DEFAULT_API_DOCS_URL_YAML;
import static org.springdoc.core.Constants.SPRINGDOC_ENABLED;

@RestController
@ConditionalOnProperty(name = SPRINGDOC_ENABLED, matchIfMissing = true)
public class MultipleOpenApiResource {

    public static final String DEFAULT_GROUP_NAME = "default";
    private static final String GROUP_REQUEST_PARAM_NAME = "group";

    private final Map<String, OpenApiResource> groupedOpenApiResources;

    @Autowired
    public MultipleOpenApiResource(Optional<List<GroupedOpenApi>> groupedOpenApis,
                                   RequestMappingInfoHandlerMapping requestMappingHandlerMapping, OpenAPIBuilder defaultOpenAPIBuilder,
                                   AbstractRequestBuilder defaultAbstractRequestBuilder, AbstractResponseBuilder defaultAbstractResponseBuilder,
                                   OperationBuilder defaultOperationBuilder, GeneralInfoBuilder defaultGeneralInfoBuilder,
                                   RequestBodyBuilder defaultRequestBodyBuilder,
                                   Optional<List<OpenApiCustomiser>> defaultOpenApiCustomisers) {

        this.groupedOpenApiResources = groupedOpenApis.map(items -> items.stream()
                .collect(Collectors.toMap(GroupedOpenApi::getGroup, item ->
                        new OpenApiResource(
                                nullOr(item.getOpenAPIBuilder(), defaultOpenAPIBuilder),
                                nullOr(item.getRequestBuilder(), defaultAbstractRequestBuilder),
                                nullOr(item.getResponseBuilder(), defaultAbstractResponseBuilder),
                                nullOr(item.getOperationBuilder(), defaultOperationBuilder),
                                nullOr(item.getInfoBuilder(), defaultGeneralInfoBuilder),
                                nullOr(item.getRequestBodyBuilder(), defaultRequestBodyBuilder),
                                requestMappingHandlerMapping,
                                Optional.ofNullable(nullOr(item.getOpenApiCustomisers(), defaultOpenApiCustomisers.orElse(null)))
                        )))
        ).orElse(Collections.emptyMap());
    }

    private static <T> T nullOr(T item, T defaultItem) {
        return item == null ? defaultItem : item;
    }

    @Operation(hidden = true)
    @GetMapping(value = API_DOCS_URL, produces = MediaType.APPLICATION_JSON_VALUE)
    public String openapiJson(HttpServletRequest request, @Value(API_DOCS_URL) String apiDocsUrl,
                              @RequestParam(value = GROUP_REQUEST_PARAM_NAME, required = false, defaultValue = DEFAULT_GROUP_NAME) String group)
            throws JsonProcessingException {
        return getOpenApiResourceOrThrow(group).openapiJson(request, apiDocsUrl);
    }

    @Operation(hidden = true)
    @GetMapping(value = DEFAULT_API_DOCS_URL_YAML, produces = APPLICATION_OPENAPI_YAML)
    public String openapiYaml(HttpServletRequest request, @Value(DEFAULT_API_DOCS_URL_YAML) String apiDocsUrl,
                              @RequestParam(value = GROUP_REQUEST_PARAM_NAME, required = false, defaultValue = DEFAULT_GROUP_NAME) String group)
            throws JsonProcessingException {
        return getOpenApiResourceOrThrow(group).openapiJson(request, apiDocsUrl);
    }

    private OpenApiResource getOpenApiResourceOrThrow(@RequestParam(value = "group", required = false, defaultValue = "default") String group) {
        OpenApiResource openApiResource = groupedOpenApiResources.get(group);
        if (openApiResource == null) {
            throw new IllegalStateException("No OpenAPI resource found for group " + group);
        }
        return openApiResource;
    }
}

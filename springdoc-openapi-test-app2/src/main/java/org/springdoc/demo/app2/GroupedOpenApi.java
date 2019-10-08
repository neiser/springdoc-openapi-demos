package org.springdoc.demo.app2;

import org.springdoc.api.OpenApiCustomiser;
import org.springdoc.core.AbstractRequestBuilder;
import org.springdoc.core.AbstractResponseBuilder;
import org.springdoc.core.GeneralInfoBuilder;
import org.springdoc.core.OpenAPIBuilder;
import org.springdoc.core.OperationBuilder;
import org.springdoc.core.RequestBodyBuilder;

import java.util.List;
import java.util.Objects;

public class GroupedOpenApi {

    private final String group;

    private final OpenAPIBuilder openAPIBuilder;
    private final AbstractRequestBuilder requestBuilder;
    private final AbstractResponseBuilder responseBuilder;
    private final OperationBuilder operationBuilder;
    private final GeneralInfoBuilder infoBuilder;
    private final RequestBodyBuilder requestBodyBuilder;
    private final List<OpenApiCustomiser> openApiCustomisers;

    private GroupedOpenApi(Builder builder) {
        this.group = Objects.requireNonNull(builder.group, "group must not be null");
        this.openAPIBuilder = builder.openAPIBuilder;
        this.requestBuilder = builder.requestBuilder;
        this.responseBuilder = builder.responseBuilder;
        this.operationBuilder = builder.operationParser;
        this.infoBuilder = builder.infoBuilder;
        this.requestBodyBuilder = builder.requestBodyBuilder;
        this.openApiCustomisers = builder.openApiCustomisers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getGroup() {
        return group;
    }

    public OpenAPIBuilder getOpenAPIBuilder() {
        return openAPIBuilder;
    }

    public AbstractRequestBuilder getRequestBuilder() {
        return requestBuilder;
    }

    public AbstractResponseBuilder getResponseBuilder() {
        return responseBuilder;
    }

    public OperationBuilder getOperationBuilder() {
        return operationBuilder;
    }

    public GeneralInfoBuilder getInfoBuilder() {
        return infoBuilder;
    }

    public RequestBodyBuilder getRequestBodyBuilder() {
        return requestBodyBuilder;
    }

    public List<OpenApiCustomiser> getOpenApiCustomisers() {
        return openApiCustomisers;
    }

    public static class Builder {
        private String group;
        private OpenAPIBuilder openAPIBuilder;
        private AbstractRequestBuilder requestBuilder;
        private AbstractResponseBuilder responseBuilder;
        private OperationBuilder operationParser;
        private GeneralInfoBuilder infoBuilder;
        private RequestBodyBuilder requestBodyBuilder;
        private List<OpenApiCustomiser> openApiCustomisers;

        private Builder() {
            // use static factory method in parent class
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setOpenAPIBuilder(OpenAPIBuilder openAPIBuilder) {
            this.openAPIBuilder = openAPIBuilder;
            return this;
        }

        public Builder setRequestBuilder(AbstractRequestBuilder requestBuilder) {
            this.requestBuilder = requestBuilder;
            return this;
        }

        public Builder setResponseBuilder(AbstractResponseBuilder responseBuilder) {
            this.responseBuilder = responseBuilder;
            return this;
        }

        public Builder setOperationParser(OperationBuilder operationParser) {
            this.operationParser = operationParser;
            return this;
        }

        public Builder setInfoBuilder(GeneralInfoBuilder infoBuilder) {
            this.infoBuilder = infoBuilder;
            return this;
        }

        public Builder setRequestBodyBuilder(RequestBodyBuilder requestBodyBuilder) {
            this.requestBodyBuilder = requestBodyBuilder;
            return this;
        }

        public Builder setOpenApiCustomisers(List<OpenApiCustomiser> openApiCustomisers) {
            this.openApiCustomisers = openApiCustomisers;
            return this;
        }

        public GroupedOpenApi build() {
            return new GroupedOpenApi(this);
        }
    }
}

package org.springdoc.demo.app2;

import org.springdoc.api.OpenApiCustomiser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GroupedOpenApi {

    private final String group;
    private final List<OpenApiCustomiser> openApiCustomisers;

    private GroupedOpenApi(Builder builder) {
        this.group = Objects.requireNonNull(builder.group, "group");
        this.openApiCustomisers = Objects.requireNonNull(builder.openApiCustomisers);
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getGroup() {
        return group;
    }

    public List<OpenApiCustomiser> getOpenApiCustomisers() {
        return openApiCustomisers;
    }

    public static class Builder {
        private String group;
        private List<OpenApiCustomiser> openApiCustomisers = new ArrayList<>();

        private Builder() {
            // use static factory method in parent class
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder addOpenApiCustomiser(OpenApiCustomiser openApiCustomiser) {
            this.openApiCustomisers.add(openApiCustomiser);
            return this;
        }

        public GroupedOpenApi build() {
            return new GroupedOpenApi(this);
        }
    }
}

package org.springdoc.demo.app2;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Paths;
import org.springdoc.api.OpenApiResource;
import org.springdoc.core.OpenAPIBuilder;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MultipleOpenApiSupportConfiguration {
    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> {
            for (String beanName : beanFactory.getBeanNamesForType(OpenApiResource.class)) {
                ((BeanDefinitionRegistry) beanFactory).removeBeanDefinition(beanName);
            }
            for (String beanName : beanFactory.getBeanNamesForType(OpenAPIBuilder.class)) {
                beanFactory.getBeanDefinition(beanName).setScope("prototype");
            }
            for (String beanName : beanFactory.getBeanNamesForType(OpenAPI.class)) {
                beanFactory.getBeanDefinition(beanName).setScope("prototype");
            }
        };
    }

    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi.builder().setGroup("user")
                .addOpenApiCustomiser(openApi -> includePathsWithPrefix(openApi, "/user"))
                .build();
    }

    @Bean
    public GroupedOpenApi storeOpenApi() {
        return GroupedOpenApi.builder().setGroup("store")
                .addOpenApiCustomiser(openApi -> includePathsWithPrefix(openApi, "/store"))
                .build();
    }

    private void includePathsWithPrefix(OpenAPI openApi, String pathPrefix) {
        Paths paths = new Paths();
        openApi.getPaths().entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(pathPrefix))
                .forEach(entry -> paths.addPathItem(entry.getKey(), entry.getValue()));
        openApi.setPaths(paths);
    }
}

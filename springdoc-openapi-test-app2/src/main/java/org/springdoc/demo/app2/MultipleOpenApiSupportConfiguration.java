package org.springdoc.demo.app2;

import org.springdoc.api.OpenApiResource;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springdoc.demo.app2.MultipleOpenApiResource.DEFAULT_GROUP_NAME;

@Configuration
public class MultipleOpenApiSupportConfiguration {
    @Bean
    public BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return beanFactory -> {
            for (String beanName : beanFactory.getBeanNamesForType(OpenApiResource.class)) {
                ((BeanDefinitionRegistry) beanFactory).removeBeanDefinition(beanName);
            }
        };
    }

    @Bean
    public GroupedOpenApi defaultGroupedOpenApi() {
        return GroupedOpenApi.builder().setGroup(DEFAULT_GROUP_NAME).build();
    }
}

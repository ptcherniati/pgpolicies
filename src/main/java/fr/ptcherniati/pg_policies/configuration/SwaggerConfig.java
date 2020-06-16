package fr.ptcherniati.pg_policies.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableSwagger2
@Slf4j
public class SwaggerConfig {
    public static final String[] CLASSES = new String[]{"Log.*", "Authorities", "Users", "Aliments", "Categories", "Roles", "Policies", "Verifierqiosjio"};
    public static final String CLASSES_PATH = "/api/v1/%s.*";

    @Bean
    public Docket api() {
        String definition = Arrays.stream(CLASSES)
                .map(s -> String.format(CLASSES_PATH, s))
                .collect(Collectors.joining("|"));
        log.warn(definition);
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("fr.ptcherniati.pg_policies.web"))
                .paths(PathSelectors.regex(definition))
                .build();
    }

    /*@Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));

    }*/
}
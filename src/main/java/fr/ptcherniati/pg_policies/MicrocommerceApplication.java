package fr.ptcherniati.pg_policies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.File;

@EnableSwagger2
@EnableWebMvc
@SpringBootApplication(scanBasePackages = {"fr.ptcherniati.pg_policies"})
@PropertySource("classpath:application.properties")
public class MicrocommerceApplication implements WebMvcConfigurer {

    public static void main(String[] args) {

        SpringApplication.run(MicrocommerceApplication.class, args);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry
                .addResourceHandler("/static/**")
                .addResourceLocations("file:" + new File(".").getAbsolutePath() + "/src/main/resources/web/", "classpath:/web/")
                .setCachePeriod(0)
                .resourceChain(false)
                .addResolver(new PathResourceResolver());
        registry
                .addResourceHandler("/static/static/index.html")
                .addResourceLocations("file:" + new File(".").getAbsolutePath() + "/src/main/resources/web/", "classpath:/web/index.html")
                .setCachePeriod(0)
                .resourceChain(false)
                .addResolver(new PathResourceResolver());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ore-si-ng")
                .description("Api Rest pour le stockage et la restitution de fichier CSV")
                .version("1.0")
                .termsOfServiceUrl("https://inra.fr")
                .license("LICENSE")
                .licenseUrl("https://www.gnu.org/licenses/lgpl.html")
                .build();
    }
}

package org.aravena.test.springboot.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SpringFoxConfig {

    //http://localhost:8083/swagger-ui/
    //http://localhost:8083/v2/api-docs
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.aravena.test.springboot.app.controller"))
                //.paths(PathSelectors.any())
                .paths(PathSelectors.ant("/api/cuentas/*"))
                .build();
    }
}

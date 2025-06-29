package br.com.guifroes1984.api.pagamentos.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.GrantType;
import springfox.documentation.service.OAuth;
import springfox.documentation.service.ResourceOwnerPasswordCredentialsGrant;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.guifroes1984.api.pagamentos.resources"))
				.paths(PathSelectors.any()).build().apiInfo(apiInfo()).securitySchemes(Arrays.asList(securityScheme()))
				.securityContexts(Arrays.asList(securityContext()));

	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("API - Pagamentos e Gastos Pessoais.")
				.description("API para pagamentos e gastos pessoais.").version("1.0.0")
				.contact(new Contact("Guilherme Henrique Froes", " ", " ")).build();
	}

	private SecurityScheme securityScheme() {
		GrantType grantType = new ResourceOwnerPasswordCredentialsGrant("http://localhost:8080/oauth/token");

		return new OAuth("oauth-security", scopes(), Arrays.asList(grantType));
	}

	private List<AuthorizationScope> scopes() {
		return Arrays.asList(new AuthorizationScope("read", "Acesso de leitura"),
				new AuthorizationScope("write", "Acesso de escrita"));
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
				.securityReferences(Arrays
						.asList(new SecurityReference("oauth-security", scopes().toArray(new AuthorizationScope[0]))))
				.forPaths(PathSelectors.any()).build();
	}

}

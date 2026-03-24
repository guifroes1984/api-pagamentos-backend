package com.guifroes1984.pagamentos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI().info(new Info().title("API de Pagamentos").version("2.0.0")
				.description("API para gerenciamento de pagamentos e categorias")
				.contact(new Contact().name("Guifroes1984").email("guilhermefroes26@gmail.com")
						.url("https://github.com/guifroes1984"))
				.license(new License().name("Apache 2.0").url("http://springdoc.org")));
	}

}

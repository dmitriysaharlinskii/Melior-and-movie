package com.example.movieapp.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
@Configuration
public class RestClientConfig {

	@Value("${tmdb.api.base-url}")
	private String baseUrl;

	@Value("${tmdb.api.key}")
	private String apiKey;

	@Bean
	public RestClient tmdbRestClient(RestClient.Builder builder) {
		return builder
				.baseUrl(baseUrl)
				.defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
				.defaultHeader("Authorization", "Bearer " + apiKey) // или через параметр запроса
				.requestInterceptor((request, body, execution) -> {
					// Логирование запроса
					System.out.println("➡️ Запрос к TMDB: " + request.getMethod() + " " + request.getURI());
					long startTime = System.currentTimeMillis();

					try {
						var response = execution.execute(request, body);
						long duration = System.currentTimeMillis() - startTime;
						System.out.println("⬅️ Ответ от TMDB за " + duration + "ms (статус: " + response.getStatusCode() + ")");
						return response;
					} catch (Exception e) {
						System.out.println("❌ Ошибка при запросе к TMDB: " + e.getMessage());
						throw e;
					}
				})
				.build();
	}
}
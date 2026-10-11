package farit.dev.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import farit.dev.dto.TaxRateResponse;
import farit.dev.service.TaxRateService;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class TaxRateServiceRestImpl implements TaxRateService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final URI TAX_RATE_URI = URI.create("http://localhost:80/app1/api/tax-rate");

    public TaxRateServiceRestImpl() {
        objectMapper = new ObjectMapper();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    @Override
    @SneakyThrows
    public TaxRateResponse getTaxRate() {
        HttpRequest request = HttpRequest.newBuilder(TAX_RATE_URI)
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );
        if (response.statusCode() != 200) {
            throw new RuntimeException("Админка вернула код " + response.statusCode());
        }
        return objectMapper.readValue(response.body(), TaxRateResponse.class);
    }
}

package farit.dev.service.impl;

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
    private static final URI TAX_RATE_URI = URI.create("http://localhost:80/app1/api/tax-rate");

    public TaxRateServiceRestImpl() {
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
        return new TaxRateResponse(parsePercent(response.body()));
    }

    private double parsePercent(String json) {
        String clean = json.replace(" ", "").replace("\"", "");
        String key = "percent:";
        int start = clean.indexOf(key);
        if (start < 0) {
            throw new RuntimeException("В json нет percent");
        }
        start += key.length();
        int end = start;
        while (end < clean.length()) {
            char c = clean.charAt(end);
            if (Character.isDigit(c) || c == '.' || c == '-') {
                end++;
            } else {
                break;
            }
        }
        return Double.parseDouble(clean.substring(start, end));
    }
}

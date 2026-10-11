package farit.dev.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import farit.dev.dto.SignInRequest;
import farit.dev.dto.SignInResponse;
import farit.dev.service.SignInService;
import lombok.SneakyThrows;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class SignInServiceRestImpl implements SignInService {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private static final URI SIGN_IN_URI = URI.create("http://localhost:80/app1/api/sign-in");

    public SignInServiceRestImpl() {
        objectMapper = new ObjectMapper();
        httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(3))
                .build();
    }

    @SneakyThrows
    public SignInResponse signIn(SignInRequest request) {
        try {
            HttpResponse<String> response = httpClient.send(
                    buildRequest(request),
                    HttpResponse.BodyHandlers.ofString()
            );

            return parseResponse(response);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    @SneakyThrows
    private HttpRequest buildRequest(SignInRequest request) {
        String json = objectMapper.writeValueAsString(request);
        return HttpRequest.newBuilder(SIGN_IN_URI)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private SignInResponse parseResponse(HttpResponse<String> rs) {
        try {
            SignInResponse resp = objectMapper.readValue(rs.body(), SignInResponse.class);
            if(rs.statusCode() != 200) {
                return error("Not 200 code!");
            }
            return resp;
        } catch (Exception e) {
            return error("Cannot parse response");
        }
    }

    private SignInResponse error(String error) {
        return new SignInResponse(false, null, error);
    }
}
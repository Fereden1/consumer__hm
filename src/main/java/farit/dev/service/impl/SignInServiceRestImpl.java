package farit.dev.service.impl;

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
    private static final URI SIGN_IN_URI = URI.create("http://localhost:80/app1/api/sign-in");

    public SignInServiceRestImpl() {
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

    private HttpRequest buildRequest(SignInRequest request) {
        String json = "{\"username\":\"" + request.username() + "\",\"password\":\"" + request.password() + "\"}";
        return HttpRequest.newBuilder(SIGN_IN_URI)
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    private SignInResponse parseResponse(HttpResponse<String> rs) {
        try {
            if (rs.statusCode() != 200) {
                return error("Not 200 code!");
            }
            String json = rs.body();
            boolean success = json.contains("\"success\":true") || json.contains("\"success\": true");
            String token = readJsonString(json, "token");
            String message = readJsonString(json, "message");
            return new SignInResponse(success, token, message);
        } catch (Exception e) {
            return error("Cannot parse response");
        }
    }

    private String readJsonString(String json, String key) {
        String pattern = "\"" + key + "\"";
        int i = json.indexOf(pattern);
        if (i < 0) {
            return null;
        }
        int colon = json.indexOf(':', i);
        int quote = json.indexOf('"', colon + 1);
        if (quote < 0) {
            return null;
        }
        int end = json.indexOf('"', quote + 1);
        if (end < 0) {
            return null;
        }
        String value = json.substring(quote + 1, end);
        if ("null".equals(value)) {
            return null;
        }
        return value;
    }

    private SignInResponse error(String error) {
        return new SignInResponse(false, null, error);
    }
}

package net.ramslayer.duinomccore.backend;

import com.google.gson.Gson;
import net.ramslayer.duinomccore.backend.schemas.DepositApproval;
import net.ramslayer.duinomccore.backend.schemas.DepositRequest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

public class BackendClient {
    private final String baseURL;
    private final Gson gson;
    private final HttpClient httpClient;

    public BackendClient(String baseURL) {
        this.baseURL = baseURL.endsWith("/") ? baseURL.substring(0, baseURL.length() - 1) : baseURL;
        this.gson = new Gson();
        this.httpClient = HttpClient.newHttpClient();
    }

    public CompletableFuture<DepositApproval> sendDeposit(DepositRequest request) {
        try {
            String json = gson.toJson(request);

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(baseURL + "/deposit"))
                    .header("Content-Type", "application/json; charset=UTF-8")
                    .header("Accept", "application/json")
                    .timeout(Duration.ofSeconds(5))
                    .version(HttpClient.Version.HTTP_1_1)
                    .POST(HttpRequest.BodyPublishers.ofString(json, StandardCharsets.UTF_8))
                    .build();

            return httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> gson.fromJson(response.body(), DepositApproval.class));
        } catch (Exception e) {
            CompletableFuture<DepositApproval> failed = new CompletableFuture<>();
            failed.completeExceptionally(e);
            return failed;
        }
    }
}

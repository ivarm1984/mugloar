package ee.ivar.mugloar.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

@Configuration
@Profile("mocks")
public class WiremockConfiguration {

    @Value("${wiremock.port:8081}")
    private int wireMockPort;
    @Autowired
    private ResourceLoader resourceLoader;

    private WireMockServer wireMockServer;

    @Bean
    public WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(
                WireMockConfiguration.wireMockConfig()
                        .port(wireMockPort)
        );
        this.wireMockServer = wireMockServer;
        wireMockServer.start();
        setupStubs(wireMockServer);
        return wireMockServer;
    }

    private void setupStubs(WireMockServer wireMockServer) {

        String gameStartResponse = readFile("mocks/gameStartResponse.json");
        String reputationResponse = readFile("mocks/investigateReputationResponse.json");
        String messagesResponse = readFile("mocks/messagesResponse.json");
        String shopResponse = readFile("mocks/shopResponse.json");
        String shopBuyResponse = readFile("mocks/shopBuyResponse.json");
        String solveBuyResponse = readFile("mocks/solveResponse.json");

        stubPost("/api/v2/game/start", gameStartResponse);
        stubPost(".*investigate/reputation.*", reputationResponse);
        stubGet(".*/messages.*", messagesResponse);
        stubGet(".*/shop.*", shopResponse);
        stubPost(".*/shop/buy.*", shopBuyResponse);
        stubPost(".*/solve.*", solveBuyResponse);
    }

    private void stubPost(String url, String response) {
        wireMockServer.stubFor(post(urlMatching(url))
                .willReturn(
                        aResponse()
                                .withBody(response)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
    }

    private void stubGet(String url, String response) {
        wireMockServer.stubFor(get(urlMatching(url))
                .willReturn(
                        aResponse()
                                .withBody(response)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));
    }

    private String readFile(String filePath) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + filePath);
            return new String(Files.readAllBytes(Paths.get(resource.getURI())));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read the file from resources: " + filePath, e);
        }
    }

}

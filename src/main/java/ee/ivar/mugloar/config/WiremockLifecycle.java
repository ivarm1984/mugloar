package ee.ivar.mugloar.config;

import com.github.tomakehurst.wiremock.WireMockServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.DisposableBean;

@RequiredArgsConstructor
public class WiremockLifecycle implements DisposableBean {

    private final WireMockServer wireMockServer;

    @Override
    public void destroy() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

}

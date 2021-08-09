package com.adacom.gridsdctester.config;

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import eu.grids.sdk.service.Impl.GRIDSClientManager;
import eu.grids.sdk.service.Impl.GRIDSIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Create by szamanis - Adacom S.A.
 */
@Configuration
public class SpringConfig {


    @Autowired
    private Environment env;

    @Bean
    public GRIDSIssuer gridsIssuer() throws URISyntaxException {

        GRIDSIssuer issuer = new GRIDSIssuer(
                new URI("https://vm.project-grids.eu:8180/auth/realms/grids/"),
                "717ca0c9-f7bb-45da-a1be-fa0c55ba106b",
                "8333eebe-90a3-4c66-805a-11407959e9c8",
                new URI("http://localhost:8080/callback")
        );

        return issuer;

    }

    @Bean
    public GRIDSClientManager gridsManager() throws URISyntaxException {

        GRIDSIssuer issuer = new GRIDSIssuer(new URI("https://vm.project-grids.eu:8180/auth/realms/grids/"));

        OIDCProviderMetadata metadata = issuer.getOPMetadata();

        GRIDSClientManager manager = new GRIDSClientManager(metadata.getRegistrationEndpointURI());

        return manager;

    }


}

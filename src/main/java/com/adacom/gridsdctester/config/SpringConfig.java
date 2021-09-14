package com.adacom.gridsdctester.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
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
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

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
                "c898f3a0-f659-487f-ba29-25114a8c8dee",
                "7bf69e58-9763-4032-b3ae-bbd284e0356c",
                new URI("http://localhost:8080/callback")
        );

        return issuer;

    }

    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();

        return keyPair;

    }

    @Bean
    public GRIDSClientManager gridsManager() throws URISyntaxException {

        GRIDSIssuer issuer = new GRIDSIssuer(new URI("https://vm.project-grids.eu:8180/auth/realms/grids/"));

        OIDCProviderMetadata metadata = issuer.getOPMetadata();

        GRIDSClientManager manager = new GRIDSClientManager(metadata.getRegistrationEndpointURI());

        return manager;

    }




}

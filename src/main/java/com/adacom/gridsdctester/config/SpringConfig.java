package com.adacom.gridsdctester.config;

import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import eu.grids.sdk.service.Impl.GRIDSClientManager;
import eu.grids.sdk.service.Impl.GRIDSIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Create by szamanis - Adacom S.A.
 */
@Configuration
public class SpringConfig {


    @Autowired
    private Environment env;

    @Bean("gridsIssuer")
    @DependsOn({"jwksKeys"})
    public GRIDSIssuer gridsIssuer(@Autowired() KeyPair keyPair) throws URISyntaxException {

        GRIDSIssuer issuer = new GRIDSIssuer(
                new URI(env.getProperty("DCC_URI")),
                env.getProperty("DC_CLIENT_ID"),
                env.getProperty("DC_CLIENT_SECRET"),
                new URI(env.getProperty("DC_URL") + "callback"),
                keyPair

        );

        return issuer;

    }

    @Bean("jwksKeys")
    public KeyPair keyPair() throws NoSuchAlgorithmException {

        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair keyPair = gen.generateKeyPair();

        return keyPair;

    }

    @Bean("gridsManager")
    @DependsOn({"gridsIssuer"})
    public GRIDSClientManager gridsManager() throws URISyntaxException {

        GRIDSIssuer issuer = new GRIDSIssuer(new URI(env.getProperty("DCC_URI")));

        OIDCProviderMetadata metadata = issuer.getOPMetadata();

        GRIDSClientManager manager = new GRIDSClientManager(metadata.getRegistrationEndpointURI());

        return manager;

    }

}

package com.adacom.gridsdctester.controllers;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.client.ClientInformation;
import com.nimbusds.openid.connect.sdk.rp.OIDCClientMetadata;
import eu.grids.sdk.service.Impl.GRIDSClientManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

/**
 * Create by szamanis - Adacom S.A.
 */
@Controller
@RequestMapping("client")
public class ClientManagementController {

    @Autowired
    private Environment env;

    @Autowired
    GRIDSClientManager gridsClientManager;


    @RequestMapping(value = "register")
    public String register(Model model) throws IOException, ParseException, URISyntaxException {

        OIDCClientMetadata clientMetadata = new OIDCClientMetadata();

        clientMetadata.setName(env.getProperty("DC_NAME"));
        clientMetadata.setGrantTypes(Collections.singleton(GrantType.AUTHORIZATION_CODE));
        clientMetadata.setRedirectionURI(new URI(env.getProperty("DC_URL") + "callback"));
        clientMetadata.setJWKSetURI(new URI(env.getProperty("DC_URL") + "jwks"));

        clientMetadata.setUserInfoJWEAlg(JWEAlgorithm.RSA_OAEP_256);
        clientMetadata.setUserInfoJWEEnc(EncryptionMethod.A128CBC_HS256);
        clientMetadata.setUserInfoJWSAlg(JWSAlgorithm.RS256);

        ClientInformation clientInformation = gridsClientManager.registerClient(clientMetadata, env.getProperty("DCC_MASTER_TOKEN"));

        System.out.println(clientInformation.toJSONObject().toJSONString());

        return clientInformation.toJSONObject().toJSONString();

    }
}

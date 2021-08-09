package com.adacom.gridsdctester.controllers;

import com.nimbusds.oauth2.sdk.GrantType;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.client.ClientInformation;
import com.nimbusds.oauth2.sdk.client.ClientMetadata;
import eu.grids.sdk.service.Impl.GRIDSClientManager;
import org.springframework.beans.factory.annotation.Autowired;
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
    GRIDSClientManager gridsClientManager;

    private String masterToken = "eyJhbGciOiJIUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICI3NDdiMzRmNy02YmZjLTRhODgtODgxYS0wYjlhNWQ3NjZmOTgifQ.eyJleHAiOjE2Mjk1NDAyNzQsImlhdCI6MTYyMDkwMDI3NCwianRpIjoiMDNlNThjZDYtYzhmMi00M2Q2LThiMTEtNWIzMzZiNzhjMTNlIiwiaXNzIjoiaHR0cHM6Ly92bS5wcm9qZWN0LWdyaWRzLmV1OjgxODAvYXV0aC9yZWFsbXMvZ3JpZHMiLCJhdWQiOiJodHRwczovL3ZtLnByb2plY3QtZ3JpZHMuZXU6ODE4MC9hdXRoL3JlYWxtcy9ncmlkcyIsInR5cCI6IkluaXRpYWxBY2Nlc3NUb2tlbiJ9.2_1E8938Ofa8nuN1oYo4Xq9j3s-2_Fm9oJ2x_pJlo20";


    @RequestMapping(value = "register")
    public String register(Model model) throws IOException, ParseException, URISyntaxException {


        ClientMetadata clientMetadata = new ClientMetadata();
        clientMetadata.setName("Adacom test");
        clientMetadata.setGrantTypes(Collections.singleton(GrantType.AUTHORIZATION_CODE));
        clientMetadata.setRedirectionURI(URI.create("http://localhost:8080/callback"));
        clientMetadata.setJWKSetURI(new URI("https://vm.project-grids.eu:8180/auth/realms/grids/protocol/openid-connect/certs"));


        ClientInformation clientInformation = gridsClientManager.registerClient(clientMetadata, masterToken);

        System.out.println(clientInformation.toJSONObject().toJSONString());

        return clientInformation.toJSONObject().toJSONString();

    }
}

package com.adacom.gridsdctester.controllers;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.oauth2.sdk.util.JSONArrayUtils;
import com.nimbusds.openid.connect.sdk.OIDCClaimsRequest;
import com.nimbusds.openid.connect.sdk.assurance.claims.VerifiedClaimsSetRequest;
import com.nimbusds.openid.connect.sdk.claims.DistributedClaims;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import eu.grids.sdk.service.Impl.GRIDSIssuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Set;

/**
 * Create by szamanis - Adacom S.A.
 */
@Controller
public class LoginController {

    @Autowired
    private GRIDSIssuer gridsIssuer;

    @RequestMapping(value = "")
    public String index(Model model) throws ParseException {

        //String evidenceStr = "[{\"type\":{\"value\":\"company_register\"},\"registry\":{\"organisation\":{\"essential\":false,\"purpose\":\"string\"},\"country\":{\"essential\":true,\"purpose\":\"string\",\"value\":\"ES\"}},\"time\":{\"max_age\":31000000,\"essential\":true,\"purpose\":\"string\"},\"data\":{\"essential\":true,\"purpose\":\"string\"},\"extractURL\":{\"essential\":true,\"purpose\":\"string\"},\"document\":{\"SKU\":{\"essential\":false,\"purpose\":\"string\"},\"option\":{\"essential\":false,\"purpose\":\"string\"}}}]";
        String evidenceStr = "[{\"type\":{\"value\":\"company_register\"},\"registry\":{\"organisation\":{\"essential\":false,\"purpose\":\"string\"},\"country\":{\"essential\":true,\"purpose\":\"string\",\"value\":\"AT\"}}}]";


        JSONArray evidence = JSONArrayUtils.parse(evidenceStr);


        JSONObject verification = new JSONObject();
        verification.put("trust_framework", "grids_kyb");
        verification.put("userinfo_endpoint", "https://dp.kompany.com:8050/userinfo");
        verification.put("evidence", evidence);


        JSONObject idVerification = new JSONObject();
        idVerification.put("trust_framework", "eidas");

        OIDCClaimsRequest claims = new OIDCClaimsRequest()
                .withIDTokenVerifiedClaimsRequest(
                        new VerifiedClaimsSetRequest()
                                .withVerificationJSONObject(idVerification)
                                .add("family_name")
                                .add("given_name")
                                .add("birthdate")
                                .add("person_identifier")
                                .add("place_of_birth")
                                .add("address")
                                .add("gender")
                )
                .withUserInfoVerifiedClaimsRequest(
                        new VerifiedClaimsSetRequest()
                                .withVerificationJSONObject(verification)
                                .add("family_name")
                                .add("given_name")
                                .add("birthdate")
                                .add("legal_name")
                                .add("legal_person_identifier")
                                .add("lei")
                                .add("vat_registration")
                                .add("address")
                                .add("tax_reference")
                                .add("sic")
                                .add("business_role")
                                .add("sub_jurisdiction")
                                .add("trading_status")
                );






        model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims)+"&legal_person_identifier=375715X&legal_name=360Kompany AG");

        return "index";
    }

    @RequestMapping(value = "callback")
    public String callback(HttpServletRequest request,
                           HttpServletResponse response, Model model) {


        // model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims));
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        OIDCTokens tokens = gridsIssuer.requestToken(requestURL.append('?').append(queryString).toString());

        if (tokens == null)
        {
            model.addAttribute("errorMessage", "There was an issue with getting token");
            return "error";
        }

        request.getSession().setAttribute("TOKENS", tokens);

        return "redirect:profile";
    }

    @RequestMapping(value = "profile")
    public String profile(HttpServletRequest request,
                          HttpServletResponse response, Model model) {

        OIDCTokens tokens = (OIDCTokens) request.getSession().getAttribute("TOKENS");

        if (tokens == null)
        {
            model.addAttribute("errorMessage", "There was an issue with getting token");
            return "error";
        }

        model.addAttribute("idToken", tokens.getIDTokenString());

        UserInfo userInfo = gridsIssuer.getUserInfo(tokens.getAccessToken().getValue());

        if (userInfo == null)
        {
            model.addAttribute("errorMessage", "There was an issue with getting user info");
            return "error";
        }

        Set<DistributedClaims> set = userInfo.getDistributedClaims();
        for (DistributedClaims claims : set) {

            String token = claims.getAccessToken().toString();

            model.addAttribute("dpToken", token);


//
//            WebClient client = WebClient.create();
//
//            String body = "717ca0c9-f7bb-45da-a1be-fa0c55ba106b";
//
//            String iToken ="eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjpbIlJPTEVfVVNFUiJdLCJzdWIiOiI3MTdjYTBjOS1mN2JiLTQ1ZGEtYTFiZS1mYTBjNTViYTEwNmIiLCJpYXQiOjE2MzAzMjE5NzksImV4cCI6MTYzMDM1MDc3OX0.RYhPLXpOuKrbAVGRU7lOC9fYH58ba6p1IF01vgYj3Hrms61GcUGQDZY8siJEC8sdUa0iSk5wordlZEFxKddS0Q";
//
//            String response2 = client.post()
//                    .uri( "https://vm.project-grids.eu:8481/dpc/dcIntrospection" )
//                    .contentType(MediaType.APPLICATION_JSON)
//                    .headers(headers -> headers.setBearerAuth(iToken))
//                    .bodyValue(body)
//                    .exchange()
//                    .block()
//                    .bodyToMono(String.class)
//                    .block();


            UserInfo dpUserInfo = gridsIssuer.getDPUserInfo(claims.getSourceEndpoint(), claims.getAccessToken().toString());




            if (dpUserInfo == null)
            {
                model.addAttribute("errorMessage", "There was an issue with getting dp user info");
                return "error";
            }
        }
        return "index";
    }
}



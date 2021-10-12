package com.adacom.gridsdctester.controllers;

import com.nimbusds.oauth2.sdk.ParseException;
import com.nimbusds.openid.connect.sdk.OIDCClaimsRequest;
import com.nimbusds.openid.connect.sdk.assurance.claims.VerifiedClaimsSetRequest;
import com.nimbusds.openid.connect.sdk.claims.DistributedClaims;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import eu.grids.sdk.service.Impl.GRIDSIssuer;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Create by szamanis - Adacom S.A.
 */
@Controller
public class LoginController {

    @Autowired
    private GRIDSIssuer gridsIssuer;

    @RequestMapping(value = {"", "1"})
    public String index(Model model) throws ParseException {


        JSONArray evidence = buildMinimalEvidence();


        JSONObject verification = new JSONObject();
        verification.put("trust_framework", "grids_kyb");
        verification.put("userinfo_endpoint", "https://dp.kompany.com:8050/userinfo");
        verification.put("evidence", evidence);


        JSONObject verification2 = new JSONObject();
        verification2.put("trust_framework", "grids_kyb");
        verification2.put("userinfo_endpoint", "https://dp.kompany.com:8060/userinfo");
        verification2.put("evidence", evidence);


        JSONObject idVerification = new JSONObject();
        idVerification.put("trust_framework", "eidas");

        List<VerifiedClaimsSetRequest> userInfoVerifiedList = new ArrayList<>();

        userInfoVerifiedList.add(new VerifiedClaimsSetRequest()
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
                .add("trading_status"));

        userInfoVerifiedList.add(new VerifiedClaimsSetRequest()
                .withVerificationJSONObject(verification2)
                .add("data")
        );


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
                .withUserInfoVerifiedClaimsRequestList(userInfoVerifiedList);


        model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims) + "&legal_person_identifier=375715X&legal_name=360Kompany AG");

        return "index";
    }

    private JSONArray buildMinimalEvidence() {

        JSONObject type = new JSONObject();
        type.put("value", "company_register");

        JSONObject registry = new JSONObject();
        JSONObject organisation = new JSONObject();
        JSONObject country = new JSONObject();

        organisation.put("essential", false);
        organisation.put("purpose", "string");

        country.put("essential", true);
        country.put("purpose", "string");
        country.put("value", "AT");

        registry.put("organisation", organisation);
        registry.put("country", country);

        JSONObject evidence = new JSONObject();
        evidence.put("type", type);
        evidence.put("registry", registry);

        JSONArray array = new JSONArray();

        array.add(evidence);

        return array;
    }

    @RequestMapping(value = "2")
    public String index2(Model model) throws ParseException {

        JSONArray evidence = buildMinimalEvidence();

        JSONObject verification = new JSONObject();
        verification.put("trust_framework", "grids_kyb");
        verification.put("userinfo_endpoint", "https://dp.kompany.com:8050/userinfo");
        verification.put("evidence", evidence);


        JSONObject idVerification = new JSONObject();
        idVerification.put("trust_framework", "eidas");

        List<VerifiedClaimsSetRequest> userInfoVerifiedList = new ArrayList<>();

        userInfoVerifiedList.add(new VerifiedClaimsSetRequest()
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
                .add("trading_status"));

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
                .withUserInfoVerifiedClaimsRequestList(userInfoVerifiedList);


        model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims) + "&legal_person_identifier=375715X&legal_name=360Kompany AG");

        return "index";
    }

    @RequestMapping(value = "3")
    public String index3(Model model) throws ParseException {

        JSONArray evidence = buildMinimalEvidence();

        JSONObject verification = new JSONObject();
        verification.put("trust_framework", "grids_kyb");
        verification.put("userinfo_endpoint", "https://dp.kompany.com:8050/userinfo");
        verification.put("evidence", evidence);


        JSONObject verification2 = new JSONObject();
        verification2.put("trust_framework", "grids_kyb");
        verification2.put("userinfo_endpoint", "https://dp.kompany.com:8060/userinfo");
        verification2.put("evidence", evidence);


        JSONObject idVerification = new JSONObject();
        idVerification.put("trust_framework", "eidas");

        List<VerifiedClaimsSetRequest> userInfoVerifiedList = new ArrayList<>();


        userInfoVerifiedList.add(new VerifiedClaimsSetRequest()
                .withVerificationJSONObject(verification2)
                .add("data")
        );


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
                .withUserInfoVerifiedClaimsRequestList(userInfoVerifiedList);


        model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims) + "&legal_person_identifier=375715X&legal_name=360Kompany AG");

        return "index";
    }

    @RequestMapping(value = "callback")
    public String callback(HttpServletRequest request,
                           HttpServletResponse response, Model model) {


        // model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims));
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();

        OIDCTokens tokens = gridsIssuer.requestToken(requestURL.append('?').append(queryString).toString());

        if (tokens == null) {
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

        if (tokens == null) {
            model.addAttribute("errorMessage", "There was an issue with getting token");
            return "error";
        }

        model.addAttribute("idToken", tokens.getIDTokenString());

        UserInfo userInfo = gridsIssuer.getUserInfo(tokens.getAccessToken().getValue());

        if (userInfo == null) {
            model.addAttribute("errorMessage", "There was an issue with getting user info");
            return "error";
        }

        model.addAttribute("userInfo", userInfo.toJSONString());

        Set<DistributedClaims> set = userInfo.getDistributedClaims();

        int index = 0;

        if (set != null) {

            for (DistributedClaims claims : set) {

                String token = claims.getAccessToken().toString();

                UserInfo dpUserInfo = gridsIssuer.getDPUserInfo(claims.getSourceEndpoint(), token);

                if (dpUserInfo != null) {
                    model.addAttribute("dpUserInfo" + index, dpUserInfo.toJSONString());
                } else {
                    model.addAttribute("dpUserInfo" + index, "null");
                }

                index++;
            }

        }
        return "index";
    }
}



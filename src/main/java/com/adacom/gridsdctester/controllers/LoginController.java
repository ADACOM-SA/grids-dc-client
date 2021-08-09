package com.adacom.gridsdctester.controllers;

import com.nimbusds.openid.connect.sdk.OIDCClaimsRequest;
import com.nimbusds.openid.connect.sdk.assurance.claims.VerifiedClaimsSetRequest;
import com.nimbusds.openid.connect.sdk.claims.DistributedClaims;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import com.nimbusds.openid.connect.sdk.token.OIDCTokens;
import eu.grids.sdk.service.Impl.GRIDSIssuer;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.List;
import java.util.Set;

/**
 * Create by szamanis - Adacom S.A.
 */
@Controller
public class LoginController {

    @Autowired
    private GRIDSIssuer gridsIssuer;

    @RequestMapping(value = "")
    public String index(Model model) {


        JSONObject verification = new JSONObject();
        verification.put("trust_framework", "grids_kyb");
        verification.put("userinfo_endpoint", "https://dp.kompany.com:8050/userinfo");

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


        claims.getIDTokenVerifiedClaimsRequestList().add(new VerifiedClaimsSetRequest())



        model.addAttribute("loginUrl", gridsIssuer.getAuthorizationUrl(claims));

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

        UserInfo userInfo = gridsIssuer.getUserInfo(tokens.getAccessToken().getValue());

        if (userInfo == null)
        {
            model.addAttribute("errorMessage", "There was an issue with getting user info");
            return "error";
        }

        Set<DistributedClaims> set = userInfo.getDistributedClaims();
        for (DistributedClaims claims : set) {

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



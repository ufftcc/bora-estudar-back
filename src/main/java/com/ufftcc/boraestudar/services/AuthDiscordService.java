package com.ufftcc.boraestudar.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class AuthDiscordService {

    @Value("${spring.security.oauth2.client.provider.discord.userInfoUri}")
    String userInfoUri;

    @Value("${spring.security.oauth2.client.provider.discord.tokenUri}")
    String tokenUri;

    @Value("${spring.security.oauth2.client.registration.discord.client-id}")
    String clientId;

    @Value("${spring.security.oauth2.client.registration.discord.client-secret}")
    String clientSecret;

    @Value("${spring.security.oauth2.client.registration.discord.authorizationGrantType}")
    String authorizationGrantType;

    @Value("${spring.security.oauth2.client.registration.discord.redirect-uri}")
    String redirectUri;

    @Value("${spring.security.oauth2.client.registration.discord.clientName}")
    String clientName;

    public String getDiscordUser(String code){

        String accessToken = getCodeBearerAuth(code);

        String url = userInfoUri;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<Object> header = new HttpEntity<>(body, headers);

        RestTemplateBuilder rtb = new RestTemplateBuilder();

        ResponseEntity<String> response = rtb.basicAuthentication(clientId,clientSecret).build().exchange(url, HttpMethod.GET, header, String.class);

        return response.toString();

    }

    public String getCodeBearerAuth(String code) {

        RestTemplate restTemplate = new RestTemplate();

        String url = UriComponentsBuilder.fromHttpUrl(tokenUri)
                .queryParam("client_id"     , clientId)
                .queryParam("client_secret" , clientSecret)
                .queryParam("grant_type"    , authorizationGrantType)
                .queryParam("code"          , code)
                .queryParam("redirect_uri"  , redirectUri)
                .queryParam("scope"         , "identify")
                .toUriString();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/x-www-form-urlencoded");
        headers.add("Accept-Encoding", "application/x-www-form-urlencoded");

        HttpEntity<Object> request = new HttpEntity<>(body, headers);

        RestTemplateBuilder rtb = new RestTemplateBuilder();
        ResponseEntity<String> response = rtb.basicAuthentication(clientId,clientSecret).build().postForEntity(url, request, String.class);

        // parte dois

        int startIndex = response.toString().indexOf("{");
        int endIndex = response.toString().indexOf("}");
        String jsonPart = response.toString().substring(startIndex, endIndex + 1);

        // Criar um ObjectMapper
        ObjectMapper mapper = new ObjectMapper();

        // Converter o JSON para um objeto JsonNode
        JsonNode root = null;
        try {
            root = mapper.readTree(jsonPart);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Extrair o valor do campo "access_token"
        String accessToken = root.get("access_token").asText();

        return accessToken;
    }


}

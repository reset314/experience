package com.example.experience.infrastructure.sync.adapter.builtin;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.experience.common.exception.SyncAPIAuthException;
import com.example.experience.common.exception.SyncAPIClientException;
import com.example.experience.common.exception.SyncAPIServiceException;
import com.example.experience.infrastructure.sync.adapter.FetchContext;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;
import com.example.experience.infrastructure.sync.adapter.SyncResult;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BiliBiliAdapter implements SyncAdapterHandler {

    private final RestClient biliClient = RestClient.builder()
        .defaultStatusHandler(HttpStatusCode::is5xxServerError, (req, res) -> {
            throw new SyncAPIServiceException("BiliBili API Service Error: " + res.getStatusCode());
        })
        .defaultStatusHandler(status -> status.value() == 401 || status.value() == 403, (req, res) -> {
            throw new SyncAPIAuthException("BiliBili API Auth Error: " + res.getStatusCode());
        })
        .defaultStatusHandler(HttpStatusCode::is4xxClientError, (req, res) -> {
            throw new SyncAPIClientException("BiliBili API Client Error: " + res.getStatusCode());
        })
        .build();
    
    @Override
    public SyncResult fetchEvents(FetchContext ctx) {
        return null;
    }

    public BiliDTO.LoginPageUrlAndQrcodeKeyResponse getLoginPageUrlAndQrcodeKey() {
        URI uri = UriComponentsBuilder.fromUriString(BiliHost.PASSPORT)
            .path(BiliUri.GETLOGINQRCODEURI)
            .build()
            .toUri();
        JsonNode response = biliClient.get()
            .uri(uri)
            .retrieve()
            .body(JsonNode.class);
        String responseUrl = response.get("data").get("url").asText();
        String responseQrcodeKey = response.get("data").get("qrcode_key").asText();
        return new BiliDTO.LoginPageUrlAndQrcodeKeyResponse(responseUrl, responseQrcodeKey);
    }

    public BiliDTO.LoginResultResponse getLoginResult(BiliDTO.LoginResultRequest request) {
        URI uri = UriComponentsBuilder.fromUriString(BiliHost.PASSPORT)
            .path(BiliUri.GETLOGINRESULTURI)
            .queryParam("qrcode_key", request.QrcodeKey)
            .build()
            .toUri();
        ResponseEntity<JsonNode> response = biliClient.get()
            .uri(uri)
            .retrieve()
            .toEntity(JsonNode.class);
        JsonNode body = response.getBody();
        HttpHeaders header = response.getHeaders();
        String url = body.get("url").asText();
        String refreshToken = body.get("refresh_token").asText();
        Long timestampV = body.get("timestamp").asLong();
        Instant timestamp = Instant.ofEpochMilli(timestampV);
        
        // header.forEach((headerName, headerValue) -> 
             
        // );

        return new BiliDTO.LoginResultResponse(url, refreshToken, timestamp, header);
    }

    private static final class BiliUri {
        public static final String GETLOGINQRCODEURI = "/x/passport-login/web/qrcode/generate";
        public static final String GETLOGINRESULTURI = "/x/passport-login/web/qrcode/poll";
        // public static final String 
    }

    private static final class BiliHost {
    public static final String PASSPORT = "https://passport.bilibili.com";
    public static final String API      = "https://api.bilibili.com";
}

    public static class BiliDTO {
        public record LoginPageUrlAndQrcodeKeyResponse(String responseUrl, String responseQrcodeKey){};
        public record LoginResultRequest(String QrcodeKey){};
        public record LoginResultResponse(String url, String refreshToken, Instant timestamp, HttpHeaders header){};
    }

}

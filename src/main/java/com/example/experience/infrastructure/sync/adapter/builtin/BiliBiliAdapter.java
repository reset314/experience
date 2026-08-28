package com.example.experience.infrastructure.sync.adapter.builtin;

import java.net.HttpCookie;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitRequest;
import com.example.experience.infrastructure.sync.adapter.auth.AuthInitResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    private final ObjectMapper objectMapper;

    public BiliBiliAdapter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public SyncResult fetchEvents(FetchContext ctx) {
        return null;
    }

    @Override
    public BiliDTO.LoginPageUrlAndQrcodeKeyResponse initiateAuth(AuthInitRequest requset){
        
        return null;
    }

    private BiliDTO.LoginPageUrlAndQrcodeKeyResponse getLoginPageUrlAndQrcodeKey() {
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

    private BiliDTO.LoginResultResponse getLoginResult(BiliDTO.LoginResultRequest request) {
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
        int code = body.get("data").get("code").asInt();
        if (code != 0) {
            throw new SyncAPIClientException("BiliBili API Client Error: " + code);
        }
        HttpHeaders headers = response.getHeaders();
        List<String> setCookieHeaders = headers.getOrEmpty(HttpHeaders.SET_COOKIE);
        List<BiliCookie> cookies = setCookieHeaders.stream()
            .flatMap(header -> HttpCookie.parse(header).stream())
            .map(cookie -> new BiliCookie(
                cookie.getName(),
                cookie.getValue(),
                cookie.getDomain(),
                cookie.getPath(),
                expiresFromMaxAge(cookie.getMaxAge()),
                cookie.getSecure(),
                cookie.isHttpOnly()
            ))
            .toList();

        if (cookies.stream().noneMatch(c -> "SESSDATA".equals(c.name()))) {
            throw new SyncAPIClientException("BiliBili login response missing SESSDATA cookie");
        }

        String url = body.get("url").asText();
        String refreshToken = body.get("refresh_token").asText();
        Long timestampV = body.get("timestamp").asLong();
        Instant timestamp = Instant.ofEpochMilli(timestampV);
        Instant expiresAt = cookies.stream()
            .map(BiliCookie::expires)
            .filter(exp -> exp != null)
            .min(Instant::compareTo)
            .orElse(null);

        BiliCookieCredential credential = new BiliCookieCredential(cookies, refreshToken, expiresAt);
        CredentialEnvelope<BiliCookieCredential> envelope = new CredentialEnvelope<>(
            1, "bilibili:cookie", Instant.now(), credential);

        String credentialJson;
        try {
            credentialJson = objectMapper.writeValueAsString(envelope);
        } catch (Exception e) {
            throw new SyncAPIClientException("Failed to serialize BiliBili credential: " + e.getMessage());
        }

        return new BiliDTO.LoginResultResponse(url, refreshToken, timestamp, credentialJson);
    } 

    private Instant expiresFromMaxAge(long maxAge) {
        return maxAge < 0 ? null : Instant.now().plusSeconds(maxAge);
    }

    private String toCookieHeader(List<BiliCookie> cookies) {
        return cookies.stream()
            .map(c -> c.name() + "=" + c.value())
            .collect(Collectors.joining("; "));
    }

    private static final class BiliUri {
        public static final String GETLOGINQRCODEURI = "/x/passport-login/web/qrcode/generate";
        public static final String GETLOGINRESULTURI = "/x/passport-login/web/qrcode/poll";
        public static final String GETIFCOOKIENEEDREFRESHURI = "/x/passport-login/web/cookie/info";
        public static final String GETMYINFOURI = "/x/member/web/account";
        public static final String GETFAVLISTURI
    }

    private static final class BiliHost {
    public static final String PASSPORT = "https://passport.bilibili.com";
    public static final String API      = "https://api.bilibili.com";
}

    public static record CredentialEnvelope<T>(int version, String type, Instant createdAt, T payload) {}

    public static record BiliCookieCredential(List<BiliCookie> cookies, String refreshToken, Instant expiresAt) {}

    public static record BiliCookie(
        String name,
        String value,
        String domain,
        String path,
        Instant expires,
        boolean secure,
        boolean httpOnly
    ) {}

    public static record BiliVideo(
        String url,
        boolean isLike,
        boolean isFav,
        boolean isCoin,
        BiliFavFolder favFolder,
        
    ){}

    public static record BiliFavFolder(){}

    public static record BiliUp(
        String spaceUrl,
        boolean isSubscribe
    ){}

    private static class BiliDTO {
        public record LoginPageUrlAndQrcodeKeyResponse(String responseUrl, String responseQrcodeKey) implements AuthInitResponse{}
        public record LoginResultRequest(String QrcodeKey){}
        public record LoginResultResponse(String url, String refreshToken, Instant timestamp, String credentialJson){}
        public record MyInfoGetRequest(){}
        public record MyInfoGetResponse(int mid, String uname, String userId, String sign, Instant birthday, String sex, String rank){}
        public record GetFavlistRequest(){}
        public record GetFavlistResponse(){}
        public record 
    }

}

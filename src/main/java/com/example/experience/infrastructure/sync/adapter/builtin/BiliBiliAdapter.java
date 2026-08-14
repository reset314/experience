package com.example.experience.infrastructure.sync.adapter.builtin;

import org.checkerframework.common.returnsreceiver.qual.This;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.example.experience.infrastructure.sync.adapter.FetchContext;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;
import com.example.experience.infrastructure.sync.adapter.SyncResult;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BiliBiliAdapter implements SyncAdapterHandler {

    private final RestClient biliClient = RestClient.create();
    
    @Override
    public SyncResult fetchEvents(FetchContext ctx) {
        return null;
    }

    public BiliDTO.LoginPageUrlAndQrcodeKeyDTO getLoginPageUrlAndQrcodeKey() {
        JsonNode response = biliClient.get()
            .uri(BiliUri.GETLOGINQRCODEURI)
            .retrieve()
            .body(JsonNode.class);
        String responseUrl = response.get("data").get("url").asText();
        String responseQrcodeKey = response.get("data").get("qrcode_key").asText();
        return new BiliDTO.LoginPageUrlAndQrcodeKeyDTO(responseUrl, responseQrcodeKey);
    }

    public 

    private static final class BiliUri {
        public static final String GETLOGINQRCODEURI = "";
        public static final String GETLOGINRESULTURI = "";
        // public static final String 
    }

    public static class BiliDTO {
        public record LoginPageUrlAndQrcodeKeyDTO(String responseUrl, String responseQrcodeKey){};
        public record LoginResultDTO(String)
    }

}

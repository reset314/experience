package com.example.experience.infrastructure.sync.adapter.builtin;

import com.example.experience.infrastructure.sync.adapter.*;

public class GithubAdapter implements SyncAdapterHandler {

    @Override
    public AdapterManifest manifest(){
        return AdapterManifest.builder().version("1").build(); 
    }

    @Override
    public void fetchEvents(FetchContext ctx) {
    }

    @Override
    public void uploadEvent(FetchContext ctx) {
    }

    @Override
    public AuthResult initAuth(FetchContext ctx, AuthRequest authRequest) {
        return null;
    }

    @Override
    public AuthResult cheakAuthState(FetchContext ctx, AuthRequest authRequest) {
        return null;
    }

    @Override
    public AuthResult continueAuth(FetchContext ctx, AuthRequest authRequest) {
        return null;
    }

}

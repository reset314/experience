package com.example.experience.infrastructure.sync.adapter.builtin;

import com.example.experience.infrastructure.sync.adapter.AdapterManifest;
import com.example.experience.infrastructure.sync.adapter.FetchContext;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;
import com.example.experience.infrastructure.sync.adapter.SyncResult;

public class DiaryAdapter implements SyncAdapterHandler {

    @Override
    public AdapterManifest manifest() {
        return AdapterManifest.builder().version("1").build();
    }

    @Override
    public void fetchEvents(FetchContext ctx) {
    }

    @Override
    public void uploadEvent(FetchContext ctx) {
    }

}

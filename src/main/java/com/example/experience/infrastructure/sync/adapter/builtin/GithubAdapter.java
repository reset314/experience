package com.example.experience.infrastructure.sync.adapter.builtin;

import com.example.experience.infrastructure.sync.adapter.FetchContext;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;
import com.example.experience.infrastructure.sync.adapter.SyncResult;

public class GithubAdapter implements SyncAdapterHandler {

    @Override
    public SyncResult fetchEvents(FetchContext ctx) {
        return null;
    }

    @Override
    public SyncResult uploadEvent(FetchContext ctx) {
        return null;
    }

}

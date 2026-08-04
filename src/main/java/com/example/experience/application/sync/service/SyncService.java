package com.example.experience.application.sync.service;

import com.example.experience.application.sync.dto.SyncLogDetailRequest;
import com.example.experience.application.sync.dto.SyncLogDetailResponse;
import com.example.experience.application.sync.dto.SyncLogListRequest;
import com.example.experience.application.sync.dto.SyncLogListResponse;

public interface SyncService {
    SyncLogListResponse getSyncLogs(SyncLogListRequest request);

    SyncLogDetailResponse getSyncLogDetail(SyncLogDetailRequest request);
}

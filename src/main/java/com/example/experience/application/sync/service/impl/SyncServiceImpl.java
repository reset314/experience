package com.example.experience.application.sync.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.experience.application.sync.dto.SyncLogDetailRequest;
import com.example.experience.application.sync.dto.SyncLogDetailResponse;
import com.example.experience.application.sync.dto.SyncLogListRequest;
import com.example.experience.application.sync.dto.SyncLogListResponse;
import com.example.experience.application.sync.dto.SyncLogResponse;
import com.example.experience.application.sync.service.SyncService;
import com.example.experience.domain.sync.entity.SyncLog;
import com.example.experience.domain.sync.repository.SyncLogRepository;
import com.example.experience.domain.sync.repository.SyncLogSpecifications;
import com.example.experience.infrastructure.log.LogConstants;
import com.example.experience.infrastructure.log.MdcHelper;
import com.example.experience.infrastructure.security.AuthorizationService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import static net.logstash.logback.argument.StructuredArguments.kv;

@Service
@RequiredArgsConstructor
@Slf4j
public class SyncServiceImpl implements SyncService {
    private final SyncLogRepository syncLogRepository;

    private final AuthorizationService authorizationService;

    @Override
    public SyncLogListResponse getSyncLogs(SyncLogListRequest request) {
        Pageable pageable = buildPageable(request);
        Specification<SyncLog> spec = SyncLogSpecifications.fromRequest(request);

        Page<SyncLog> page = syncLogRepository.findAll(spec, pageable);

        MdcHelper.put(LogConstants.MdcKeys.EVENT_TYPE, LogConstants.EventTypes.SYNC_SUCCESS);
        try {
            log.info("sync_log_list_query_success",
                kv("desc", "成功查询同步日志列表"),
                kv("userId", request.userId()),
                kv("page", page.getNumber()),
                kv("size", page.getSize()),
                kv("total", page.getTotalElements()));
        } finally {
            MdcHelper.remove(LogConstants.MdcKeys.EVENT_TYPE);
        }

        return new SyncLogListResponse(
                page.getContent().stream()
                    .map(this::toListResponse)
                    .toList(),
                page.getTotalElements(),
                page.getNumber(),
                page.getSize());
    }

    @Override
    public SyncLogDetailResponse getSyncLogDetail(SyncLogDetailRequest request) {
        SyncLog syncLog = authorizationService.requireOwned(
                request.userId(),
                request.syncLogId(),
                "SyncLogDetailRequest",
                syncLogRepository::findById,
                entity -> entity.getCreatedBy());

        MdcHelper.put(LogConstants.MdcKeys.EVENT_TYPE, LogConstants.EventTypes.SYNC_SUCCESS);
        try {
            log.info("sync_log_detail_query_success",
                kv("desc", "成功查询"),
                kv("syncLogId", syncLog.getId()),
                kv("dataSourceId", syncLog.getDataSource().getId()),
                kv("status", syncLog.getStatus()));
        } finally {
            MdcHelper.remove(LogConstants.MdcKeys.EVENT_TYPE);
        }

        return toResponse(syncLog);
    }

    private SyncLogDetailResponse toResponse(SyncLog syncLog) {
        return new SyncLogDetailResponse(
                syncLog.getId(),
                syncLog.getCreatedBy(),
                syncLog.getDataSource().getId(),
                syncLog.getDataSource().getDisplayName(),
                syncLog.getStartedAt().toString(),
                syncLog.getFinishedAt() != null ? syncLog.getFinishedAt().toString() : null,
                syncLog.getStatus(),
                syncLog.getErrorMessage(),
                syncLog.getContext(),
                syncLog.getEventsFetched(),
                syncLog.getEventsInserted(),
                syncLog.getMediaDownloaded()
        );
    }

    private Pageable buildPageable(SyncLogListRequest request) {
        int page = request.page() != null ? request.page().intValue() : 0;
        int size = request.size() != null ? request.size().intValue() : 20;

        String sortBy = request.sortBy();
        if (!isAllowedSortField(sortBy)) {
            sortBy = "startedAt";
        }

        Sort.Direction direction = Sort.Direction.DESC;
        if (request.sortDirection() != null && request.sortDirection().equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        return PageRequest.of(page, size, Sort.by(direction, sortBy));
    }

    private boolean isAllowedSortField(String sortBy) {
        return sortBy != null && (
            sortBy.equals("startedAt") ||
            sortBy.equals("finishedAt") ||
            sortBy.equals("status") ||
            sortBy.equals("createdAt"));
    }

    private SyncLogResponse toListResponse(SyncLog syncLog) {
        return new SyncLogResponse(
                syncLog.getId(),
                syncLog.getCreatedBy(),
                syncLog.getDataSource().getId(),
                syncLog.getDataSource().getDisplayName(),
                syncLog.getStartedAt().toString(),
                syncLog.getFinishedAt() != null ? syncLog.getFinishedAt().toString() : null,
                syncLog.getStatus(),
                syncLog.getEventsFetched(),
                syncLog.getEventsInserted(),
                syncLog.getMediaDownloaded()
        );
    }
}

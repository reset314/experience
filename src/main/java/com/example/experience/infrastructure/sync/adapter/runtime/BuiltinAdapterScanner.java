package com.example.experience.infrastructure.sync.adapter.runtime;

import java.util.List;
import java.util.Optional;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import com.example.experience.common.utils.Uuid7Utils;
import com.example.experience.domain.sync.entity.SyncAdapter;
import com.example.experience.domain.sync.repository.SyncAdapterRepository;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class BuiltinAdapterScanner {

    private final SyncAdapterRepository syncAdapterRepository;
    private final List<SyncAdapterHandler> builtinHandlers;

    @EventListener(ApplicationReadyEvent.class)
    public void scanAndRegister() {
        for (SyncAdapterHandler handler : builtinHandlers) {
            Class<?> handlerClass = ClassUtils.getUserClass(handler);
            String className = handlerClass.getName();
            String adapterKey = handlerClass.getSimpleName();
            String displayName = adapterKey;

            Optional<SyncAdapter> existing = syncAdapterRepository.findByAdapterKey(adapterKey);
            SyncAdapter adapter = existing.orElseGet(() -> SyncAdapter.builder()
                    .id(Uuid7Utils.generateUuid7())
                    .adapterKey(adapterKey)
                    .build());

            adapter.setAdapterType("builtin");
            adapter.setClassName(className);
            adapter.setDisplayName(displayName);
            adapter.setVisibility("public");
            adapter.setStatus("active");
            adapter.setCreatedBy("system");
            adapter.setSourcePath(null);
            adapter.setCompiledPath(null);

            syncAdapterRepository.save(adapter);
            log.info("Registered builtin adapter: {}", adapterKey);
        }
    }
}

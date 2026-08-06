package com.example.experience.infrastructure.sync.adapter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.example.experience.common.exception.AdapterLoadException;
import com.example.experience.domain.sync.entity.SyncAdapter;
import com.example.experience.domain.sync.repository.SyncAdapterRepository;
import com.example.experience.infrastructure.sync.adapter.runtime.UploadedAdapterLoader;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SyncAdapterRegistry {

    private final SyncAdapterRepository syncAdapterRepository;
    private final UploadedAdapterLoader uploadedAdapterLoader;
    private final ApplicationContext applicationContext;
    private final Map<String, SyncAdapterHandler> uploadedCache = new ConcurrentHashMap<>();

    public SyncAdapterHandler getHandler(String adapterKey) {
        SyncAdapter adapter = syncAdapterRepository.findByAdapterKey(adapterKey)
                .orElseThrow(() -> new AdapterLoadException("Adapter not found: " + adapterKey));

        if ("builtin".equals(adapter.getAdapterType())) {
            try {
                Class<?> handlerClass = Class.forName(adapter.getClassName());
                return applicationContext.getBean(handlerClass.asSubclass(SyncAdapterHandler.class));
            } catch (ClassNotFoundException e) {
                throw new AdapterLoadException("Builtin adapter class not found: " + adapter.getClassName(), e);
            }
        }

        return uploadedCache.computeIfAbsent(adapterKey, k -> uploadedAdapterLoader.load(adapter));
    }
}

package com.example.experience.infrastructure.sync.adapter.runtime;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.springframework.stereotype.Component;

import com.example.experience.common.exception.AdapterLoadException;
import com.example.experience.domain.sync.entity.SyncAdapter;
import com.example.experience.infrastructure.sync.adapter.SyncAdapterHandler;

@Component
public class UploadedAdapterLoader {

    public SyncAdapterHandler load(SyncAdapter adapter) {
        String compiledPath = adapter.getCompiledPath();
        if (compiledPath == null || compiledPath.isBlank()) {
            throw new AdapterLoadException("Compiled path is missing for adapter: " + adapter.getAdapterKey());
        }

        File compiledDir = new File(compiledPath);
        if (!compiledDir.exists() || !compiledDir.isDirectory()) {
            throw new AdapterLoadException("Compiled directory does not exist: " + compiledPath);
        }

        try {
            URL[] urls = { compiledDir.toURI().toURL() };
            URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
            Class<?> clazz = Class.forName(adapter.getClassName(), true, classLoader);
            Object instance = clazz.getDeclaredConstructor().newInstance();
            if (!(instance instanceof SyncAdapterHandler handler)) {
                throw new AdapterLoadException("Class does not implement SyncAdapterHandler: " + adapter.getClassName());
            }
            return handler;
        } catch (MalformedURLException e) {
            throw new AdapterLoadException("Invalid compiled path: " + compiledPath, e);
        } catch (ClassNotFoundException e) {
            throw new AdapterLoadException("Adapter class not found: " + adapter.getClassName(), e);
        } catch (ReflectiveOperationException e) {
            throw new AdapterLoadException("Failed to instantiate adapter: " + adapter.getClassName(), e);
        }
    }
}

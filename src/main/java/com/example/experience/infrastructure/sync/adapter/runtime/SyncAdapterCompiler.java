package com.example.experience.infrastructure.sync.adapter.runtime;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.springframework.stereotype.Component;

import com.example.experience.common.exception.AdapterLoadException;

@Component
public class SyncAdapterCompiler {

    public void compile(String sourcePath, String outputPath) {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new AdapterLoadException("Source file not found: " + sourcePath);
        }

        File outputDir = new File(outputPath);
        if (!outputDir.exists() && !outputDir.mkdirs()) {
            throw new AdapterLoadException("Failed to create output directory: " + outputPath);
        }

        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new AdapterLoadException("System Java compiler is not available");
        }

        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        try (StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, StandardCharsets.UTF_8)) {
            Iterable<? extends JavaFileObject> compilationUnits = fileManager.getJavaFileObjects(sourceFile);
            JavaCompiler.CompilationTask task = compiler.getTask(
                    null,
                    fileManager,
                    diagnostics,
                    Arrays.asList("-d", outputPath),
                    null,
                    compilationUnits);

            boolean success = task.call();
            if (!success) {
                StringBuilder sb = new StringBuilder("Compilation failed:");
                for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
                    sb.append("\n").append(diagnostic.getMessage(null));
                }
                throw new AdapterLoadException(sb.toString());
            }
        } catch (IOException e) {
            throw new AdapterLoadException("Failed to compile adapter source: " + sourcePath, e);
        }
    }
}

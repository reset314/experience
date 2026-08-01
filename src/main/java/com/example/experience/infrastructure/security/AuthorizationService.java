package com.example.experience.infrastructure.security;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.example.experience.common.exception.AccessDeniedException;
import com.example.experience.common.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthorizationService {

    /**
     * 校验资源存在且归属指定用户，返回资源实体。
     *
     * @param userId         当前登录用户 id（由调用方传入）
     * @param resourceId     资源 id（各表主键 id 字段）
     * @param resourceName   资源名，用于 404 异常与日志
     * @param findById       按 id 查询资源，例如 xxxRepository::findById
     * @param ownerExtractor 提取资源归属人，例如 Xxx::getCreatedBy
     * @return 资源实体
     * @throws ResourceNotFoundException 资源不存在（404）
     * @throws AccessDeniedException     资源存在但归属不是该用户（403）
     */
    public <T> T requireOwned(String userId, String resourceId, String resourceName,
                              Function<String, Optional<T>> findById,
                              Function<T, String> ownerExtractor) {
        T resource = findById.apply(resourceId)
            .orElseThrow(() -> new ResourceNotFoundException(resourceName, resourceId));
        if (!Objects.equals(userId, ownerExtractor.apply(resource))) {
            throw new AccessDeniedException(
                String.format("User %s has no access to %s with id '%s'",
                    userId, resourceName, resourceId));
        }
        return resource;
    }
}

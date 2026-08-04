package com.example.experience.domain.media.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.experience.domain.media.entity.MediaFile;

public interface MediaFileRepository extends JpaRepository<MediaFile, String>, JpaSpecificationExecutor<MediaFile> {
    List<MediaFile> findByUserId(String userId);

    List<MediaFile> findByDataSourceId(String dataSourceId);

    Optional<MediaFile> findByChecksumSha256(String checksumSha256);

    List<MediaFile> findByCreatedBy(String createdBy);

    Optional<MediaFile> findByIdAndCreatedBy(String id, String createdBy);
}

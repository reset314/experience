package com.example.experience.infrastructure.sync.adapter;

import java.util.Set;
import lombok.Builder;

@Builder
public record EntitySpec(
        String Type,//声明一个实体,这个字段不用严格等实体对应的java的record类名
        String DisplayName,
        TypedPath idField,
        String DisplayNameField,
        Set<String> ignoreFields,// 表示ChangeLog忽略的值
        int schemaVersion
        ) {

}

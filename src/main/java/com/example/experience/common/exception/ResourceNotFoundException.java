package com.example.experience.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    // 资源名
    private final String resourceName;
    // 字段名
    private final String fieldName;
    // 查询值
    private final Object fieldValue;

    public ResourceNotFoundException(String resourceName, String id) {
        super(String.format("%s not found with id : '%s'", resourceName, id));
        this.resourceName = resourceName;
        this.fieldName = "id";
        this.fieldValue = id;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getResourceName() { return resourceName; }
    public String getFieldName() { return fieldName; }
    public Object getFieldValue() { return fieldValue; }
}

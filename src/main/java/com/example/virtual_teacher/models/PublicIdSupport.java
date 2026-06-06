package com.example.virtual_teacher.models;

import java.util.UUID;

public final class PublicIdSupport {

    private PublicIdSupport() {
    }

    public static void ensureAssigned(PublicIdentifiable entity) {
        if (entity.getPublicId() == null || entity.getPublicId().isBlank()) {
            entity.setPublicId(UUID.randomUUID().toString());
        }
    }
}

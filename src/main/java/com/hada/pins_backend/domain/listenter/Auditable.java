package com.hada.pins_backend.domain.listenter;

import java.time.LocalDateTime;

/**
 * Created by bangjinhyuk on 2021/08/04.
 */
public interface Auditable {
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
}

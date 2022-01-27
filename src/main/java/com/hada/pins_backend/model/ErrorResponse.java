package com.hada.pins_backend.model;

import lombok.*;

/*
 * Created by parksuho on 2022/01/19.
 * Modified by parksuho on 2022/01/26.
 */
@Getter
@RequiredArgsConstructor
public final class ErrorResponse {
    private final String message;
}
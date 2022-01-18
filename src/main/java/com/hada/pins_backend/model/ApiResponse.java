package com.hada.pins_backend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/*
 * Created by parksuho on 2022/01/19.
 */
@Getter
@Setter
@RequiredArgsConstructor
public final class ApiResponse<T>{
    private final T data;
}

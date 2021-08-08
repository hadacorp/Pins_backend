package com.hada.pins_backend.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by bangjinhyuk on 2021/08/08.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VerifyResult {

    private boolean success;
    private String username;
}

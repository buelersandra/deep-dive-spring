package com.sika.contact.domain.response;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class BaseResponseDTO<T> {
    private String message;
    private T data;
}

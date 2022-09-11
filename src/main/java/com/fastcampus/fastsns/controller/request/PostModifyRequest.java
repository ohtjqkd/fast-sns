package com.fastcampus.fastsns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostModifyRequest {
    private String title;
    private String body;
}

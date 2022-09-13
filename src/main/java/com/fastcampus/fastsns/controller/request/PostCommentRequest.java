package com.fastcampus.fastsns.controller.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor // Response<Void> type은 해당 어노테이션이 필요한듯 보임
@Data
public class PostCommentRequest {
    private String comment;
}

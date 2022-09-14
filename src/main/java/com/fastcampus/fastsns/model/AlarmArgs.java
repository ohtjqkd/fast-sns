package com.fastcampus.fastsns.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlarmArgs {
    private Integer fromUserId;
    private Integer targetId;
}

package com.fastcampus.fastsns.model;

import com.fastcampus.fastsns.model.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
public class User {
    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;
    private Timestamp registerAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User fromEntity(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRole(),
                entity.getRegisterAt(),
                entity.getUpdatedAt(),
                entity.getDeleteAt()
        );
    }
}

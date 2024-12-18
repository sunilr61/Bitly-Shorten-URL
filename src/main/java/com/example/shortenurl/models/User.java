package com.example.shortenurl.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name="users")
public class User extends BaseModel{
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private UserPlan userPlan;
}

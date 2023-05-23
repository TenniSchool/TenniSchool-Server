package com.TenniSchool.tenniSchool.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AuditingTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String  nickName;

    @Column(nullable = false)
    private String  email;

    @Column(nullable = false)
    private String password;
    @Builder
    private User(String nickName, String email, String password){
        this.nickName = nickName;
        this.email = email;
        this.password = password;
    }

}

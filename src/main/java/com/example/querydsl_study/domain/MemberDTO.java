package com.example.querydsl_study.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {
    private String userName;
    private String teamName;
    public MemberDTO(String userName, String teamName){
        this.userName = userName;
        this.teamName = teamName;
    }
}

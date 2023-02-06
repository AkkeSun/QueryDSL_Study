package com.example.querydsl_study;

import com.example.querydsl_study.domain.Member;
import com.example.querydsl_study.domain.Team;
import com.example.querydsl_study.repository.MemberRepository;
import com.example.querydsl_study.repository.TeamRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
public class DataJpaTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TeamRepository teamRepository;

    @Test
    public void save(){

        Team team = new Team("team1");
        teamRepository.save(team);

        Member member1 = new Member("member1", team);
        Member member2 = new Member("member2", team);
        memberRepository.save(member1);
        memberRepository.save(member2);

        for (Member member : memberRepository.findAll()) {
            log.info("userId : {} || userName : {}, || teamId : {}",
                member.getId(), member.getUserName(), member.getTeam().getId());
        }
    }
}

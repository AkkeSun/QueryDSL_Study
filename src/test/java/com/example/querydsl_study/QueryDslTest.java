package com.example.querydsl_study;

import com.example.querydsl_study.domain.Member;
import com.example.querydsl_study.domain.MemberDTO;
import com.example.querydsl_study.domain.QMember;
import com.example.querydsl_study.domain.Team;
import com.example.querydsl_study.repository.MemberQueryDslSupport;
import com.example.querydsl_study.repository.MemberRepository;
import com.example.querydsl_study.repository.TeamRepository;
import com.querydsl.core.Tuple;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
@Transactional
public class QueryDslTest {

    @Autowired
    private MemberQueryDslSupport queryDslSupport;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    public void init(){

        Team team = new Team("t1");
        teamRepository.save(team);

        Member member1 = new Member("m1", team);
        Member member2 = new Member("m2", team);
        Member member3 = new Member("m3", team);
        Member member4 = new Member("m4", team);
        Member member5 = new Member("m5", team);
        Member member6 = new Member("m6", team);
        Member member7 = new Member("m7", team);
        Member member8 = new Member("m8", team);
        Member member9 = new Member("m9", team);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);
        memberRepository.save(member5);
        memberRepository.save(member6);
        memberRepository.save(member7);
        memberRepository.save(member8);
        memberRepository.save(member9);
    }

    @Test
    public void findAll(){
        for (Member member : queryDslSupport.findAll()) {
            log.info("memberId : {} || meberName : {} || teamId : {}",
                member.getId(), member.getUserName(), member.getTeam().getId());
        }
    }

    @Test
    public void findAllOrderByMemberId(){
        for (Member member : queryDslSupport.findAllOrderByMemberId()) {
            log.info("memberId : {} || meberName : {} || teamId : {}",
                member.getId(), member.getUserName(), member.getTeam().getId());
        }
    }

    @Test
    public void findAllPaging(){
        Pageable pageable = PageRequest.of(0, 5);
        for (Member member : queryDslSupport.findAll(pageable)) {
            log.info("memberId : {} || meberName : {} || teamId : {}",
                member.getId(), member.getUserName(), member.getTeam().getId());
        }
    }

    @Test
    public void finaTupleGroupByTeamId(){
        QMember member = QMember.member;
        for (Tuple tuple : queryDslSupport.finaTupleGroupByTeamId()) {
            log.info("teamName : {} || memberCount: {}",
                tuple.get(member.team.name), tuple.get(member.count()));
        }
    }

    @Test
    public void findByUserName(){
       Member m = queryDslSupport.findByUserName("m1").get();
        Assertions.assertEquals(m.getUserName(), "m1");
        Assertions.assertEquals(m.getTeam().getName(), "t1");
    }

    @Test
    public void findTop1(){
        Member m = queryDslSupport.findTop1().get();
        log.info("memberId : {} || meberName : {} || teamId : {}",
            m.getId(), m.getUserName(), m.getTeam().getId());
    }

    @Test
    public void findByIdUserNameAndTeamName(){
        MemberDTO memberDTO = new MemberDTO("m1", "t1");
        Member m = queryDslSupport.findByIdUserNameAndTeamName(memberDTO).get();
        Assertions.assertEquals(m.getUserName(), "m1");
        Assertions.assertEquals(m.getTeam().getName(), "t1");
    }

    @Test
    public void findBySubQuery(){
        Member m = queryDslSupport.findBySubQuery("m1").get();
        Assertions.assertEquals(m.getUserName(), "m1");
        Assertions.assertEquals(m.getTeam().getName(), "t1");
    }

    @Test
    public void findByMemberJoin(){
        Member m = queryDslSupport.findByMemberJoin("m1").get();
        Assertions.assertEquals(m.getUserName(), "m1");
        Assertions.assertEquals(m.getTeam().getName(), "t1");
    }

    @Test
    public void findByMemberJoinFetch(){
        Member m = queryDslSupport.findByMemberJoinFetch("m1").get();
        Assertions.assertEquals(m.getUserName(), "m1");
        Assertions.assertEquals(m.getTeam().getName(), "t1");
    }
}

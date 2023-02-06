package com.example.querydsl_study.repository;

import com.example.querydsl_study.domain.Member;
import com.example.querydsl_study.domain.MemberDTO;
import com.querydsl.core.Tuple;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryDslSupport {

    List<Member> findAll();
    List<Member> findAllOrderByMemberId();
    List<Tuple> finaTupleGroupByTeamId();
    Page<Member> findAll(Pageable pageable);
    Optional<Member> findByUserName(String userName);
    Optional<Member> findTop1();
    Optional<Member> findByIdUserNameAndTeamName(MemberDTO memberDTO);
    Optional<Member> findBySubQuery(String userName);
    Optional<Member> findByMemberJoin(String userName);
    Optional<Member> findByMemberJoinFetch(String userName);
}

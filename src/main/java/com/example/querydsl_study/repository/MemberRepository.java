package com.example.querydsl_study.repository;

import com.example.querydsl_study.domain.Member;
import com.example.querydsl_study.domain.MemberDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    List<Member> findAll();
    Page<Member> findAll(Pageable pageable);
    Optional<Member> findById(String id);
    Optional<Member> findTop1ById(String id);

    Optional<Member> findByIdUserNameAndTeamName(MemberDTO memberDTO);

}

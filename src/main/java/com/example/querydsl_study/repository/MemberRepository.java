package com.example.querydsl_study.repository;

import com.example.querydsl_study.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MemberRepository extends JpaRepository<Member, Integer> {
}

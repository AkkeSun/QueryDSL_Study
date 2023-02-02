package com.example.querydsl_study.repository;

import com.example.querydsl_study.domain.Member;
import com.example.querydsl_study.domain.MemberDTO;
import com.example.querydsl_study.domain.QMember;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository{

    private final JPAQueryFactory query;

    // 기본 Q 생성 (쿼리타입)
    // 같은 엔티티를 조인하거나 같은 엔티티를 서브쿼리에 사용하는 경우 생성자로 만들어줘야한다. 입력 파라미터는 별칭이다
    // QMember qMember = new QMember("m");
    private QMember member = QMember.member;

    // 리스트 리턴
    @Transactional(readOnly = true)
    public List<Member> findAll() {
        return query.selectFrom(member).fetch();
    }

    // 페이징 리스트 리턴
    @Transactional(readOnly = true)
    public Page<Member> findAll(Pageable pageable) {
        // Pageable pageable = PageRequest.of(index, size);
        QueryResults<Member> queryResults = query.selectFrom(member)
            .offset(pageable.getOffset())  // 페이지 인덱스
            .limit(pageable.getPageSize()) // 페이지 사이즈
            .fetchResults();
        List<Member> memberList = queryResults.getResults();
        long totalData = queryResults.getTotal();
        return new PageImpl<>(memberList, pageable, totalData);
    }

    // 단일 갑 리턴
    @Transactional(readOnly = true)
    public Optional <Member> findById(String id) {
        return Optional.ofNullable(query.selectFrom(member).where(member.id.eq(id)).fetchOne());
    }

    // 첫번째 값 리턴
    @Transactional(readOnly = true)
    public Optional<Member> findTop1ById(String id) {
        return Optional.ofNullable(query.selectFrom(member).where(member.id.eq(id)).fetchFirst());
    }

    // 동적쿼리 예제 (BooleanBuilder)
    @Transactional(readOnly = true)
    public Optional<Member> findByIdUserNameAndTeamName(MemberDTO memberDTO) {

        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(memberDTO.getUserName())){
            builder.and(member.userName.eq(memberDTO.getUserName()));
        }
        if(StringUtils.hasText(memberDTO.getTeamId())){
            builder.and(member.team.name.eq(memberDTO.getTeamId()));
        }
        return Optional.ofNullable(query.selectFrom(member).where(builder).fetchOne());
    }

    // 서브쿼리 예제

    // 정렬 예제

    // 그룹 예제

    // 리턴값 튜플

    // 조인
}

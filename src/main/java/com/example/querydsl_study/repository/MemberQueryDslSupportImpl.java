package com.example.querydsl_study.repository;

import com.example.querydsl_study.domain.Member;
import com.example.querydsl_study.domain.MemberDTO;
import com.example.querydsl_study.domain.QMember;
import com.example.querydsl_study.domain.QTeam;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
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
public class MemberQueryDslSupportImpl implements MemberQueryDslSupport {

    private final JPAQueryFactory query;

    // 기본 Q 생성 (쿼리타입)
    // 같은 엔티티를 조인하거나 같은 엔티티를 서브쿼리에 사용하는 경우 생성자로 만들어줘야한다. 입력 파라미터는 별칭이다
    // QMember qMember = new QMember("m");
    private QMember member = QMember.member;
    private QTeam team = QTeam.team;

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

    // 정렬 예제
    @Transactional(readOnly = true)
    public List<Member> findAllOrderByMemberId() {
        return query.selectFrom(member).orderBy(member.id.desc()).fetch();
    }

    // 그룹 + 튜플예지 (복수의 컬럼을 선택시 튜플타입으로 리턴된다)
    @Transactional(readOnly = true)
    public List<Tuple> finaTupleGroupByTeamId(){
        return query
            .select(member.team.name, member.count()).
            from(member)
            .groupBy(member.team.id)
            .fetch();
    }

    // 단일 갑 리턴
    @Transactional(readOnly = true)
    public Optional <Member> findByUserName(String userName) {
        return Optional.ofNullable(query.selectFrom(member).where(member.userName.eq(userName)).fetchOne());
    }

    // 첫번째 값 리턴
    @Transactional(readOnly = true)
    public Optional<Member> findTop1() {
        return Optional.ofNullable(query.selectFrom(member).fetchFirst());
    }

    // 동적쿼리 예제 (BooleanBuilder)
    // eq : equals. goe : 이상, gt : 초과, loe : 이하, it : 미만, between : a~b
    @Transactional(readOnly = true)
    public Optional<Member> findByIdUserNameAndTeamName(MemberDTO memberDTO) {

        BooleanBuilder builder = new BooleanBuilder();
        if(StringUtils.hasText(memberDTO.getUserName())){
            builder.and(member.userName.eq(memberDTO.getUserName()));
        }
        if(StringUtils.hasText(memberDTO.getTeamName())){
            builder.and(member.team.name.eq(memberDTO.getTeamName()));
        }
        return Optional.ofNullable(query.selectFrom(member).where(builder).fetchOne());
    }

    // 서브쿼리 예제
    public Optional<Member> findBySubQuery(String userName){
        return Optional.ofNullable(query.selectFrom(member)
            .where(member.id.eq(query.select(member.id)
                            .from(member)
                            .where(member.userName.eq(userName)).fetchOne()))
            .fetchOne());
    }

    // 기본 조인
    // join(), leftJoin(), rightJoin()
    public Optional<Member> findByMemberJoin(String userName){
        return Optional.ofNullable(
            query.selectFrom(member)
                .join(member.team, team)
                .where(member.userName.eq(userName))
                .fetchOne());
    }

    // Fetch Join : 연관관계 엔티티를 함께 조회
    public Optional<Member> findByMemberJoinFetch(String userName){
        return Optional.ofNullable(
            query.selectFrom(member)
                .join(member.team, team).fetchJoin()
                .where(member.userName.eq(userName))
                .fetchOne());
    }

}

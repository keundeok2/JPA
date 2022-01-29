package study.querydsl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.querydsl.entity.Member;
import static study.querydsl.entity.QMember.*;
import static study.querydsl.entity.QTeam.team;

import study.querydsl.entity.QMember;
import study.querydsl.entity.QTeam;
import study.querydsl.entity.Team;

import javax.persistence.EntityManager;
import java.util.List;

@SpringBootTest
@Transactional
public class QueryDslBasicTest {

    @Autowired
    EntityManager em;
    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);

        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

    }

//    @Test
    void startJPQL() {
        String queryString = "select m from Member m where m.username = :username";
        Member findByJpql = em.createQuery(queryString, Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findByJpql.getUsername()).isEqualTo("member1");
    }

//    @Test
    void startQuerydsl() {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QMember m = new QMember("m");

        Member findMember = queryFactory
                .select(m)
                .from(m)
                .where(m.username.eq("member1"))
                .fetchOne();

        // JPQL -> RUNTIME에서 오류를 확인할 수 있다.
        // Querydsl -> COMPILE 시점에서 오류를 확인할 수 있다.

        // JPQL -> parameter binding을 수동으로 해주어야한다.
        // Querydsl -> 자동으로 된다.

        assertThat(findMember.getUsername()).isEqualTo("member1");

    }

//    @Test
    void QType() {
        Member findMember = queryFactory
                .select(member)
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

//    @Test
    void search() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1"),
                        member.age.eq(10),
                        null) // and
                .fetchOne();

        // 메서드 체인 없이 파라미터만 나열되는 것은 AND 조건이다.
        // 여기서 null은 무시된다. 동적쿼리를 깔끔하게 만들 수 있음 -> 추후 강의

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

//    @Test
    void searchAndParam() {
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1")
                        .and(member.age.eq(10))
                )
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
        assertThat(findMember.getAge()).isEqualTo(10);
    }

    // Querydsl 연산자 지원
    // eq, ne, eq().not() [!=], isNotNull() [IS NOT NULL],
    // in, notIn, between, goe, gt, loe, lt,
    // like, contains, startsWith


//    @Test
    void resultFetch() {
        List<Member> fetch = queryFactory
                .selectFrom(member)
                .fetch();

        Member fetchOne = queryFactory
                .selectFrom(QMember.member)
                .fetchOne();

        Member fetchfirst = queryFactory
                .selectFrom(QMember.member)
                .fetchFirst(); // limit(1).fetchOne() 과 같다

        // count 쿼리까지 함께 실행
        // 성능을 위해 컨텐츠를 조회하는 쿼리와 카운트 쿼리가 다를 때가 있다.
        // 이 경우에는 fetchResults()를 사용하지 않고 count 쿼리를 따로 실행해야한다.
        // querydsl5.0 deprecated -> fetch()를 사용할 것
        QueryResults<Member> fetchResults = queryFactory
                .selectFrom(member)
                .fetchResults();

        fetchResults.getTotal(); // totalCount
        List<Member> results = fetchResults.getResults();
        fetchResults.getOffset(); // offset
        fetchResults.getLimit(); // limit

        // Count query
        // querydsl5.0 deprecated
        long fetchCount = queryFactory
                .selectFrom(member)
                .fetchCount();

        // querydsl5.0 Count query
        Long totalCount = queryFactory
                //.select(Wildcard.count) //select count(*)
                .select(member.count()) //select count(member.id)
                .from(member)
                .fetchOne();

    }

    /**
     * 검색 조건
     * 1. 회원 나이 내림차순
     * 2. 회원 이름 오름차순
     * 단 2.에서 회원 이름이 없을 시 마지막에 출력 (nulls last)
     */
//    @Test
    void sort() {

        em.persist(new Member(null, 100));
        em.persist(new Member("member5", 100));
        em.persist(new Member("member6", 100));

        List<Member> result = queryFactory
                .selectFrom(member)
                .where(member.age.eq(100))
                .orderBy(member.age.desc(), member.username.asc().nullsLast())
                .fetch();

        Member member5 = result.get(0);
        Member member6 = result.get(1);
        Member memberNull = result.get(2);
        assertThat(member5.getUsername()).isEqualTo("member5");
        assertThat(member6.getUsername()).isEqualTo("member6");
        assertThat(memberNull.getUsername()).isEqualTo(null);
    }

//    @Test
    void paging() {
        List<Member> result = queryFactory
                .selectFrom(member)
                .orderBy(member.username.desc())
                .offset(1)
                .limit(2)
                .fetch();

        assertThat(result.size()).isEqualTo(2);
    }

//    @Test
    void aggregation() {

        List<Tuple> result = queryFactory
                .select(
                        member.count(),
                        member.age.max(),
                        member.age.min(),
                        member.age.sum(),
                        member.age.avg()
                )
                .from(member)
                .fetch();

        Tuple tuple = result.get(0);
        assertThat(tuple.get(member.count())).isEqualTo(4);

    }

    @Test
    void grouping() {

        List<Tuple> result = queryFactory
                .select(team.name, member.age.avg())
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .fetch();

        Tuple teamA = result.get(0);
        Tuple teamB = result.get(1);

        assertThat(teamA.get(team.name)).isEqualTo("teamA");
        assertThat(teamA.get(member.age.avg())).isEqualTo(15);
        assertThat(teamB.get(team.name)).isEqualTo("teamB");
        assertThat(teamB.get(member.age.avg())).isEqualTo(35);

    }



}

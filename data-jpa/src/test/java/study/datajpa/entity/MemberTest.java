package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.repository.MemberRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {

    @PersistenceContext
    private EntityManager em;

    @Autowired private MemberRepository memberRepository;

//    @Test
    void testEntity() {
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);
        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

         em.persist(member1);
         em.persist(member2);
         em.persist(member3);
         em.persist(member4);

         em.flush();
         em.clear();

        List<Member> resultList = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : resultList) {
            System.out.println("member = " + member);
            System.out.println("--> member.getTeam() = " + member.getTeam());
        }

    }

    @Test
    void baseTest() throws Exception {
        // given
        Member member1 = new Member("member1");
        memberRepository.save(member1); // @prePersist
        // when
        List<Member> result = memberRepository.findAll();
        for (Member member : result) {
            System.out.println("member.getCreatedDate() = " + member.getCreatedDate());
            System.out.println("member.getLastModifiedDate() = " + member.getLastModifiedDate());
            System.out.println("member.getCreatedBy() = " + member.getCreatedBy());
            System.out.println("member.getLastModifiedBy() = " + member.getLastModifiedBy());
        }

    }

}
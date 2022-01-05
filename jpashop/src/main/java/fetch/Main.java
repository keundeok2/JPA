package fetch;

import fetch.domain.Member;
import fetch.domain.Team;
import org.hibernate.Hibernate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            {
                Team team = new Team();
                team.setName("team1");
                em.persist(team);

                Member member = new Member();
                member.setUsername("member1");
                member.setTeam(team);
                em.persist(member);

                em.flush();
                em.clear();

                Member findMember = em.find(Member.class, member.getId());

                // Team: proxy
                System.out.println("findMember = " + findMember.getTeam().getClass());

                System.out.println("===========================");
                // 실제 team을 사용하는 시점에 초기화
                System.out.println(findMember.getTeam().getName());
                System.out.println("===========================");

                // 예상치 못한 SQL 발생
                // EAGER로 걸려있는 필드들이 많으면 모든 테이블에서 데이터를 가져오는 SQL이 나간다.


            }

            {
                Team team = new Team();
                team.setName("team1");
                em.persist(team);

                Team teamB = new Team();
                teamB.setName("team1");
                em.persist(teamB);

                Member member = new Member();
                member.setUsername("member1");
                member.setTeam(team);
                em.persist(member);
                Member member2 = new Member();
                member.setUsername("member2");
                member.setTeam(teamB);
                em.persist(member2);

                em.flush();
                em.clear();

                // N + 1 발생 ( 1 -> JPQL 쿼리, N -> 조회된 Member의 갯수: Team을 select 해와야 함)
                List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();

                // N + 1 제거 방법
                // 1.fetch join(JPQL)
                // 지연 로딩이더라도 JOIN으로 모든 데이터를 가져옴
                List<Member> members2 = em.createQuery("select m from Member m join fetch m.team", Member.class).getResultList();


            }



            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            System.out.println("==== error ====");
            e.printStackTrace();
        } finally {
            em.close();
        }

        entityManagerFactory.close();
    }
}

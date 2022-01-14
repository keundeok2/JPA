package jpql;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class FetchJoinMain {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("member1");
            member1.setTeam(teamA);

            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("member3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

            /*
            // fetch join을 사용하지 않은 경우
            String query = "select m from Member m";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();

            for (Member member : resultList) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // Member 엔터티의 Team은 프록시이다. (지연 로딩)
                // 회원1, 팀A (SQL SELECT)
                // 회원2, 팀A (1차캐시에서 값 얻음)
                // 회원3, 팀B (SQL SELECT)
                // -> N + 1 문제 발생
            }
             */


            /*
            // fetch join 사용
            String query = "select m from Member m join fetch m.team t";
            List<Member> resultList = em.createQuery(query, Member.class)
                    .getResultList();
            for (Member member : resultList) {
                System.out.println("member = " + member.getUsername() + ", " + member.getTeam().getName());
                // Member 엔터티의 Team은 엔터티다. 마치 즉시 로딩 되는 것 처럼.
                // N + 1 문제 발생하지 않음
            }
             */

            /*
            // Collection fetch join
            String query = "select t from Team t join fetch t.members m";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .getResultList();
            for (Team team : resultList) {
                System.out.println("team.getName() = " + team.getName() +", members.size = " + team.getMembers().size());
                // 결과
                // teamA, size 2
                // teamA, size 2
                // teamB, size 1
            }
             */

            // 중복 제거
            String query = "select distinct t from Team t join fetch t.members m";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .getResultList();
            for (Team team : resultList) {
                System.out.println("team.getName() = " + team.getName() +", members.size = " + team.getMembers().size());
                // 결과
                // teamA, size 2
                // teamB, size 1
            }


            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        entityManagerFactory.close();
    }
}

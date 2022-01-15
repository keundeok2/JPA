package jpql;

import jpql.domain.Member;
import jpql.domain.MemberType;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Collection;
import java.util.List;

public class Main2 {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {


            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member1 = new Member();
            member1.setAge(10);
            member1.setUsername("member1");
            member1.setTeam(team);

            em.persist(member1);

            Member member2 = new Member();
            member2.setAge(15);
            member2.setUsername("member2");
            member2.setTeam(team);

            em.persist(member2);

            em.flush();
            em.clear();


            // 1. 상태 필드 -> 경로 탐색 끝. 추가 탐색 불가능
            /*
            String query = "select m.username from Member m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
             */

            // 2. 단일 값 연관 경로 -> 묵시적 내부 조인 발생, 탐색 가능 (m.team.name 등)
            /*
            String query = "select m.team from Member m";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .getResultList();
             */

            // 3. 컬렉션 값 연관 경로 -> 묵시적 내부 조인 발생, 탐색 불가능 (t.members.username 등)
            /*
            String query = "select t.members from Team t";
            Collection resultList = em.createQuery(query, Collection.class)
                    .getResultList();
             */

            // 4. 명시적 조인을 통한 컬렉션 값 연관 경로
            // 명시적 조인을 하면 컬렉션 값에도 별칭을 부여할 수 있다. 별칭을 통해 객체 경로 탐색을 하여 원하는 값을 가져올 수 있다.
            String query = "select m.username from Team t join t.members m";
            List<String> resultList = em.createQuery(query, String.class)
                    .getResultList();
            // 결과 -> member1, member2

            // 묵시적 조인 X 실무에서 애로사항이 많이 밣생하기때문 -> 명시적 조인을 사용하자
            // 묵시적 조인은 조인이 일어나는 상황을 한번에 파악하기 어렵다.
            // 묵시적 조인은 오로지 내부 조인. 외부 조인 불가능


            tx.commit();
        } catch (Exception e) {
            System.out.println("=======error========");
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        entityManagerFactory.close();
    }
}

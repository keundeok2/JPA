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

            /*
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
             */


            /*
            // 한계 1. fetch join의 대상에는 가급적 별칭을 사용하지 않는다.
            // fetch join 대상에 별칭을 부여하고 조건을 거는 것은 되도록 사용하지 않는다.
            // fetch join은 모든 연관 데이터를 가져오는 것을 목적으로 하기 때문이다.
            //  -> 데이터의 정합성 이슈 (team을 두 번 조회 하는데, 하나에는 member에 조건을 건 경우와 다른 하나에는 조건을 걸지 않고 모두 가져온 경우에 team의 데이터가 일치하지 않는다.)
            // 별칭은 fetch join 대상에 fetch join을 하는 경우에 사용하기도 한다.

            // 사용하지 말 것. 실행은 됨.
            String query = "select t from Team t join fetch t.members m where m.username = 'member1'";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .getResultList();

            for (Team team : resultList) {
                System.out.println("team = " + team.getMembers());
            }
             */

            // 한계 2. 둘 이상의 컬렉션은 페치 조인할 수 없다.
            // -> 페치 조인의 대상 컬렉션은 하나만 지정할 수 있다.


            /*
            // 한계 3. 컬렉션을 페치 조인하면 페이징 API를 사용할 수 없다.
            //  일대일, 다대일 같은 단일 값 연관 필드들은 페치 조인을 해도 페이징 API 사용 가능

            String query = "select t from Team t join fetch t.members";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(1)
                    .getResultList();

            // WARN: HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
            // 실행되는 쿼리에서는 페이징에 관한 내용이 포함되어있지 않다.
            // 결국 모든 데이터를 가져온 것
             */

            // 해결 방안
            // fetch join을 사용하지 않고 batchSize를 통해 N+1 문제를 해결하기
            // batchSize -> IN 키워드를 사용해 설정한 사이즈만큼 사용한다. 글로벌 설정하거나 엔터티에 애너테이션으로 설정 (Team.Members)

            String query = "select t from Team t";
            List<Team> resultList = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            System.out.println("resultList.size() = " + resultList.size());

            for (Team team : resultList) {
                System.out.println("team.getName() = " + team.getName());
                for (Member member : team.getMembers()) {
                    System.out.println("--> member = " + member);
                }
            }

            // 결과
            // Member를 가져올 떄 SELECT 쿼리를 호출한다.
            // 이 때 members0_.TEAM_ID in ( ?, ? )을 사용하여 데이터를 가져와서 N+1 문제를 최소화 할 수 있다.



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

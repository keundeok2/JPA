package jpql;

import jpql.domain.Member;
import jpql.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main3 {
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

            /*
            {
                // 엔터티 직접 사용
                String query = "select m from Member m where m = :member";
                List<Member> resultList = em.createQuery(query, Member.class)
                        .setParameter("member", member1)
                        .getResultList();

                // 결과 -> 실행된 SQL의 where 조건 [where member0_.id=?]
                // 엔터티를 넘겨도 식별자 조건으로 SQL이 생성된다.

            }
             */

            /*
            {
                // Named Query
                // NamedQuery를 등록하여 사용할 수 있다. (엔터티, XML 등에 정의)
                // 어플리케이션 실행 시 NamedQuery를 검증하여 구문 오류 등을 찾아준다.
                List<Member> resultList = em.createNamedQuery("Member.findByUsername", Member.class)
                        .setParameter("username", "member1")
                        .getResultList();

                for (Member member : resultList) {
                    System.out.println("member = " + member);
                }
            }
             */

            {
                String query = "update Member m set m.age = :age";
                int resultCount = em.createQuery(query)
                        .setParameter("age", 20)
                        .executeUpdate();

                System.out.println("resultCount = " + resultCount); // 2
                System.out.println("member1: " + member1.getAge()); // age=10
                System.out.println("member2: " + member2.getAge()); // age=15
                // 결과 -> 벌크 연산은 영속선 컨텍스트를 무시하고 바로 DB에 적용된다.
                // 따라서 영속성 컨텍스트에 존재하는 member1, member2의 나이는 업데이트 되지 않았다.
                // 이를 해결하려면 영속성 컨텍스트를 초기화하고 다시 데이터를 조회해야한다.

                em.clear();
                Member findMember = em.find(Member.class, member1.getId());
                System.out.println("findMember = " + findMember); // age = 20

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

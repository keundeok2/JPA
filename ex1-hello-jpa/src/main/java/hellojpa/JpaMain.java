package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        // EntityManagerFactory는 단 하나만 생성
        // transaction 단위마다 EntityManager 생성, 사용. EntityManager는 DB Connection이라고 생각하면 됨

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // INERT
            /*
            Member member = new Member();
            member.setId(2L);
            member.setName("kdkimB");

            em.persist(member);

            tx.commit();
            */

            // SELECT
            /*
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            tx.commit();
            */
            // DELETE
            /*
            Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);
            tx.commit();
            */

            // UPDATE

            // JPA로 가져온 객체는 JPA 관리하에 있다.
            // 트랜잭션을 커밋할 때 객체의 상태를 체크하고 변경사항이 있으면 UPDATE SQL을 날린다
            /*
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
            tx.commit();
            */

            // 테이블을 대상으로 쿼리를 만들지 않고 객체를 대상을 쿼리를 만든다
            /*
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();
            result.forEach(m -> System.out.println("m.name = " + m.getName()));
            tx.commit();
             */


            /*
            // [영속성 컨텍스트]
            // 비영속 상태
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

            // 영속
            System.out.println("=== BEFORE ===");
            em.persist(member);
            System.out.println("=== AFTER ===");

            Member findMember = em.find(Member.class, 101L);

            System.out.println("findMember.getId() = " + findMember.getId());
            System.out.println("findMember.getName() = " + findMember.getName());

            tx.commit();

            // 결과 -> 쿼리가 날아가는 시점은 persist()이 아니라 트랜잭션을 커밋할 때
            // em.find()에서 쿼리가 날아가지 않음 -> DB가 아니라 1차 캐시에서 데이터를 가져온 것
             */

            Member findMember1 = em.find(Member.class, 100L);
            Member findMember2 = em.find(Member.class, 100L);

            // 결과 -> 쿼리는 한번만 날아감
            // 첫 번째 find 에서는 DB에서 데이터를 갖고와서 1차 캐시에 저장
            // 두 번째 find 에서는 1차 캐시에서 데이터를 가져옴


        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

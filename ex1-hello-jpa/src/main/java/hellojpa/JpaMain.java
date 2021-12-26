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

            /*
            Member findMember1 = em.find(Member.class, 100L);
            Member findMember2 = em.find(Member.class, 100L);
            // 결과 -> 쿼리는 한번만 날아감 (findMember1을 가져올 때)
            // 첫 번째 find 에서는 DB에서 데이터를 갖고와서 1차 캐시에 저장
            // 두 번째 find g에서는 1차 캐시에서 데이터를 가져옴
            // findMember1 == findMember2 -> true!

            tx.commit();
             */


            /*
            em.persist(memberA);
            em.persist(memberB);
            // 여기까지는 Insert SQL을 DB에 보내지 않는다.

            // 커밋하는 순간 SQL을 DB에 보낸다
            tx.commit();
             */

            /*
            // update
            Member findMember = em.find(Member.class, 101L);
            findMember.setName("ZZZZ");

            // commit() 에서 쿼리가 날아감
            // 1차 캐시 안에 entity snapshot이 있는데 커밋 직전에 엔터티와 스냅샷을 비교하여
            // 변경사항이 있으면 update 쿼리를 날린다.
            // dirty checking

            tx.commit();
             */

            /*
            // flush

            Member member = new Member(200L, "hello");
            em.persist(member);
            em.flush(); // 직접 호출

            System.out.println("========================");
            // ======== 전에 쿼리가 날아감 (커밋 전에 쿼리가 날아감)

            tx.commit();
             */

            /*
            // detach
            Member member = em.find(Member.class, 100L);
            member.setName("ZZZZZZ");

            em.detach(member);
//            em.clear(); 영속성 컨텍스트 완전히 초기화
            System.out.println("========================");

            tx.commit();
            // 결과 -> Select 쿼리만 날아가고 update 쿼리는 날아가지 않음
            // member를 영속성 컨텍스트에서 분리하였기 때문에 (준영속 상태)
            // 영속성 컨텍스트가 제공하는 기능을 사용할 수 없음 (dirty checking 하여 update query 전송)
             */

            /*
            // detach
            Member member = em.find(Member.class, 100L);
            em.clear();
            Member member2 = em.find(Member.class, 100L);

            System.out.println("========================");

            tx.commit();

            // 결과 -> === 이전에 select query가 두 번 실행된다.
            // em.claer()로 영속성 컨텍스트 내의 데이터를 모두 detach 하였기 때문임

             */



        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

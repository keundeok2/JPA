package proxy;

import org.hibernate.Hibernate;
import proxy.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            /*
            {
                Member member = new Member();
                member.setUsername("hello");

                em.persist(member);

                em.flush();
                em.clear();

                // getReference를 하는 시점에는 쿼리가 실행되지 않는다.
                // Member 객체가 아닌 가짜 객체 (프록시) 를 리턴한다.
                // 껍데기는 호출한 클래스와 같지만 속은 비어있음. 내부에 target entity 필드가 진짜 레퍼런스를 가리킨다.
                Member findMember = em.getReference(Member.class, member.getId());

                // class proxy.domain.Member$HibernateProxy$cLGlO2a4  프록시 클래스
                System.out.println("before findMember = " + findMember.getClass());

                // ID는 파라미터로 넘긴 값이어서 DB에 접근하지 않아도 알 수 있다. 쿼리를 날리지 않는다.
                System.out.println("findMember.getId() = " + findMember.getId());

                // DB에 접근해서 데이터를 가져와야 할 때 쿼리를 날린다.
                // 프록시 내부의 target을 초기화 한다.
                System.out.println("findMember.getUsername(); = " + findMember.getUsername());

                // 프록시 내부의 target이 초기화 되었기 때문에 쿼리를 날리지 않는다.
                System.out.println("findMember.getUsername(); = " + findMember.getUsername());

                // target이 초기화 되었다고해서 프록시 객체가 엔터티 객체가 되는 것은 아니다.
                // 데이터를 가져올 때에는 실제 DB에서 조회된 엔터티에서 데이터를 가져오는 것 뿐이다.
                // 엔터티 객체의 타입을 비교할 때에 == 대신 instanceof 를 사용할 것
                System.out.println("after findMember = " + findMember.getClass());
            }

             */

            {
                Member member = new Member();
                member.setUsername("hello");

                em.persist(member);

                em.flush();
                em.clear();

                /*
                Member m1 = em.find(Member.class,  member.getId());
                System.out.println("m1.getClass() = " + m1.getClass()); // Member

                // 영속성 컨텍스트에 데이터가 존재한다면 프록시 대신 엔터티 객체를 반환한다.
                Member m2 = em.getReference(Member.class, member.getId());
                System.out.println("m2.getClass() = " + m2.getClass()); // Member

                System.out.println("a == a: " + (m1 == m2)); // true 인스턴스까지 완벽히 동일하다
                 */

                /*
                Member m1 = em.getReference(Member.class,  member.getId());
                System.out.println("m1.getClass() = " + m1.getClass()); // Proxy

                Member m2 = em.getReference(Member.class, member.getId());
                System.out.println("m2.getClass() = " + m2.getClass()); // Proxy

                System.out.println("a == a: " + (m1 == m2)); // true 인스턴스까지 완벽히 동일하다
                */

                /*
                Member m1 = em.getReference(Member.class,  member.getId());
                System.out.println("m1.getClass() = " + m1.getClass()); // Proxy

                Member m2 = em.find(Member.class, member.getId());
                System.out.println("m2.getClass() = " + m2.getClass()); // Proxy (find를 해도 proxy가 나올 수 있다.)

                System.out.println("a == a: " + (m1 == m2)); // true 인스턴스까지 완벽히 동일하다
                 */

            }

            /*
            {
                // 엔터티가 준영속 상태일 때 프록시를 초기화하면 예외가 발생한다

                Member member = new Member();
                member.setUsername("hello");

                em.persist(member);

                em.flush();
                em.clear();

                Member refMember = em.getReference(Member.class, member.getId());

                em.detach(refMember);
//                em.close();
//                em.clear();

                refMember.getUsername();

            }
             */

            {
                // 프록시 상태 확인 및 제어
                Member member = new Member();
                member.setUsername("hello");

                em.persist(member);

                em.flush();
                em.clear();

                Member m1 = em.getReference(Member.class, member.getId());
                // 프록시 인스턴스 초기화 여부 확인
                System.out.println("isLoaded: "+entityManagerFactory.getPersistenceUnitUtil().isLoaded(m1));;

                // 프록시 클래스 확인
                System.out.println("proxy: " + m1.getClass());

                // 프록시 강제 초기화 (Insert 쿼리 호출)
                Hibernate.initialize(m1);

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

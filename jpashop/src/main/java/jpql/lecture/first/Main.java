package jpql.lecture.first;


import jpql.lecture.first.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            {
                em.createQuery(
                        "select m From Member m where m.username like '%kim%'",
                        Member.class
                ).getResultList();
            }

            {
                // criteria 사용
                // 장점
                // 동적쿼리를 만들기 더 좋다
                // 자바코드로 sql을 만들기 때문에 컴파일에서 오타 등 오류를 잡을 수 있음

                // 단점
                // 어렵다. sql과는 거리가 멀어서 알아보기 힘들다. 유지보수가 어렵다.

                CriteriaBuilder cb = em.getCriteriaBuilder();
                CriteriaQuery<Member> query = cb.createQuery(Member.class);

                Root<Member> m = query.from(Member.class);

                CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("username"), "kim"));

                List<Member> resultList = em.createQuery(cq).getResultList();

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

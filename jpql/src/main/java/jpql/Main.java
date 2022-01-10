package jpql;

import jpql.domain.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            {
                /*
                Member member = new Member();
                member.setAge(10);
                member.setUsername("hello");
                em.persist(member);

                TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            Query query2 = em.createQuery("select m.username, m.age from Member m");

                // List로 반환
                List<Member> resultList = query1.getResultList();
                for (Member member1 : resultList) {
                    System.out.println("member1 = " + member1);
                }

                // 객체 하나만 반환
                Member findMember = query1.getSingleResult();

                // getResultList() -> 결과가 없으면 빈 List 반환
                // getSingleResult() -> 결과가 없거나 2개 이상이면 Exception
                 */
            }

            {
                // query parameter
                Member member = new Member();
                member.setAge(10);
                member.setUsername("hello");
                em.persist(member);

                Member singleResult = em.createQuery("select m from Member m where m.username = :username", Member.class)
                        .setParameter("username", "hello")
                        .getSingleResult();

                System.out.println(singleResult);

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

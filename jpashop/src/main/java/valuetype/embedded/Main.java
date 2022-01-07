package valuetype.embedded;

import fetch.domain.Team;
import valuetype.embedded.domain.Address;
import valuetype.embedded.domain.Member;
import valuetype.embedded.domain.Period;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("hello");
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{

            Member member = new Member();
            member.setUsername("user");
            member.setAddress(new Address("city", "street", "1000"));
            member.setPeriod(new Period(LocalDateTime.of(2022,1,1,0, 0), LocalDateTime.of(2022,1,3,0,0)));

            em.persist(member);


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

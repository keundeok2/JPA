package advanced.practice;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try {

            tx.commit();
        } catch (Exception e) {
            System.out.println("=========== error ==========");
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

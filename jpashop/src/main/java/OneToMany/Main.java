package OneToMany;

import OneToMany.domain.Member;
import OneToMany.domain.Team;

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

            Member member = new Member();
            member.setUsername("helloA");
            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member); // Member의 TEAM_ID 업데이트
            em.persist(team);

            tx.commit();
        } catch (Exception e) {

        } finally {
            em.close();
        }

        emf.close();

    }
}

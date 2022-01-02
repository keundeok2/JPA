package advanced.inheritance;

import advanced.inheritance.domain.Movie;

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

            Movie movie = new Movie();
            movie.setActor("김근덕");
            movie.setDirector("이용진");
            movie.setName("제이피에이 정복기");
            movie.setPrice(10000);

            em.persist(movie);

            em.flush();
            em.clear();


            Movie findMovie = em.find(Movie.class, movie.getId());
            System.out.println("findMovie.getId() = " + findMovie.getId());
            System.out.println("findMovie.getId() = " + findMovie.getName());
            System.out.println("findMovie.getId() = " + findMovie.getPrice());
            System.out.println("findMovie.getId() = " + findMovie.getActor());
            System.out.println("findMovie.getId() = " + findMovie.getDirector());

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

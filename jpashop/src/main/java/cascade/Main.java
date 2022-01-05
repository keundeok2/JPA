package cascade;

import cascade.domain.Child;
import cascade.domain.Parent;

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
            Child child1 = new Child();
            Child child2 = new Child();

            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            // cascade 속성으로 parent 내부의 child 까지 영속화된다.
            // parent와 child의 라이프사이클이 거의 같을 때 사용한다.
            // parent가 child의 단일 소유자일 때 사용 (child를 소유하고 있는 엔터티가 여러개일 때 사용 X)
            em.persist(parent);

            em.flush();

            System.out.println("===========");
            Parent findParent = em.find(Parent.class, parent.getId());

            // CASCADE.ALL, orpahnRemoval=true ::  parent와 child 모두 delete
            // CASCADE.PERSIST, orpahnRemoval=true ::  parent와 child 모두 delete
            // CASCADE.PERSIST, orpahnRemoval=false ::  parent만 delete BUT FK제약조건으로 삭제 불가 Exception 발생
//            em.remove(parent);

            // childList에 orphanRemoval = true 이기 때문에
            // 부모 엔터티와 자식 엔터티의 관계가 끊어지면 delete SQL이 호출된다.
//            findParent.getChildList().remove(0);

            // CASCADE.ALL, orphanRemoval 속성 없음 :: parent와 child 모두 삭제
            // REMOVE가 자식 엔터티까지 전파됨
//            em.remove(findParent);

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

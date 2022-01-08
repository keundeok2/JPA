package valuetype.collection;

import valuetype.collection.domain.Address;
import valuetype.collection.domain.AddressEntity;
import valuetype.collection.domain.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Set;

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
                member.setHomeAddress(new Address("homeCity", "homeStreet", "100100"));
                member.getAddressHistory().add(new Address("old1", "oldstreet1", "100111"));
                member.getAddressHistory().add(new Address("old2", "oldstreet2", "100111"));
                member.getFavoriteFoods().add("치킨");
                member.getFavoriteFoods().add("피자");
                member.getFavoriteFoods().add("마라탕");

                em.persist(member);

                em.flush();
                em.clear();

                System.out.println("================");
                Member findMember = em.find(Member.class, member.getId());
                // collection들은 지연로딩 된다.
                List<Address> addressHistory = findMember.getAddressHistory();
                for (Address address : addressHistory) {
                    System.out.println("address.getCity() = " + address.getCity());
                }

                Set<String> favoriteFoods = findMember.getFavoriteFoods();
                for (String favoriteFood : favoriteFoods) {
                    System.out.println("favoriteFood = " + favoriteFood);
                }

                // 값 타입 컬레션 UPDATE
                // homeAddress UPDATE
                // 값 타입의 value를 바로 바꾸는 것은 위험함. instance를 생성해서 완전히 갈이 끼워야 함
//                findMember.getHomeAddress().setCity("newCity");
                Address a = findMember.getHomeAddress();
                findMember.setHomeAddress(new Address("newCity", a.getStreet(), a.getZipcode()));

                // favorite_foods: 치킨 -> 한식
                findMember.getFavoriteFoods().remove("치킨");
                findMember.getFavoriteFoods().add("한식");

                // addressHistory: old1 -> newCity1
                // 내부의 값이 같은 인스턴스로 삭제할 row를 찾는다.
                findMember.getAddressHistory().remove(new Address("old1", "oldstreet1", "100111"));
                findMember.getAddressHistory().add(new Address("newCity1", "newstreet1", "100111"));
                // 결과 -> 모든 row를 delete하고 남은 값을 다시 insert한다.
                // --> 사이드 이펙트가 발생할 수 있다.
                // 값 타입 컬렉션 대신 일대다 맵핑을 사용하는 것이 관리하기 더 좋다.

            }
             */

            {
                // 값타입 컬렉션 대신 일대다 맵핑을 사용하는 방법

                Member member = new Member();
                member.setUsername("hello");
                member.setHomeAddress(new Address("homeCity", "homeStreet", "100100"));
                member.getAddressHistory().add(new AddressEntity("old1", "oldstreet1", "100111"));
                member.getAddressHistory().add(new AddressEntity("old2", "oldstreet2", "100111"));
                member.getFavoriteFoods().add("치킨");
                member.getFavoriteFoods().add("피자");
                member.getFavoriteFoods().add("마라탕");

                em.persist(member);

                em.flush();
                em.clear();

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

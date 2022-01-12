package jpql;

import jpql.domain.*;

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

            /**
             * JPQL 기본 API
             */
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

            /*
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
             */

            /**
             * 프로젝션
             */
            /*
            {
                // 엔터티 프로젝션
                Member member = new Member();
                member.setAge(10);
                member.setUsername("hello");
                em.persist(member);

                List<Member> resultList = em.createQuery("select m from Member m", Member.class)
                        .getResultList();

                Member findMember = resultList.get(0);
                findMember.setAge(20);

                // 결과: 엔터티 프로젝션으로 조회한 엔터티는 영속성 컨텍스트에 의해 관리된다.

            }

            {
                // 엔터티 프로젝션
                // JOIN으로 team 엔터티를 조회한다.
                List<Team> resultList = em.createQuery("select m.team from Member m", Team.class)
                        .getResultList();

            }

            {
                // 임베디드 프로젝션
                em.createQuery("select o.address from Order o", Address.class)
                        .getResultList();

            }

            {
                // 스칼라 타입 프로젝션
                em.createQuery("select distinct m.username, m.age from Member m").getResultList();
            }

            {
                // 스칼라 타입 여러 값 가져오기
                // 첫번째
                List resultList = em.createQuery("select distinct m.username, m.age from Member m").getResultList();
                Object o = resultList.get(0);
                Object[] result = (Object[]) o;
                System.out.println("result[0] = " + result[0]);
                System.out.println("result[1] = " + result[1]);

                // 두번째
                List<Object[]> resultList1 = em.createQuery("select distinct m.username, m.age from Member m").getResultList();
                Object[] objects = resultList1.get(0);
                System.out.println("objects = " + objects[0]);
                System.out.println("objects = " + objects[1]);

                // 세번째
                List<MemberDto> memberDtos = em.createQuery("select new jpql.domain.MemberDto(m.username, m.age) from Member m", MemberDto.class).getResultList();
                MemberDto memberDto = memberDtos.get(0);
                System.out.println("memberDto.getUsername() = " + memberDto.getUsername());
                System.out.println("memberDto.getAge() = " + memberDto.getAge());

            }

             */

            /**
             * 페이징
             */

            /*
            {
                for (int i = 0; i < 100; i++) {
                    Member member = new Member();
                    member.setAge(i);
                    member.setUsername("hello" + i);
                    em.persist(member);
                }
                em.flush();
                em.clear();

                List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                        .setFirstResult(1)
                        .setMaxResults(10)
                        .getResultList();

                System.out.println("result.size() = " + result.size());
                for (Member member1 : result) {
                    System.out.println("member1 = " + member1);
                }
            }
             */

            /**
             * 조인
             */
            /*
            {
                Team team = new Team();
                team.setName("teamA");
                em.persist(team);

                Member member = new Member();
                member.setAge(10);
                member.setUsername("member1");
                member.setTeam(team);

                em.persist(member);

                em.flush();
                em.clear();

                // inner join
                List<Member> result = em.createQuery("select m from Member m inner join m.team t where t.name =: teamName", Member.class)
                        .setParameter("teamName", "teamA")
                        .getResultList();

                // outer join
                List<Member> result1 = em.createQuery("select m from Member m left outer join m.team t", Member.class)
                        .getResultList();


                // theta join
                List<Member> result2 = em.createQuery("select m from Member m, Team t where m.username = t.name", Member.class)
                        .getResultList();

                // ON 절 사용할 수 있다.
                List<Member> result3 = em.createQuery("select m from Member m left join m.team t on t.name =: teamName", Member.class)
                        .setParameter("teamName", "teamA")
                        .getResultList();

                // 연관관계가 없는 엔터티 외부 JOIN, ON 절 사용
                List<Member> result4 = em.createQuery("select m from Member m left outer join Team t on m.username = t.name", Member.class)
                        .getResultList();


            }

             */

            /**
             * JPQL 타입 표현과 기타식
             */
            /*
            {
                Team team = new Team();
                team.setName("teamA");
                em.persist(team);

                Member member = new Member();
                member.setAge(10);
                member.setUsername("member1");
                member.setType(MemberType.ADMIN);
                member.setTeam(team);

                em.persist(member);

                em.flush();
                em.clear();

                // Enum 타입을 사용할 때에는 패키지명을 포함한 이름을 사용해야한다.
//                String query = "select m.username, 'HELLO', true from Member m " +
//                                "where m.type = jpql.domain.MemberType.USER";
//                List<Object[]> result = em.createQuery(query).getResultList();

                // parameter를 사용하면 간단하게 사용할 수 있음
                String query = "select m.username, 'HELLO', true from Member m " +
                                "where m.type = :userType";
                List<Object[]> result = em.createQuery(query)
                        .setParameter("userType", MemberType.ADMIN)
                        .getResultList();

                for (Object[] objects : result) {
                    System.out.println("objects[0] = " + objects[0]);
                    System.out.println("objects[1] = " + objects[1]);
                    System.out.println("objects[2] = " + objects[2]);
                }

            }

             */

            /**
             * 조건식
             */
            {
                Team team = new Team();
                team.setName("teamA");
                em.persist(team);

                Member member = new Member();
                member.setAge(10);
                member.setUsername("member1");
                member.setType(MemberType.ADMIN);
                member.setTeam(team);

                em.persist(member);

                em.flush();
                em.clear();

                // case문
                String query =
                        "select " +
                                "case when m.age <= 10 then '학생요금' " +
                                "     when m.age >= 60 then '경로요금' " +
                                "else '일반요금' " +
                                "end " +
                        "from Member m"
                        ;
                List<String> result = em.createQuery(query, String.class).getResultList();
                for (String s : result) {
                    System.out.println("s = " + s);
                }


                // coalesce(a, b) a가 null이면 b 반환, null 아니면 a 반환
                String query2 = "select coalesce(m.username, '이름 없는 회원') from Member m ";
                List<String> result2 = em.createQuery(query2, String.class).getResultList();
                for (String s : result2) {
                    System.out.println("s = " + s);
                }

                // nullif(a, b) -> a와 b가 같으면 null 반환 아니면 a 반환
                String query3 = "select nullif(m.username, 'member1') from Member m ";
                List<String> result3 = em.createQuery(query3, String.class).getResultList();
                for (String s : result3) {
                    System.out.println("s = " + s);
                }


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

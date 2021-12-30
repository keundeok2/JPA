package memberteam;

import memberteam.domain.Member;
import memberteam.domain.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            /*
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("Member1");
            member.setTeamId(team.getId());
            em.persist(member);

            Member findMember = em.find(Member.class, 1);
            Team findTeam = em.find(Team.class, findMember.getId());
             */

            // 단방향 연관 관계
            /*
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("Member1");
            member.setTeam(team);
            em.persist(member);

            Member findMember = em.find(Member.class, member.getId());
            Team findTeam = findMember.getTeam();
            System.out.println("findTeam.getName() = " + findTeam.getName());

            // ....
            // member의 team 업데이트
            Team newTeam = em.find(Team.class, 100L);
            member.setTeam(newTeam);
             */

            /*
            // 양방향 연관관계
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("Member1");
            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());
            List<Member> members = findMember.getTeam().getMembers();
            for (Member m : members) {
                System.out.println("m.getUsername() = " + m.getId());
            }
             */

            // 양방향 맵핑 시 실수하는 부분
            /*
            Member member = new Member();
            member.setUsername("Member1");
            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            team.getMembers().add(member); // 주인이 아닌 엔터티에서 외래키를 관리하려 함
            em.persist(team);

            em.flush();
            em.clear();

            // 결과 -> Member의 외래키로 team_id = null이 설정됨
             */


            Member member = new Member();
            member.setUsername("Member1");
            em.persist(member);

            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            // 두 객체에 모두 값을 셋팅
            member.setTeam(team);
            member.changeTeam(team);
//            team.getMembers().add(member); // Member.changeTeam()을 할 때 자신을 team.members에 추가하므로 제거가능

            
//            em.flush();
//            em.clear();

            List<Member> members = team.getMembers();
            System.out.println("==========");
            for (Member m : members) {
                System.out.println("m.getId() = " + m.getId());
            }
            System.out.println("==========");


            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }
}

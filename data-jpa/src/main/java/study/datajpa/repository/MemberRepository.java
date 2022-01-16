package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.entity.Member;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername") // NamedQuery name
    // @Query 애너테이션이 없어도 되는 이유
    // -> 관례상 JpaRepository의 엔터티 타입 + 메서드 이름으로 NamedQuery를 찾는다.
    // NamedQuery가 없으면 메서드 이름으로 쿼리를 생성한다.
    List<Member> findByUsername(@Param("username") String username);

}

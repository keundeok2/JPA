package study.datajpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

//    @Query(name = "Member.findByUsername") // NamedQuery name
    // @Query 애너테이션이 없어도 되는 이유
    // -> 관례상 JpaRepository의 엔터티 타입 + 메서드 이름으로 NamedQuery를 찾는다.
    // NamedQuery가 없으면 메서드 이름으로 쿼리를 생성한다.
    List<Member> findByUsername(@Param("username") String username);

    // NamedQuery와 같이 애플리케이션 로딩 시점에서 파싱, 검증하여 오류를 잡을 수 있음
    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new study.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") Collection<String> names);

    List<Member> findListByUsername(String username);
    Member findMemberByUsername(String username);
    Optional<Member> findOptionalByUsername(String username);

    // count 쿼리 분리
    @Query(value = "select m from Member m left join m.team t"
            ,countQuery = "select count(m.username) from Member m")
    Page<Member> findByAge(int age, Pageable pageable);

//    Slice<Member> findByAge(int age, Pageable pageable);
    // 반환타입을 List로 받을 수 있다. 원하는 갯수만 가져올 뿐 Pageable 기능은 사용하지 못한다.
//    List<Member> findByAge(int age, Pageable pageable);

    @Modifying(clearAutomatically = true) // executeUpdate() 실행
    @Query("update Member m set m.age = m.age + 1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);
}

package study.datajpa.repository;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // 트랜잭션 안에서 데이터 저장 등이 요청되어야한다.
@Rollback(false) // @Transactional 애너테이션이 트랜잭션을 롤백시켜버려서 쿼리가 실행되지않아서
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

//    @Test
    void findMember() {
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);

        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member); // findMember == member가 실행된 것 equals(), hashCode() ㄴㄴ
    }

//    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberJpaRepository.findById(member1.getId()).orElse(null);
        Member findMember2 = memberJpaRepository.findById(member2.getId()).orElse(null);
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        findMember1.setUsername("updateMemberName");

        // 리스트 검증
        List<Member> all = memberJpaRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);
        long deletedCount = memberJpaRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }


//    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member memberA = new Member("member", 10, null);
        Member memberB = new Member("member", 20, null);
        memberJpaRepository.save(memberA);
        memberJpaRepository.save(memberB);

        List<Member> result = memberJpaRepository.findByUsernameAndAgeGreaterThen("member", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("member");
        assertThat(result.get(0).getAge()).isGreaterThan(15);
        assertThat(result.size()).isEqualTo(1);
    }

//    @Test
    void namedQuery() {
        Member member1 = new Member("AAA");
        Member member2 = new Member("BBB");

        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> members = memberJpaRepository.findByUsername("AAA");

        assertThat(members.get(0).getUsername()).isEqualTo("AAA");

    }

//    @Test
    void paging() {
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 10, null));
        memberJpaRepository.save(new Member("member3", 10, null));
        memberJpaRepository.save(new Member("member4", 10, null));
        memberJpaRepository.save(new Member("member5", 10, null));

        int age = 10;
        int offset = 0;
        int limit = 3;

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        //then
        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    void bulkUpdate() {
        memberJpaRepository.save(new Member("member1", 10, null));
        memberJpaRepository.save(new Member("member2", 19, null));
        memberJpaRepository.save(new Member("member3", 20, null));
        memberJpaRepository.save(new Member("member4", 21, null));
        memberJpaRepository.save(new Member("member5", 40, null));

        //when
        int resultCount = memberJpaRepository.bulkAgePlus(20);

        //then
        assertThat(resultCount).isEqualTo(3);
    }

}
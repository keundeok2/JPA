package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

//    @Test
    void testMember() {
        System.out.println(memberRepository.getClass());
        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(savedMember.getId()).orElseThrow(() -> new RuntimeException());

        assertThat(findMember.getId()).isEqualTo(savedMember.getId());
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

//    @Test
    void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).orElse(null);
        Member findMember2 = memberRepository.findById(member2.getId()).orElse(null);
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);


        findMember1.setUsername("updateMemberName");

        // 리스트 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

//    @Test
    void findByUsernameAndAgeGreaterThen() {
        Member memberA = new Member("member", 10, null);
        Member memberB = new Member("member", 20, null);
        memberRepository.save(memberA);
        memberRepository.save(memberB);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("member", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("member");
        assertThat(result.get(0).getAge()).isGreaterThan(15);
        assertThat(result.size()).isEqualTo(1);
    }

//    @Test
    void namedQuery() {
        Member member1 = new Member("AAA");
        Member member2 = new Member("BBB");

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> members = memberRepository.findByUsername("AAA");

        assertThat(members.get(0).getUsername()).isEqualTo("AAA");
    }

//    @Test
    void query() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> results = memberRepository.findUser("AAA", 10);
        Member findMember = results.get(0);

        assertThat(findMember.getUsername()).isEqualTo("AAA");
        assertThat(findMember.getAge()).isEqualTo(10);
        assertThat(findMember).isEqualTo(member1);

    }


//    @Test
    void findUsernameList() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }

    }

//    @Test
    void findMemberDto() {

        Team team = new Team("teamA");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10, team);
        memberRepository.save(m1);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("memberDto = " + memberDto);
        }

    }

//    @Test
    void findByNames() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> list = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : list) {
            System.out.println("member = " + member);
        }

    }

    @Test
    void returnType() {
        Member member1 = new Member("AAA", 10, null);
        Member member2 = new Member("BBB", 20, null);

        memberRepository.save(member1);
        memberRepository.save(member2);

        // 컬렉션 조회
//        List<Member> aaa = memberRepository.findListByUsername("AAA");

        // 단건 조회
//        Member findMember = memberRepository.findMemberByUsername("AAA");
//        System.out.println("findMember = " + findMember);

        // Optional 조회
//        Optional<Member> aaa = memberRepository.findOptionalByUsername("AAA");
//        Member member = aaa.orElse(null);
//        System.out.println("member = " + member);


        List<Member> aaa = memberRepository.findListByUsername("adf");
        aaa.size(); // 0 빈 컬렉션을 반환한다. null 아님!

        Member findMember = memberRepository.findMemberByUsername("adf");
        // findMember == null  null을 반환한다.

        Optional<Member> opt = memberRepository.findOptionalByUsername("asdf");
        System.out.println("opt = " + opt); // Optional.empty

    }

    @Test
    void paging() {
        memberRepository.save(new Member("member1", 10, null));
        memberRepository.save(new Member("member2", 10, null));
        memberRepository.save(new Member("member3", 10, null));
        memberRepository.save(new Member("member4", 10, null));
        memberRepository.save(new Member("member5", 10, null));

        int age = 10;
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        // Page
        //when
        // 반환타입 Page: totalCount 쿼리 호출, Slice: 호출 X
        Page<Member> page = memberRepository.findByAge(age, pageRequest);

        // 엔터티 -> dto 변환
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));

        //then
        List<Member> content = page.getContent();
        long totalElements = page.getTotalElements(); // 5
        int totalPages = page.getTotalPages(); // 2
        for (Member member : content) {
            System.out.println("member = " + member);
        }

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getTotalElements()).isEqualTo(5);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();

        /*
        // Slice
        // totalCount 쿼리를 호출하지않고 갯수를 하나 더 조회한다.
        Slice<Member> page = memberRepository.findByAge(age, pageRequest);
        List<Member> content = page.getContent();


        assertThat(content.size()).isEqualTo(3);
//        assertThat(page.getTotalElements()).isEqualTo(5);
//        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst()).isTrue();
        assertThat(page.hasNext()).isTrue();
         */

    }

}
package study.datajpa.repository;

import org.springframework.beans.factory.annotation.Value;

public interface UsernameOnly {

    @Value("#{target.username + ' ' + target.age}") // OPEN PROJECTION 엔터티를 모두가져와서 프로젝션 생성
    String getUsername();
}

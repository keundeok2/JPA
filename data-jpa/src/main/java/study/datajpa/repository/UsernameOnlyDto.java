package study.datajpa.repository;

public class UsernameOnlyDto {

    private final String username;

    // 생성자로 받는 파라미터 이름으로 프로젝션
    public UsernameOnlyDto(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

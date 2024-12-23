package hello.fclover.repository;

import hello.fclover.domain.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface mongoTestRepository extends MongoRepository<Member, String> {
    Member findByMemberId(String memberId);
}

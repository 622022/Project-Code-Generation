package io.swagger.dao;

import io.swagger.model.content.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findUserByUsername(String username);
    @Query(nativeQuery = true, value = "SELECT * FROM User LIMIT :limit OFFSET :offset")
    Iterable<User> getallUsers(@Param("limit") int limit, @Param("offset") int offset);
}

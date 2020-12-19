package io.swagger.dao;

import io.swagger.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    User findUserByUsername(String username);
    Iterable<User> queryUserByOffSet(int limit);

    @Query(
            value = "SELECT * FROM user limit ?1",
            nativeQuery = true)
    Iterable<User> queryUserByLimit(int limit);

}

package io.swagger.dao;

import io.swagger.model.AccountObject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountObject, String> {

}

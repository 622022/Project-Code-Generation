package io.swagger.dao;

import io.swagger.model.AccountObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends CrudRepository<AccountObject, String> {
    public   Iterable<AccountObject> getAccountObjectByOwnerId(int ownerId);
    public   Iterable<AccountObject> getAccountObjectByType(AccountObject.TypeEnum type);
    public   Iterable<AccountObject> getAccountObjectByStatus(AccountObject.StatusEnum status);

}

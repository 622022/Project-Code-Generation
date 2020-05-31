package io.swagger.dao;

import io.swagger.model.ApiKey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends CrudRepository<ApiKey,String> {

    boolean findById(String principle);
}

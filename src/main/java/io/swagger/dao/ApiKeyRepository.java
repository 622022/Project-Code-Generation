package io.swagger.dao;

import io.swagger.model.ApiKey;
import io.swagger.model.InlineResponse2002;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiKeyRepository extends CrudRepository<InlineResponse2002, String> {

}

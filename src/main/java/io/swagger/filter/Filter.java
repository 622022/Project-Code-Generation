package io.swagger.filter;

import io.swagger.model.AccountObject;

public class Filter {
    public Integer limit;
    public Integer offset;
    public Integer accountOwnerId;
    public AccountObject.TypeEnum type;
    public AccountObject.StatusEnum status;


    public Filter(Integer limit, Integer offset, Integer accountOwnerId, String type, String status) {
        this.limit = limit==0?null:limit;
        this.offset = offset==0?null:offset;
        this.accountOwnerId = accountOwnerId==0?null:accountOwnerId;
        this.type =type==""?null: AccountObject.TypeEnum.valueOf(type.trim().toUpperCase()) ;
        this.status =status==""?null: AccountObject.StatusEnum.valueOf(status.trim().toUpperCase());
    }

    public Filter(Integer limit, Integer offset) {
        this.limit = limit==0?null:limit;
        this.offset = offset==0?null:offset;
    }
}

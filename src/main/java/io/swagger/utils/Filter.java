package io.swagger.utils;

import io.swagger.model.content.Account;

public class Filter {
    public Integer limit;
    public Integer offset;
    public Integer accountOwnerId;
    public Account.TypeEnum type;
    public Account.StatusEnum status;


    public Filter(Integer limit, Integer offset, Integer accountOwnerId, String type, String status) {
        try{
            this.limit = limit;
            this.offset = offset;
            this.accountOwnerId = accountOwnerId;
            this.type = type == null? null: Account.TypeEnum.valueOf(type.trim().toUpperCase()) ;
            this.status = status == null ? null : Account.StatusEnum.valueOf(status.trim().toUpperCase());
        }
        catch(Exception e){
            throw new IllegalArgumentException("Filters were not correctly passed");
        }
    }

    public Filter(Integer limit, Integer offset) {
        this.limit = limit==0?null:limit;
        this.offset = offset==0?null:offset;
    }
}

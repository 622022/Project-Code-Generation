package io.swagger.utils;

import io.swagger.model.content.Account;

public class Filter {
    public Integer limit;
    public Integer offset;
    public Integer accountOwnerId;
    public Account.TypeEnum type;
    public Account.StatusEnum status;


    public Filter(Integer limit, Integer offset, Integer accountOwnerId, String type, String status) {
        try {
            this.limit = limit;
            this.offset = offset;
            this.accountOwnerId = accountOwnerId;
            this.type = type == null || type == "" ? null : Account.TypeEnum.valueOf(type.trim().toUpperCase());
            this.status = status == null || status == "" ? null : Account.StatusEnum.valueOf(status.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Filters were not correctly passed");
        }
    }

    public int getType() {
        if (this.type == Account.TypeEnum.CHECKING) {
            return 0;
        } else if (this.type == Account.TypeEnum.SAVING) {
            return 1;
        }
        return 0;
    }

    public int getStatus() {
        if (this.status == Account.StatusEnum.ACTIVE) {
            return 0;
        } else if (this.status == Account.StatusEnum.CLOSED) {
            return 1;
        }
        return 0;
    }

    public Filter(Integer limit, Integer offset) {
        this.limit = limit == 0 ? null : limit;
        this.offset = offset == 0 ? null : offset;
    }

    public boolean OnlyLimit() {
        if (this.type == null && this.status == null && this.accountOwnerId == null) {
            return true;
        }
        return false;
    }

    public boolean OnlyAccountOwnerId() {
        if (this.accountOwnerId != null && this.type == null && this.status == null) {
            return true;
        }
        return false;
    }

    public boolean OnlyStatus() {
        if (this.accountOwnerId == null && this.type == null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean OnlyType() {
        if (this.accountOwnerId == null && this.type != null && this.status == null) {
            return true;
        }
        return false;
    }

    public boolean OwnerIdWStatus() {
        if (this.accountOwnerId != null && this.type == null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean OwnerIdWType() {
        if (this.accountOwnerId != null && this.type != null && this.status == null) {
            return true;
        }
        return false;
    }

    public boolean StatusWType() {
        if (this.accountOwnerId == null && this.type != null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean OwnerIdWStatusWType() {
        if (this.accountOwnerId != null && this.type != null && this.status != null) {
            return true;
        }
        return false;
    }

}

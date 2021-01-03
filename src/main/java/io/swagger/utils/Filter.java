package io.swagger.utils;

import io.swagger.model.content.Account;

public class Filter {
    public Integer limit;
    public Integer offset;
    public Integer accountOwnerId;
    public Account.TypeEnum type;
    public Account.StatusEnum status;

    public String iBan;
    public String receiverName;

    public Filter(Integer limit, Integer offset, Integer accountOwnerId, String type, String status) {
        try {
            this.limit = limit;
            this.offset = offset;
            this.accountOwnerId = accountOwnerId;
            this.type = type == null ? null : Account.TypeEnum.valueOf(type.trim().toUpperCase());
            this.status = status == null ? null : Account.StatusEnum.valueOf(status.trim().toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Filters were not correctly passed");
        }
    }

    public Filter(String iBan, Integer limit, Integer offset, String receiverName) {
        this.iBan = iBan;
        this.limit = limit;
        this.offset = offset;
        this.receiverName = receiverName;
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
        this.limit = limit;
        this.offset = offset;
    }

    public boolean onlyLimit() {
        if (this.type == null && this.status == null && this.accountOwnerId == null) {
            return true;
        }
        return false;
    }

    public boolean onlyAccountOwnerId() {
        if (this.accountOwnerId != null && this.type == null && this.status == null) {
            return true;
        }
        return false;
    }

    public boolean onlyStatus() {
        if (this.accountOwnerId == null && this.type == null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean onlyType() {
        if (this.accountOwnerId == null && this.type != null && this.status == null) {
            return true;
        }
        return false;
    }

    public boolean ownerIdWStatus() {
        if (this.accountOwnerId != null && this.type == null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean ownerIdWType() {
        if (this.accountOwnerId != null && this.type != null && this.status == null) {
            return true;
        }
        return false;
    }

    public boolean statusWType() {
        if (this.accountOwnerId == null && this.type != null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean ownerIdWStatusWType() {
        if (this.accountOwnerId != null && this.type != null && this.status != null) {
            return true;
        }
        return false;
    }

    public boolean senderOrReceiver() {
        if (this.iBan != null) {
            return true;
        }
        return false;
    }

    public boolean receiverName() {
        if (this.iBan != null && this.receiverName != null) {
            return true;
        }
        return false;
    }
}

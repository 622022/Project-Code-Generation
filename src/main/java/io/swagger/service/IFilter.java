package io.swagger.service;

import java.util.List;

public interface IFilter {

    public void filterOffset(Integer offset);
    public void filterLimit(Integer limit);
    public void filterUser(Integer id);
    public void filterType(String type );
    public void filterStatus(String status);
    public void filterReceiver(String receiverName);
    public void filterIBAN(String IBAN);



}

package com.aitho.contocorrente.service;

import java.util.List;

public interface CustomerService {

    List<Long> getBankAccountList(Long customerId);
}

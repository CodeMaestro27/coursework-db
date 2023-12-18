package com.example.db.service;

import com.example.db.domain.Balance;
import com.example.db.model.BalanceDTO;
import com.example.db.repos.BalanceRepository;
import com.example.db.domain.Operation;
import com.example.db.repos.OperationRepository;
import com.example.db.util.NotFoundException;
import com.example.db.util.WebUtils;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final OperationRepository operationRepository;

    public BalanceService(final BalanceRepository balanceRepository,
            final OperationRepository operationRepository) {
        this.balanceRepository = balanceRepository;
        this.operationRepository = operationRepository;
    }

    public List<BalanceDTO> findAll() {
        final List<Balance> balances = balanceRepository.findAll(Sort.by("id"));
        return balances.stream()
                .map(balance -> mapToDTO(balance, new BalanceDTO()))
                .toList();
    }

    public BalanceDTO get(final Integer id) {
        return balanceRepository.findById(id)
                .map(balance -> mapToDTO(balance, new BalanceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final BalanceDTO balanceDTO) {
        final Balance balance = new Balance();
        mapToEntity(balanceDTO, balance);
        return balanceRepository.save(balance).getId();
    }

    public void update(final Integer id, final BalanceDTO balanceDTO) {
        final Balance balance = balanceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(balanceDTO, balance);
        balanceRepository.save(balance);
    }

    public void delete(final Integer id) {
        balanceRepository.deleteById(id);
    }

    private BalanceDTO mapToDTO(final Balance balance, final BalanceDTO balanceDTO) {
        balanceDTO.setId(balance.getId());
        balanceDTO.setCreateDate(balance.getCreateDate());
        balanceDTO.setDebit(balance.getDebit());
        balanceDTO.setCredit(balance.getCredit());
        balanceDTO.setAmount(balance.getAmount());
        return balanceDTO;
    }

    private Balance mapToEntity(final BalanceDTO balanceDTO, final Balance balance) {
        balance.setCreateDate(balanceDTO.getCreateDate());
        balance.setDebit(balanceDTO.getDebit());
        balance.setCredit(balanceDTO.getCredit());
        balance.setAmount(balanceDTO.getAmount());
        return balance;
    }

    public String getReferencedWarning(final Integer id) {
        final Balance balance = balanceRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Operation balanceOperation = operationRepository.findFirstByBalance(balance);
        if (balanceOperation != null) {
            return WebUtils.getMessage("balance.operation.balance.referenced", balanceOperation.getId());
        }
        return null;
    }

}

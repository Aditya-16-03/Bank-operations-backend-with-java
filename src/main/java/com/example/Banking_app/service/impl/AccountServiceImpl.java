package com.example.Banking_app.service.impl;

import com.example.Banking_app.dto.AccountDto;
import com.example.Banking_app.entity.Account;
import com.example.Banking_app.mapper.AccountMapper;
import com.example.Banking_app.repository.AccountRepository;
import com.example.Banking_app.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount= accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("account doesnot exist"));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto deposit(Long id, double amount) {
        Account acc = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("account doesnot exist"));
        double total=acc.getBalance()+amount;
        acc.setBalance(total);
        Account account=accountRepository.save(acc);
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    public AccountDto withdraw(Long id, double amount) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("account doesnot exist"));
        if(amount>account.getBalance()){
            throw new RuntimeException("inefficient bank balance");
        }
        account.setBalance(account.getBalance()-amount);
        Account acc=accountRepository.save(account);
        return AccountMapper.mapToAccountDto(acc);
    }

    @Override
    public List<AccountDto> getAllAccounts() {
        List<Account> accs =accountRepository.findAll();
        return accs.stream().map((account)-> AccountMapper.mapToAccountDto(account)).
                collect(Collectors.toList());
    }

    @Override
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("account does not exist"));
        accountRepository.deleteById(id);
    }
}

package uz.momoit.lms.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.momoit.lms.domain.Accounts;
import uz.momoit.lms.repository.AccountsRepository;
import uz.momoit.lms.service.AccountsService;
import uz.momoit.lms.service.dto.AccountsDTO;
import uz.momoit.lms.service.mapper.AccountsMapper;

/**
 * Service Implementation for managing {@link uz.momoit.lms.domain.Accounts}.
 */
@Service
@Transactional
public class AccountsServiceImpl implements AccountsService {

    private final Logger log = LoggerFactory.getLogger(AccountsServiceImpl.class);

    private final AccountsRepository accountsRepository;

    private final AccountsMapper accountsMapper;

    public AccountsServiceImpl(AccountsRepository accountsRepository, AccountsMapper accountsMapper) {
        this.accountsRepository = accountsRepository;
        this.accountsMapper = accountsMapper;
    }

    @Override
    public AccountsDTO save(AccountsDTO accountsDTO) {
        log.debug("Request to save Accounts : {}", accountsDTO);
        Accounts accounts = accountsMapper.toEntity(accountsDTO);
        accounts = accountsRepository.save(accounts);
        return accountsMapper.toDto(accounts);
    }

    @Override
    public AccountsDTO update(AccountsDTO accountsDTO) {
        log.debug("Request to update Accounts : {}", accountsDTO);
        Accounts accounts = accountsMapper.toEntity(accountsDTO);
        accounts = accountsRepository.save(accounts);
        return accountsMapper.toDto(accounts);
    }

    @Override
    public Optional<AccountsDTO> partialUpdate(AccountsDTO accountsDTO) {
        log.debug("Request to partially update Accounts : {}", accountsDTO);

        return accountsRepository
            .findById(accountsDTO.getId())
            .map(existingAccounts -> {
                accountsMapper.partialUpdate(existingAccounts, accountsDTO);

                return existingAccounts;
            })
            .map(accountsRepository::save)
            .map(accountsMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AccountsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Accounts");
        return accountsRepository.findAll(pageable).map(accountsMapper::toDto);
    }

    /**
     *  Get all the accounts where Course is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<AccountsDTO> findAllWhereCourseIsNull() {
        log.debug("Request to get all accounts where Course is null");
        return StreamSupport.stream(accountsRepository.findAll().spliterator(), false)
            .filter(accounts -> accounts.getCourse() == null)
            .map(accountsMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<AccountsDTO> findOne(Long id) {
        log.debug("Request to get Accounts : {}", id);
        return accountsRepository.findById(id).map(accountsMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Accounts : {}", id);
        accountsRepository.deleteById(id);
    }
}

package uz.momoit.lms.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uz.momoit.lms.service.dto.AccountsDTO;

/**
 * Service Interface for managing {@link uz.momoit.lms.domain.Accounts}.
 */
public interface AccountsService {
    /**
     * Save a accounts.
     *
     * @param accountsDTO the entity to save.
     * @return the persisted entity.
     */
    AccountsDTO save(AccountsDTO accountsDTO);

    /**
     * Updates a accounts.
     *
     * @param accountsDTO the entity to update.
     * @return the persisted entity.
     */
    AccountsDTO update(AccountsDTO accountsDTO);

    /**
     * Partially updates a accounts.
     *
     * @param accountsDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AccountsDTO> partialUpdate(AccountsDTO accountsDTO);

    /**
     * Get all the accounts.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<AccountsDTO> findAll(Pageable pageable);

    /**
     * Get all the AccountsDTO where Course is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<AccountsDTO> findAllWhereCourseIsNull();

    /**
     * Get the "id" accounts.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AccountsDTO> findOne(Long id);

    /**
     * Delete the "id" accounts.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}

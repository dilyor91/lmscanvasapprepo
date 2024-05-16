package uz.momoit.lms.service.mapper;

import org.mapstruct.*;
import uz.momoit.lms.domain.Accounts;
import uz.momoit.lms.service.dto.AccountsDTO;

/**
 * Mapper for the entity {@link Accounts} and its DTO {@link AccountsDTO}.
 */
@Mapper(componentModel = "spring")
public interface AccountsMapper extends EntityMapper<AccountsDTO, Accounts> {}

package uz.momoit.lms.service.mapper;

import static uz.momoit.lms.domain.AccountsAsserts.*;
import static uz.momoit.lms.domain.AccountsTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccountsMapperTest {

    private AccountsMapper accountsMapper;

    @BeforeEach
    void setUp() {
        accountsMapper = new AccountsMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAccountsSample1();
        var actual = accountsMapper.toEntity(accountsMapper.toDto(expected));
        assertAccountsAllPropertiesEquals(expected, actual);
    }
}

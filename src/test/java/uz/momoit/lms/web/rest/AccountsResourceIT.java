package uz.momoit.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.momoit.lms.domain.AccountsAsserts.*;
import static uz.momoit.lms.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import uz.momoit.lms.IntegrationTest;
import uz.momoit.lms.domain.Accounts;
import uz.momoit.lms.domain.enumeration.UserStatus;
import uz.momoit.lms.domain.enumeration.UserType;
import uz.momoit.lms.repository.AccountsRepository;
import uz.momoit.lms.service.dto.AccountsDTO;
import uz.momoit.lms.service.mapper.AccountsMapper;

/**
 * Integration tests for the {@link AccountsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountsResourceIT {

    private static final String DEFAULT_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SORTABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SORTABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_AVATAR_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_AVATAR_IMAGE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALE = "AAAAAAAAAA";
    private static final String UPDATED_LOCALE = "BBBBBBBBBB";

    private static final String DEFAULT_GENDER = "AAAAAAAAAA";
    private static final String UPDATED_GENDER = "BBBBBBBBBB";

    private static final UserType DEFAULT_USER_TYPE = UserType.STUDENT;
    private static final UserType UPDATED_USER_TYPE = UserType.TEACHER;

    private static final UserStatus DEFAULT_USER_STATUS = UserStatus.ACTIVE;
    private static final UserStatus UPDATED_USER_STATUS = UserStatus.PENDING;

    private static final String ENTITY_API_URL = "/api/accounts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccountsRepository accountsRepository;

    @Autowired
    private AccountsMapper accountsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountsMockMvc;

    private Accounts accounts;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accounts createEntity(EntityManager em) {
        Accounts accounts = new Accounts()
            .username(DEFAULT_USERNAME)
            .fullName(DEFAULT_FULL_NAME)
            .sortableName(DEFAULT_SORTABLE_NAME)
            .avatarImageUrl(DEFAULT_AVATAR_IMAGE_URL)
            .phone(DEFAULT_PHONE)
            .locale(DEFAULT_LOCALE)
            .gender(DEFAULT_GENDER)
            .userType(DEFAULT_USER_TYPE)
            .userStatus(DEFAULT_USER_STATUS);
        return accounts;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Accounts createUpdatedEntity(EntityManager em) {
        Accounts accounts = new Accounts()
            .username(UPDATED_USERNAME)
            .fullName(UPDATED_FULL_NAME)
            .sortableName(UPDATED_SORTABLE_NAME)
            .avatarImageUrl(UPDATED_AVATAR_IMAGE_URL)
            .phone(UPDATED_PHONE)
            .locale(UPDATED_LOCALE)
            .gender(UPDATED_GENDER)
            .userType(UPDATED_USER_TYPE)
            .userStatus(UPDATED_USER_STATUS);
        return accounts;
    }

    @BeforeEach
    public void initTest() {
        accounts = createEntity(em);
    }

    @Test
    @Transactional
    void createAccounts() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);
        var returnedAccountsDTO = om.readValue(
            restAccountsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccountsDTO.class
        );

        // Validate the Accounts in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAccounts = accountsMapper.toEntity(returnedAccountsDTO);
        assertAccountsUpdatableFieldsEquals(returnedAccounts, getPersistedAccounts(returnedAccounts));
    }

    @Test
    @Transactional
    void createAccountsWithExistingId() throws Exception {
        // Create the Accounts with an existing ID
        accounts.setId(1L);
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUsernameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setUsername(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFullNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setFullName(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSortableNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setSortableName(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setPhone(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setGender(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setUserType(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accounts.setUserStatus(null);

        // Create the Accounts, which fails.
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        restAccountsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get all the accountsList
        restAccountsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accounts.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USERNAME)))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].sortableName").value(hasItem(DEFAULT_SORTABLE_NAME)))
            .andExpect(jsonPath("$.[*].avatarImageUrl").value(hasItem(DEFAULT_AVATAR_IMAGE_URL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].locale").value(hasItem(DEFAULT_LOCALE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER)))
            .andExpect(jsonPath("$.[*].userType").value(hasItem(DEFAULT_USER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].userStatus").value(hasItem(DEFAULT_USER_STATUS.toString())));
    }

    @Test
    @Transactional
    void getAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        // Get the accounts
        restAccountsMockMvc
            .perform(get(ENTITY_API_URL_ID, accounts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accounts.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USERNAME))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.sortableName").value(DEFAULT_SORTABLE_NAME))
            .andExpect(jsonPath("$.avatarImageUrl").value(DEFAULT_AVATAR_IMAGE_URL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.locale").value(DEFAULT_LOCALE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER))
            .andExpect(jsonPath("$.userType").value(DEFAULT_USER_TYPE.toString()))
            .andExpect(jsonPath("$.userStatus").value(DEFAULT_USER_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccounts() throws Exception {
        // Get the accounts
        restAccountsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accounts
        Accounts updatedAccounts = accountsRepository.findById(accounts.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccounts are not directly saved in db
        em.detach(updatedAccounts);
        updatedAccounts
            .username(UPDATED_USERNAME)
            .fullName(UPDATED_FULL_NAME)
            .sortableName(UPDATED_SORTABLE_NAME)
            .avatarImageUrl(UPDATED_AVATAR_IMAGE_URL)
            .phone(UPDATED_PHONE)
            .locale(UPDATED_LOCALE)
            .gender(UPDATED_GENDER)
            .userType(UPDATED_USER_TYPE)
            .userStatus(UPDATED_USER_STATUS);
        AccountsDTO accountsDTO = accountsMapper.toDto(updatedAccounts);

        restAccountsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountsDTO))
            )
            .andExpect(status().isOk());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccountsToMatchAllProperties(updatedAccounts);
    }

    @Test
    @Transactional
    void putNonExistingAccounts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accounts.setId(longCount.incrementAndGet());

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccounts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accounts.setId(longCount.incrementAndGet());

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accountsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccounts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accounts.setId(longCount.incrementAndGet());

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountsWithPatch() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accounts using partial update
        Accounts partialUpdatedAccounts = new Accounts();
        partialUpdatedAccounts.setId(accounts.getId());

        partialUpdatedAccounts
            .username(UPDATED_USERNAME)
            .sortableName(UPDATED_SORTABLE_NAME)
            .avatarImageUrl(UPDATED_AVATAR_IMAGE_URL)
            .phone(UPDATED_PHONE)
            .locale(UPDATED_LOCALE)
            .gender(UPDATED_GENDER);

        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccounts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccounts))
            )
            .andExpect(status().isOk());

        // Validate the Accounts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountsUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAccounts, accounts), getPersistedAccounts(accounts));
    }

    @Test
    @Transactional
    void fullUpdateAccountsWithPatch() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accounts using partial update
        Accounts partialUpdatedAccounts = new Accounts();
        partialUpdatedAccounts.setId(accounts.getId());

        partialUpdatedAccounts
            .username(UPDATED_USERNAME)
            .fullName(UPDATED_FULL_NAME)
            .sortableName(UPDATED_SORTABLE_NAME)
            .avatarImageUrl(UPDATED_AVATAR_IMAGE_URL)
            .phone(UPDATED_PHONE)
            .locale(UPDATED_LOCALE)
            .gender(UPDATED_GENDER)
            .userType(UPDATED_USER_TYPE)
            .userStatus(UPDATED_USER_STATUS);

        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccounts.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccounts))
            )
            .andExpect(status().isOk());

        // Validate the Accounts in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccountsUpdatableFieldsEquals(partialUpdatedAccounts, getPersistedAccounts(partialUpdatedAccounts));
    }

    @Test
    @Transactional
    void patchNonExistingAccounts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accounts.setId(longCount.incrementAndGet());

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccounts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accounts.setId(longCount.incrementAndGet());

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accountsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccounts() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accounts.setId(longCount.incrementAndGet());

        // Create the Accounts
        AccountsDTO accountsDTO = accountsMapper.toDto(accounts);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accountsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Accounts in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccounts() throws Exception {
        // Initialize the database
        accountsRepository.saveAndFlush(accounts);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accounts
        restAccountsMockMvc
            .perform(delete(ENTITY_API_URL_ID, accounts.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accountsRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Accounts getPersistedAccounts(Accounts accounts) {
        return accountsRepository.findById(accounts.getId()).orElseThrow();
    }

    protected void assertPersistedAccountsToMatchAllProperties(Accounts expectedAccounts) {
        assertAccountsAllPropertiesEquals(expectedAccounts, getPersistedAccounts(expectedAccounts));
    }

    protected void assertPersistedAccountsToMatchUpdatableProperties(Accounts expectedAccounts) {
        assertAccountsAllUpdatablePropertiesEquals(expectedAccounts, getPersistedAccounts(expectedAccounts));
    }
}

package uz.momoit.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.momoit.lms.domain.EnrollmentAsserts.*;
import static uz.momoit.lms.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
import uz.momoit.lms.domain.Enrollment;
import uz.momoit.lms.domain.enumeration.EnrollmentStatusEnum;
import uz.momoit.lms.repository.EnrollmentRepository;
import uz.momoit.lms.service.dto.EnrollmentDTO;
import uz.momoit.lms.service.mapper.EnrollmentMapper;

/**
 * Integration tests for the {@link EnrollmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EnrollmentResourceIT {

    private static final EnrollmentStatusEnum DEFAULT_ENROLLMENT_STATUS = EnrollmentStatusEnum.ACTIVE;
    private static final EnrollmentStatusEnum UPDATED_ENROLLMENT_STATUS = EnrollmentStatusEnum.REJECTED;

    private static final Instant DEFAULT_LAST_ACTIVITY_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACTIVITY_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENROLLMENT_START_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENROLLMENT_START_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ENROLLMENT_END_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ENROLLMENT_END_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/enrollments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private EnrollmentMapper enrollmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnrollmentMockMvc;

    private Enrollment enrollment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment()
            .enrollmentStatus(DEFAULT_ENROLLMENT_STATUS)
            .lastActivityAt(DEFAULT_LAST_ACTIVITY_AT)
            .enrollmentStartAt(DEFAULT_ENROLLMENT_START_AT)
            .enrollmentEndAt(DEFAULT_ENROLLMENT_END_AT);
        return enrollment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Enrollment createUpdatedEntity(EntityManager em) {
        Enrollment enrollment = new Enrollment()
            .enrollmentStatus(UPDATED_ENROLLMENT_STATUS)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .enrollmentStartAt(UPDATED_ENROLLMENT_START_AT)
            .enrollmentEndAt(UPDATED_ENROLLMENT_END_AT);
        return enrollment;
    }

    @BeforeEach
    public void initTest() {
        enrollment = createEntity(em);
    }

    @Test
    @Transactional
    void createEnrollment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);
        var returnedEnrollmentDTO = om.readValue(
            restEnrollmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnrollmentDTO.class
        );

        // Validate the Enrollment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEnrollment = enrollmentMapper.toEntity(returnedEnrollmentDTO);
        assertEnrollmentUpdatableFieldsEquals(returnedEnrollment, getPersistedEnrollment(returnedEnrollment));
    }

    @Test
    @Transactional
    void createEnrollmentWithExistingId() throws Exception {
        // Create the Enrollment with an existing ID
        enrollment.setId(1L);
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEnrollmentStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        enrollment.setEnrollmentStatus(null);

        // Create the Enrollment, which fails.
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        restEnrollmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnrollments() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        // Get all the enrollmentList
        restEnrollmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(enrollment.getId().intValue())))
            .andExpect(jsonPath("$.[*].enrollmentStatus").value(hasItem(DEFAULT_ENROLLMENT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lastActivityAt").value(hasItem(DEFAULT_LAST_ACTIVITY_AT.toString())))
            .andExpect(jsonPath("$.[*].enrollmentStartAt").value(hasItem(DEFAULT_ENROLLMENT_START_AT.toString())))
            .andExpect(jsonPath("$.[*].enrollmentEndAt").value(hasItem(DEFAULT_ENROLLMENT_END_AT.toString())));
    }

    @Test
    @Transactional
    void getEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        // Get the enrollment
        restEnrollmentMockMvc
            .perform(get(ENTITY_API_URL_ID, enrollment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(enrollment.getId().intValue()))
            .andExpect(jsonPath("$.enrollmentStatus").value(DEFAULT_ENROLLMENT_STATUS.toString()))
            .andExpect(jsonPath("$.lastActivityAt").value(DEFAULT_LAST_ACTIVITY_AT.toString()))
            .andExpect(jsonPath("$.enrollmentStartAt").value(DEFAULT_ENROLLMENT_START_AT.toString()))
            .andExpect(jsonPath("$.enrollmentEndAt").value(DEFAULT_ENROLLMENT_END_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingEnrollment() throws Exception {
        // Get the enrollment
        restEnrollmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enrollment
        Enrollment updatedEnrollment = enrollmentRepository.findById(enrollment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEnrollment are not directly saved in db
        em.detach(updatedEnrollment);
        updatedEnrollment
            .enrollmentStatus(UPDATED_ENROLLMENT_STATUS)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .enrollmentStartAt(UPDATED_ENROLLMENT_START_AT)
            .enrollmentEndAt(UPDATED_ENROLLMENT_END_AT);
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(updatedEnrollment);

        restEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enrollmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnrollmentToMatchAllProperties(updatedEnrollment);
    }

    @Test
    @Transactional
    void putNonExistingEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enrollmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnrollmentWithPatch() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enrollment using partial update
        Enrollment partialUpdatedEnrollment = new Enrollment();
        partialUpdatedEnrollment.setId(enrollment.getId());

        partialUpdatedEnrollment.enrollmentStatus(UPDATED_ENROLLMENT_STATUS).lastActivityAt(UPDATED_LAST_ACTIVITY_AT);

        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the Enrollment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnrollmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEnrollment, enrollment),
            getPersistedEnrollment(enrollment)
        );
    }

    @Test
    @Transactional
    void fullUpdateEnrollmentWithPatch() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the enrollment using partial update
        Enrollment partialUpdatedEnrollment = new Enrollment();
        partialUpdatedEnrollment.setId(enrollment.getId());

        partialUpdatedEnrollment
            .enrollmentStatus(UPDATED_ENROLLMENT_STATUS)
            .lastActivityAt(UPDATED_LAST_ACTIVITY_AT)
            .enrollmentStartAt(UPDATED_ENROLLMENT_START_AT)
            .enrollmentEndAt(UPDATED_ENROLLMENT_END_AT);

        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEnrollment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEnrollment))
            )
            .andExpect(status().isOk());

        // Validate the Enrollment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnrollmentUpdatableFieldsEquals(partialUpdatedEnrollment, getPersistedEnrollment(partialUpdatedEnrollment));
    }

    @Test
    @Transactional
    void patchNonExistingEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enrollmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enrollmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEnrollment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        enrollment.setId(longCount.incrementAndGet());

        // Create the Enrollment
        EnrollmentDTO enrollmentDTO = enrollmentMapper.toDto(enrollment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnrollmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enrollmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Enrollment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEnrollment() throws Exception {
        // Initialize the database
        enrollmentRepository.saveAndFlush(enrollment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the enrollment
        restEnrollmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, enrollment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enrollmentRepository.count();
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

    protected Enrollment getPersistedEnrollment(Enrollment enrollment) {
        return enrollmentRepository.findById(enrollment.getId()).orElseThrow();
    }

    protected void assertPersistedEnrollmentToMatchAllProperties(Enrollment expectedEnrollment) {
        assertEnrollmentAllPropertiesEquals(expectedEnrollment, getPersistedEnrollment(expectedEnrollment));
    }

    protected void assertPersistedEnrollmentToMatchUpdatableProperties(Enrollment expectedEnrollment) {
        assertEnrollmentAllUpdatablePropertiesEquals(expectedEnrollment, getPersistedEnrollment(expectedEnrollment));
    }
}

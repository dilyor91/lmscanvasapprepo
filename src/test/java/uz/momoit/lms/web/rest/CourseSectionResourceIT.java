package uz.momoit.lms.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uz.momoit.lms.domain.CourseSectionAsserts.*;
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
import uz.momoit.lms.domain.CourseSection;
import uz.momoit.lms.repository.CourseSectionRepository;
import uz.momoit.lms.service.dto.CourseSectionDTO;
import uz.momoit.lms.service.mapper.CourseSectionMapper;

/**
 * Integration tests for the {@link CourseSectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CourseSectionResourceIT {

    private static final String DEFAULT_SECTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/course-sections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CourseSectionRepository courseSectionRepository;

    @Autowired
    private CourseSectionMapper courseSectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseSectionMockMvc;

    private CourseSection courseSection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSection createEntity(EntityManager em) {
        CourseSection courseSection = new CourseSection().sectionName(DEFAULT_SECTION_NAME);
        return courseSection;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CourseSection createUpdatedEntity(EntityManager em) {
        CourseSection courseSection = new CourseSection().sectionName(UPDATED_SECTION_NAME);
        return courseSection;
    }

    @BeforeEach
    public void initTest() {
        courseSection = createEntity(em);
    }

    @Test
    @Transactional
    void createCourseSection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);
        var returnedCourseSectionDTO = om.readValue(
            restCourseSectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseSectionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CourseSectionDTO.class
        );

        // Validate the CourseSection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCourseSection = courseSectionMapper.toEntity(returnedCourseSectionDTO);
        assertCourseSectionUpdatableFieldsEquals(returnedCourseSection, getPersistedCourseSection(returnedCourseSection));
    }

    @Test
    @Transactional
    void createCourseSectionWithExistingId() throws Exception {
        // Create the CourseSection with an existing ID
        courseSection.setId(1L);
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseSectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        courseSection.setSectionName(null);

        // Create the CourseSection, which fails.
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        restCourseSectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseSectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCourseSections() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        // Get all the courseSectionList
        restCourseSectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseSection.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME)));
    }

    @Test
    @Transactional
    void getCourseSection() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        // Get the courseSection
        restCourseSectionMockMvc
            .perform(get(ENTITY_API_URL_ID, courseSection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(courseSection.getId().intValue()))
            .andExpect(jsonPath("$.sectionName").value(DEFAULT_SECTION_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCourseSection() throws Exception {
        // Get the courseSection
        restCourseSectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCourseSection() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseSection
        CourseSection updatedCourseSection = courseSectionRepository.findById(courseSection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCourseSection are not directly saved in db
        em.detach(updatedCourseSection);
        updatedCourseSection.sectionName(UPDATED_SECTION_NAME);
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(updatedCourseSection);

        restCourseSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseSectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courseSectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCourseSectionToMatchAllProperties(updatedCourseSection);
    }

    @Test
    @Transactional
    void putNonExistingCourseSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseSection.setId(longCount.incrementAndGet());

        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, courseSectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courseSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCourseSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseSection.setId(longCount.incrementAndGet());

        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(courseSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCourseSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseSection.setId(longCount.incrementAndGet());

        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(courseSectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCourseSectionWithPatch() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseSection using partial update
        CourseSection partialUpdatedCourseSection = new CourseSection();
        partialUpdatedCourseSection.setId(courseSection.getId());

        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourseSection))
            )
            .andExpect(status().isOk());

        // Validate the CourseSection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourseSectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCourseSection, courseSection),
            getPersistedCourseSection(courseSection)
        );
    }

    @Test
    @Transactional
    void fullUpdateCourseSectionWithPatch() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the courseSection using partial update
        CourseSection partialUpdatedCourseSection = new CourseSection();
        partialUpdatedCourseSection.setId(courseSection.getId());

        partialUpdatedCourseSection.sectionName(UPDATED_SECTION_NAME);

        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCourseSection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCourseSection))
            )
            .andExpect(status().isOk());

        // Validate the CourseSection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCourseSectionUpdatableFieldsEquals(partialUpdatedCourseSection, getPersistedCourseSection(partialUpdatedCourseSection));
    }

    @Test
    @Transactional
    void patchNonExistingCourseSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseSection.setId(longCount.incrementAndGet());

        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, courseSectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courseSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCourseSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseSection.setId(longCount.incrementAndGet());

        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(courseSectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCourseSection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        courseSection.setId(longCount.incrementAndGet());

        // Create the CourseSection
        CourseSectionDTO courseSectionDTO = courseSectionMapper.toDto(courseSection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCourseSectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(courseSectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CourseSection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCourseSection() throws Exception {
        // Initialize the database
        courseSectionRepository.saveAndFlush(courseSection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the courseSection
        restCourseSectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, courseSection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return courseSectionRepository.count();
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

    protected CourseSection getPersistedCourseSection(CourseSection courseSection) {
        return courseSectionRepository.findById(courseSection.getId()).orElseThrow();
    }

    protected void assertPersistedCourseSectionToMatchAllProperties(CourseSection expectedCourseSection) {
        assertCourseSectionAllPropertiesEquals(expectedCourseSection, getPersistedCourseSection(expectedCourseSection));
    }

    protected void assertPersistedCourseSectionToMatchUpdatableProperties(CourseSection expectedCourseSection) {
        assertCourseSectionAllUpdatablePropertiesEquals(expectedCourseSection, getPersistedCourseSection(expectedCourseSection));
    }
}

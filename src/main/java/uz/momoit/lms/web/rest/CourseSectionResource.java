package uz.momoit.lms.web.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import uz.momoit.lms.repository.CourseSectionRepository;
import uz.momoit.lms.service.CourseSectionService;
import uz.momoit.lms.service.dto.CourseSectionDTO;
import uz.momoit.lms.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link uz.momoit.lms.domain.CourseSection}.
 */
@RestController
@RequestMapping("/api/course-sections")
public class CourseSectionResource {

    private final Logger log = LoggerFactory.getLogger(CourseSectionResource.class);

    private static final String ENTITY_NAME = "courseSection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CourseSectionService courseSectionService;

    private final CourseSectionRepository courseSectionRepository;

    public CourseSectionResource(CourseSectionService courseSectionService, CourseSectionRepository courseSectionRepository) {
        this.courseSectionService = courseSectionService;
        this.courseSectionRepository = courseSectionRepository;
    }

    /**
     * {@code POST  /course-sections} : Create a new courseSection.
     *
     * @param courseSectionDTO the courseSectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new courseSectionDTO, or with status {@code 400 (Bad Request)} if the courseSection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CourseSectionDTO> createCourseSection(@Valid @RequestBody CourseSectionDTO courseSectionDTO)
        throws URISyntaxException {
        log.debug("REST request to save CourseSection : {}", courseSectionDTO);
        if (courseSectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new courseSection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        courseSectionDTO = courseSectionService.save(courseSectionDTO);
        return ResponseEntity.created(new URI("/api/course-sections/" + courseSectionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, courseSectionDTO.getId().toString()))
            .body(courseSectionDTO);
    }

    /**
     * {@code PUT  /course-sections/:id} : Updates an existing courseSection.
     *
     * @param id the id of the courseSectionDTO to save.
     * @param courseSectionDTO the courseSectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSectionDTO,
     * or with status {@code 400 (Bad Request)} if the courseSectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the courseSectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CourseSectionDTO> updateCourseSection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CourseSectionDTO courseSectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CourseSection : {}, {}", id, courseSectionDTO);
        if (courseSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseSectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        courseSectionDTO = courseSectionService.update(courseSectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseSectionDTO.getId().toString()))
            .body(courseSectionDTO);
    }

    /**
     * {@code PATCH  /course-sections/:id} : Partial updates given fields of an existing courseSection, field will ignore if it is null
     *
     * @param id the id of the courseSectionDTO to save.
     * @param courseSectionDTO the courseSectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated courseSectionDTO,
     * or with status {@code 400 (Bad Request)} if the courseSectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the courseSectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the courseSectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CourseSectionDTO> partialUpdateCourseSection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CourseSectionDTO courseSectionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CourseSection partially : {}, {}", id, courseSectionDTO);
        if (courseSectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, courseSectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!courseSectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CourseSectionDTO> result = courseSectionService.partialUpdate(courseSectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, courseSectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /course-sections} : get all the courseSections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of courseSections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CourseSectionDTO>> getAllCourseSections(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of CourseSections");
        Page<CourseSectionDTO> page = courseSectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /course-sections/:id} : get the "id" courseSection.
     *
     * @param id the id of the courseSectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the courseSectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CourseSectionDTO> getCourseSection(@PathVariable("id") Long id) {
        log.debug("REST request to get CourseSection : {}", id);
        Optional<CourseSectionDTO> courseSectionDTO = courseSectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(courseSectionDTO);
    }

    /**
     * {@code DELETE  /course-sections/:id} : delete the "id" courseSection.
     *
     * @param id the id of the courseSectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourseSection(@PathVariable("id") Long id) {
        log.debug("REST request to delete CourseSection : {}", id);
        courseSectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}

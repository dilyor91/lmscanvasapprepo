package uz.momoit.lms.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.momoit.lms.domain.CourseSection;
import uz.momoit.lms.repository.CourseSectionRepository;
import uz.momoit.lms.service.CourseSectionService;
import uz.momoit.lms.service.dto.CourseSectionDTO;
import uz.momoit.lms.service.mapper.CourseSectionMapper;

/**
 * Service Implementation for managing {@link uz.momoit.lms.domain.CourseSection}.
 */
@Service
@Transactional
public class CourseSectionServiceImpl implements CourseSectionService {

    private final Logger log = LoggerFactory.getLogger(CourseSectionServiceImpl.class);

    private final CourseSectionRepository courseSectionRepository;

    private final CourseSectionMapper courseSectionMapper;

    public CourseSectionServiceImpl(CourseSectionRepository courseSectionRepository, CourseSectionMapper courseSectionMapper) {
        this.courseSectionRepository = courseSectionRepository;
        this.courseSectionMapper = courseSectionMapper;
    }

    @Override
    public CourseSectionDTO save(CourseSectionDTO courseSectionDTO) {
        log.debug("Request to save CourseSection : {}", courseSectionDTO);
        CourseSection courseSection = courseSectionMapper.toEntity(courseSectionDTO);
        courseSection = courseSectionRepository.save(courseSection);
        return courseSectionMapper.toDto(courseSection);
    }

    @Override
    public CourseSectionDTO update(CourseSectionDTO courseSectionDTO) {
        log.debug("Request to update CourseSection : {}", courseSectionDTO);
        CourseSection courseSection = courseSectionMapper.toEntity(courseSectionDTO);
        courseSection = courseSectionRepository.save(courseSection);
        return courseSectionMapper.toDto(courseSection);
    }

    @Override
    public Optional<CourseSectionDTO> partialUpdate(CourseSectionDTO courseSectionDTO) {
        log.debug("Request to partially update CourseSection : {}", courseSectionDTO);

        return courseSectionRepository
            .findById(courseSectionDTO.getId())
            .map(existingCourseSection -> {
                courseSectionMapper.partialUpdate(existingCourseSection, courseSectionDTO);

                return existingCourseSection;
            })
            .map(courseSectionRepository::save)
            .map(courseSectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseSectionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CourseSections");
        return courseSectionRepository.findAll(pageable).map(courseSectionMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CourseSectionDTO> findOne(Long id) {
        log.debug("Request to get CourseSection : {}", id);
        return courseSectionRepository.findById(id).map(courseSectionMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CourseSection : {}", id);
        courseSectionRepository.deleteById(id);
    }
}

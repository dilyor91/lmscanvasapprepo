package uz.momoit.lms.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.momoit.lms.domain.Enrollment;
import uz.momoit.lms.repository.EnrollmentRepository;
import uz.momoit.lms.service.EnrollmentService;
import uz.momoit.lms.service.dto.EnrollmentDTO;
import uz.momoit.lms.service.mapper.EnrollmentMapper;

/**
 * Service Implementation for managing {@link uz.momoit.lms.domain.Enrollment}.
 */
@Service
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final Logger log = LoggerFactory.getLogger(EnrollmentServiceImpl.class);

    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentServiceImpl(EnrollmentRepository enrollmentRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    @Override
    public EnrollmentDTO save(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to save Enrollment : {}", enrollmentDTO);
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public EnrollmentDTO update(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to update Enrollment : {}", enrollmentDTO);
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        enrollment = enrollmentRepository.save(enrollment);
        return enrollmentMapper.toDto(enrollment);
    }

    @Override
    public Optional<EnrollmentDTO> partialUpdate(EnrollmentDTO enrollmentDTO) {
        log.debug("Request to partially update Enrollment : {}", enrollmentDTO);

        return enrollmentRepository
            .findById(enrollmentDTO.getId())
            .map(existingEnrollment -> {
                enrollmentMapper.partialUpdate(existingEnrollment, enrollmentDTO);

                return existingEnrollment;
            })
            .map(enrollmentRepository::save)
            .map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EnrollmentDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enrollments");
        return enrollmentRepository.findAll(pageable).map(enrollmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EnrollmentDTO> findOne(Long id) {
        log.debug("Request to get Enrollment : {}", id);
        return enrollmentRepository.findById(id).map(enrollmentMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Enrollment : {}", id);
        enrollmentRepository.deleteById(id);
    }
}

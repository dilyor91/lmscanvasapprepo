package uz.momoit.lms.service.mapper;

import static uz.momoit.lms.domain.EnrollmentAsserts.*;
import static uz.momoit.lms.domain.EnrollmentTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EnrollmentMapperTest {

    private EnrollmentMapper enrollmentMapper;

    @BeforeEach
    void setUp() {
        enrollmentMapper = new EnrollmentMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getEnrollmentSample1();
        var actual = enrollmentMapper.toEntity(enrollmentMapper.toDto(expected));
        assertEnrollmentAllPropertiesEquals(expected, actual);
    }
}

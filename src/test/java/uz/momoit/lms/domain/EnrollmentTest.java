package uz.momoit.lms.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.momoit.lms.domain.AccountsTestSamples.*;
import static uz.momoit.lms.domain.CourseSectionTestSamples.*;
import static uz.momoit.lms.domain.CourseTestSamples.*;
import static uz.momoit.lms.domain.EnrollmentTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.momoit.lms.web.rest.TestUtil;

class EnrollmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Enrollment.class);
        Enrollment enrollment1 = getEnrollmentSample1();
        Enrollment enrollment2 = new Enrollment();
        assertThat(enrollment1).isNotEqualTo(enrollment2);

        enrollment2.setId(enrollment1.getId());
        assertThat(enrollment1).isEqualTo(enrollment2);

        enrollment2 = getEnrollmentSample2();
        assertThat(enrollment1).isNotEqualTo(enrollment2);
    }

    @Test
    void accountTest() throws Exception {
        Enrollment enrollment = getEnrollmentRandomSampleGenerator();
        Accounts accountsBack = getAccountsRandomSampleGenerator();

        enrollment.setAccount(accountsBack);
        assertThat(enrollment.getAccount()).isEqualTo(accountsBack);

        enrollment.account(null);
        assertThat(enrollment.getAccount()).isNull();
    }

    @Test
    void courseSectionTest() throws Exception {
        Enrollment enrollment = getEnrollmentRandomSampleGenerator();
        CourseSection courseSectionBack = getCourseSectionRandomSampleGenerator();

        enrollment.setCourseSection(courseSectionBack);
        assertThat(enrollment.getCourseSection()).isEqualTo(courseSectionBack);

        enrollment.courseSection(null);
        assertThat(enrollment.getCourseSection()).isNull();
    }

    @Test
    void courseTest() throws Exception {
        Enrollment enrollment = getEnrollmentRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        enrollment.setCourse(courseBack);
        assertThat(enrollment.getCourse()).isEqualTo(courseBack);

        enrollment.course(null);
        assertThat(enrollment.getCourse()).isNull();
    }
}

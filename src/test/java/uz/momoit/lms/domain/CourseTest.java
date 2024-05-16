package uz.momoit.lms.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.momoit.lms.domain.AccountsTestSamples.*;
import static uz.momoit.lms.domain.CourseTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.momoit.lms.web.rest.TestUtil;

class CourseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Course.class);
        Course course1 = getCourseSample1();
        Course course2 = new Course();
        assertThat(course1).isNotEqualTo(course2);

        course2.setId(course1.getId());
        assertThat(course1).isEqualTo(course2);

        course2 = getCourseSample2();
        assertThat(course1).isNotEqualTo(course2);
    }

    @Test
    void accountTest() throws Exception {
        Course course = getCourseRandomSampleGenerator();
        Accounts accountsBack = getAccountsRandomSampleGenerator();

        course.setAccount(accountsBack);
        assertThat(course.getAccount()).isEqualTo(accountsBack);

        course.account(null);
        assertThat(course.getAccount()).isNull();
    }
}

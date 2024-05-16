package uz.momoit.lms.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static uz.momoit.lms.domain.AccountsTestSamples.*;
import static uz.momoit.lms.domain.CourseTestSamples.*;

import org.junit.jupiter.api.Test;
import uz.momoit.lms.web.rest.TestUtil;

class AccountsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Accounts.class);
        Accounts accounts1 = getAccountsSample1();
        Accounts accounts2 = new Accounts();
        assertThat(accounts1).isNotEqualTo(accounts2);

        accounts2.setId(accounts1.getId());
        assertThat(accounts1).isEqualTo(accounts2);

        accounts2 = getAccountsSample2();
        assertThat(accounts1).isNotEqualTo(accounts2);
    }

    @Test
    void courseTest() throws Exception {
        Accounts accounts = getAccountsRandomSampleGenerator();
        Course courseBack = getCourseRandomSampleGenerator();

        accounts.setCourse(courseBack);
        assertThat(accounts.getCourse()).isEqualTo(courseBack);
        assertThat(courseBack.getAccount()).isEqualTo(accounts);

        accounts.course(null);
        assertThat(accounts.getCourse()).isNull();
        assertThat(courseBack.getAccount()).isNull();
    }
}

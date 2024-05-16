package uz.momoit.lms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import uz.momoit.lms.domain.enumeration.EnrollmentStatusEnum;

/**
 * A DTO for the {@link uz.momoit.lms.domain.Enrollment} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EnrollmentDTO implements Serializable {

    private Long id;

    @NotNull
    private EnrollmentStatusEnum enrollmentStatus;

    private Instant lastActivityAt;

    private Instant enrollmentStartAt;

    private Instant enrollmentEndAt;

    private AccountsDTO account;

    private CourseSectionDTO courseSection;

    private CourseDTO course;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnrollmentStatusEnum getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public void setEnrollmentStatus(EnrollmentStatusEnum enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Instant getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Instant lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public Instant getEnrollmentStartAt() {
        return enrollmentStartAt;
    }

    public void setEnrollmentStartAt(Instant enrollmentStartAt) {
        this.enrollmentStartAt = enrollmentStartAt;
    }

    public Instant getEnrollmentEndAt() {
        return enrollmentEndAt;
    }

    public void setEnrollmentEndAt(Instant enrollmentEndAt) {
        this.enrollmentEndAt = enrollmentEndAt;
    }

    public AccountsDTO getAccount() {
        return account;
    }

    public void setAccount(AccountsDTO account) {
        this.account = account;
    }

    public CourseSectionDTO getCourseSection() {
        return courseSection;
    }

    public void setCourseSection(CourseSectionDTO courseSection) {
        this.courseSection = courseSection;
    }

    public CourseDTO getCourse() {
        return course;
    }

    public void setCourse(CourseDTO course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EnrollmentDTO)) {
            return false;
        }

        EnrollmentDTO enrollmentDTO = (EnrollmentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, enrollmentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EnrollmentDTO{" +
            "id=" + getId() +
            ", enrollmentStatus='" + getEnrollmentStatus() + "'" +
            ", lastActivityAt='" + getLastActivityAt() + "'" +
            ", enrollmentStartAt='" + getEnrollmentStartAt() + "'" +
            ", enrollmentEndAt='" + getEnrollmentEndAt() + "'" +
            ", account=" + getAccount() +
            ", courseSection=" + getCourseSection() +
            ", course=" + getCourse() +
            "}";
    }
}

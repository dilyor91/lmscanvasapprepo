package uz.momoit.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import uz.momoit.lms.domain.enumeration.EnrollmentStatusEnum;

/**
 * A Enrollment.
 */
@Entity
@Table(name = "enrollment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Enrollment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "enrollment_status", nullable = false)
    private EnrollmentStatusEnum enrollmentStatus;

    @Column(name = "last_activity_at")
    private Instant lastActivityAt;

    @Column(name = "enrollment_start_at")
    private Instant enrollmentStartAt;

    @Column(name = "enrollment_end_at")
    private Instant enrollmentEndAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "course" }, allowSetters = true)
    private Accounts account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "course" }, allowSetters = true)
    private CourseSection courseSection;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "account" }, allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Enrollment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EnrollmentStatusEnum getEnrollmentStatus() {
        return this.enrollmentStatus;
    }

    public Enrollment enrollmentStatus(EnrollmentStatusEnum enrollmentStatus) {
        this.setEnrollmentStatus(enrollmentStatus);
        return this;
    }

    public void setEnrollmentStatus(EnrollmentStatusEnum enrollmentStatus) {
        this.enrollmentStatus = enrollmentStatus;
    }

    public Instant getLastActivityAt() {
        return this.lastActivityAt;
    }

    public Enrollment lastActivityAt(Instant lastActivityAt) {
        this.setLastActivityAt(lastActivityAt);
        return this;
    }

    public void setLastActivityAt(Instant lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public Instant getEnrollmentStartAt() {
        return this.enrollmentStartAt;
    }

    public Enrollment enrollmentStartAt(Instant enrollmentStartAt) {
        this.setEnrollmentStartAt(enrollmentStartAt);
        return this;
    }

    public void setEnrollmentStartAt(Instant enrollmentStartAt) {
        this.enrollmentStartAt = enrollmentStartAt;
    }

    public Instant getEnrollmentEndAt() {
        return this.enrollmentEndAt;
    }

    public Enrollment enrollmentEndAt(Instant enrollmentEndAt) {
        this.setEnrollmentEndAt(enrollmentEndAt);
        return this;
    }

    public void setEnrollmentEndAt(Instant enrollmentEndAt) {
        this.enrollmentEndAt = enrollmentEndAt;
    }

    public Accounts getAccount() {
        return this.account;
    }

    public void setAccount(Accounts accounts) {
        this.account = accounts;
    }

    public Enrollment account(Accounts accounts) {
        this.setAccount(accounts);
        return this;
    }

    public CourseSection getCourseSection() {
        return this.courseSection;
    }

    public void setCourseSection(CourseSection courseSection) {
        this.courseSection = courseSection;
    }

    public Enrollment courseSection(CourseSection courseSection) {
        this.setCourseSection(courseSection);
        return this;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Enrollment course(Course course) {
        this.setCourse(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Enrollment)) {
            return false;
        }
        return getId() != null && getId().equals(((Enrollment) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Enrollment{" +
            "id=" + getId() +
            ", enrollmentStatus='" + getEnrollmentStatus() + "'" +
            ", lastActivityAt='" + getLastActivityAt() + "'" +
            ", enrollmentStartAt='" + getEnrollmentStartAt() + "'" +
            ", enrollmentEndAt='" + getEnrollmentEndAt() + "'" +
            "}";
    }
}

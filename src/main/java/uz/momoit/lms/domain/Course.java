package uz.momoit.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "course_name", nullable = false)
    private String courseName;

    @NotNull
    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "course_image_path")
    private String courseImagePath;

    @NotNull
    @Column(name = "course_start_date", nullable = false)
    private Instant courseStartDate;

    @NotNull
    @Column(name = "course_end_date", nullable = false)
    private Instant courseEndDate;

    @NotNull
    @Column(name = "course_format", nullable = false)
    private String courseFormat;

    @NotNull
    @Column(name = "published", nullable = false)
    private Boolean published;

    @Column(name = "self_enrollment")
    private Boolean selfEnrollment;

    @Column(name = "self_enrollment_code")
    private String selfEnrollmentCode;

    @NotNull
    @Column(name = "storage_quota", nullable = false)
    private Integer storageQuota;

    @NotNull
    @Column(name = "status", nullable = false)
    private Boolean status;

    @JsonIgnoreProperties(value = { "course" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private Accounts account;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Course id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public Course courseName(String courseName) {
        this.setCourseName(courseName);
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public Course courseCode(String courseCode) {
        this.setCourseCode(courseCode);
        return this;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseImagePath() {
        return this.courseImagePath;
    }

    public Course courseImagePath(String courseImagePath) {
        this.setCourseImagePath(courseImagePath);
        return this;
    }

    public void setCourseImagePath(String courseImagePath) {
        this.courseImagePath = courseImagePath;
    }

    public Instant getCourseStartDate() {
        return this.courseStartDate;
    }

    public Course courseStartDate(Instant courseStartDate) {
        this.setCourseStartDate(courseStartDate);
        return this;
    }

    public void setCourseStartDate(Instant courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public Instant getCourseEndDate() {
        return this.courseEndDate;
    }

    public Course courseEndDate(Instant courseEndDate) {
        this.setCourseEndDate(courseEndDate);
        return this;
    }

    public void setCourseEndDate(Instant courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseFormat() {
        return this.courseFormat;
    }

    public Course courseFormat(String courseFormat) {
        this.setCourseFormat(courseFormat);
        return this;
    }

    public void setCourseFormat(String courseFormat) {
        this.courseFormat = courseFormat;
    }

    public Boolean getPublished() {
        return this.published;
    }

    public Course published(Boolean published) {
        this.setPublished(published);
        return this;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getSelfEnrollment() {
        return this.selfEnrollment;
    }

    public Course selfEnrollment(Boolean selfEnrollment) {
        this.setSelfEnrollment(selfEnrollment);
        return this;
    }

    public void setSelfEnrollment(Boolean selfEnrollment) {
        this.selfEnrollment = selfEnrollment;
    }

    public String getSelfEnrollmentCode() {
        return this.selfEnrollmentCode;
    }

    public Course selfEnrollmentCode(String selfEnrollmentCode) {
        this.setSelfEnrollmentCode(selfEnrollmentCode);
        return this;
    }

    public void setSelfEnrollmentCode(String selfEnrollmentCode) {
        this.selfEnrollmentCode = selfEnrollmentCode;
    }

    public Integer getStorageQuota() {
        return this.storageQuota;
    }

    public Course storageQuota(Integer storageQuota) {
        this.setStorageQuota(storageQuota);
        return this;
    }

    public void setStorageQuota(Integer storageQuota) {
        this.storageQuota = storageQuota;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public Course status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Accounts getAccount() {
        return this.account;
    }

    public void setAccount(Accounts accounts) {
        this.account = accounts;
    }

    public Course account(Accounts accounts) {
        this.setAccount(accounts);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return getId() != null && getId().equals(((Course) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", courseName='" + getCourseName() + "'" +
            ", courseCode='" + getCourseCode() + "'" +
            ", courseImagePath='" + getCourseImagePath() + "'" +
            ", courseStartDate='" + getCourseStartDate() + "'" +
            ", courseEndDate='" + getCourseEndDate() + "'" +
            ", courseFormat='" + getCourseFormat() + "'" +
            ", published='" + getPublished() + "'" +
            ", selfEnrollment='" + getSelfEnrollment() + "'" +
            ", selfEnrollmentCode='" + getSelfEnrollmentCode() + "'" +
            ", storageQuota=" + getStorageQuota() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}

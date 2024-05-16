package uz.momoit.lms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link uz.momoit.lms.domain.Course} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseDTO implements Serializable {

    private Long id;

    @NotNull
    private String courseName;

    @NotNull
    private String courseCode;

    private String courseImagePath;

    @NotNull
    private Instant courseStartDate;

    @NotNull
    private Instant courseEndDate;

    @NotNull
    private String courseFormat;

    @NotNull
    private Boolean published;

    private Boolean selfEnrollment;

    private String selfEnrollmentCode;

    @NotNull
    private Integer storageQuota;

    @NotNull
    private Boolean status;

    private AccountsDTO account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseImagePath() {
        return courseImagePath;
    }

    public void setCourseImagePath(String courseImagePath) {
        this.courseImagePath = courseImagePath;
    }

    public Instant getCourseStartDate() {
        return courseStartDate;
    }

    public void setCourseStartDate(Instant courseStartDate) {
        this.courseStartDate = courseStartDate;
    }

    public Instant getCourseEndDate() {
        return courseEndDate;
    }

    public void setCourseEndDate(Instant courseEndDate) {
        this.courseEndDate = courseEndDate;
    }

    public String getCourseFormat() {
        return courseFormat;
    }

    public void setCourseFormat(String courseFormat) {
        this.courseFormat = courseFormat;
    }

    public Boolean getPublished() {
        return published;
    }

    public void setPublished(Boolean published) {
        this.published = published;
    }

    public Boolean getSelfEnrollment() {
        return selfEnrollment;
    }

    public void setSelfEnrollment(Boolean selfEnrollment) {
        this.selfEnrollment = selfEnrollment;
    }

    public String getSelfEnrollmentCode() {
        return selfEnrollmentCode;
    }

    public void setSelfEnrollmentCode(String selfEnrollmentCode) {
        this.selfEnrollmentCode = selfEnrollmentCode;
    }

    public Integer getStorageQuota() {
        return storageQuota;
    }

    public void setStorageQuota(Integer storageQuota) {
        this.storageQuota = storageQuota;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AccountsDTO getAccount() {
        return account;
    }

    public void setAccount(AccountsDTO account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        CourseDTO courseDTO = (CourseDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courseDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
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
            ", account=" + getAccount() +
            "}";
    }
}

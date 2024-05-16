package uz.momoit.lms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import uz.momoit.lms.domain.enumeration.UserStatus;
import uz.momoit.lms.domain.enumeration.UserType;

/**
 * A Accounts.
 */
@Entity
@Table(name = "accounts")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Accounts implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "username", nullable = false)
    private String username;

    @NotNull
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotNull
    @Column(name = "sortable_name", nullable = false)
    private String sortableName;

    @Column(name = "avatar_image_url")
    private String avatarImageUrl;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "locale")
    private String locale;

    @NotNull
    @Column(name = "gender", nullable = false)
    private String gender;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_type", nullable = false)
    private UserType userType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "user_status", nullable = false)
    private UserStatus userStatus;

    @JsonIgnoreProperties(value = { "account" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "account")
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Accounts id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public Accounts username(String username) {
        this.setUsername(username);
        return this;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Accounts fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSortableName() {
        return this.sortableName;
    }

    public Accounts sortableName(String sortableName) {
        this.setSortableName(sortableName);
        return this;
    }

    public void setSortableName(String sortableName) {
        this.sortableName = sortableName;
    }

    public String getAvatarImageUrl() {
        return this.avatarImageUrl;
    }

    public Accounts avatarImageUrl(String avatarImageUrl) {
        this.setAvatarImageUrl(avatarImageUrl);
        return this;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getPhone() {
        return this.phone;
    }

    public Accounts phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocale() {
        return this.locale;
    }

    public Accounts locale(String locale) {
        this.setLocale(locale);
        return this;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getGender() {
        return this.gender;
    }

    public Accounts gender(String gender) {
        this.setGender(gender);
        return this;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public Accounts userType(UserType userType) {
        this.setUserType(userType);
        return this;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserStatus getUserStatus() {
        return this.userStatus;
    }

    public Accounts userStatus(UserStatus userStatus) {
        this.setUserStatus(userStatus);
        return this;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public Course getCourse() {
        return this.course;
    }

    public void setCourse(Course course) {
        if (this.course != null) {
            this.course.setAccount(null);
        }
        if (course != null) {
            course.setAccount(this);
        }
        this.course = course;
    }

    public Accounts course(Course course) {
        this.setCourse(course);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Accounts)) {
            return false;
        }
        return getId() != null && getId().equals(((Accounts) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Accounts{" +
            "id=" + getId() +
            ", username='" + getUsername() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", sortableName='" + getSortableName() + "'" +
            ", avatarImageUrl='" + getAvatarImageUrl() + "'" +
            ", phone='" + getPhone() + "'" +
            ", locale='" + getLocale() + "'" +
            ", gender='" + getGender() + "'" +
            ", userType='" + getUserType() + "'" +
            ", userStatus='" + getUserStatus() + "'" +
            "}";
    }
}

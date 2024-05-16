package uz.momoit.lms.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import uz.momoit.lms.domain.enumeration.UserStatus;
import uz.momoit.lms.domain.enumeration.UserType;

/**
 * A DTO for the {@link uz.momoit.lms.domain.Accounts} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountsDTO implements Serializable {

    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    private String sortableName;

    private String avatarImageUrl;

    @NotNull
    private String phone;

    private String locale;

    @NotNull
    private String gender;

    @NotNull
    private UserType userType;

    @NotNull
    private UserStatus userStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSortableName() {
        return sortableName;
    }

    public void setSortableName(String sortableName) {
        this.sortableName = sortableName;
    }

    public String getAvatarImageUrl() {
        return avatarImageUrl;
    }

    public void setAvatarImageUrl(String avatarImageUrl) {
        this.avatarImageUrl = avatarImageUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountsDTO)) {
            return false;
        }

        AccountsDTO accountsDTO = (AccountsDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accountsDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountsDTO{" +
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

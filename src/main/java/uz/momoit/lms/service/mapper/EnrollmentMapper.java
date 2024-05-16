package uz.momoit.lms.service.mapper;

import org.mapstruct.*;
import uz.momoit.lms.domain.Accounts;
import uz.momoit.lms.domain.Course;
import uz.momoit.lms.domain.CourseSection;
import uz.momoit.lms.domain.Enrollment;
import uz.momoit.lms.service.dto.AccountsDTO;
import uz.momoit.lms.service.dto.CourseDTO;
import uz.momoit.lms.service.dto.CourseSectionDTO;
import uz.momoit.lms.service.dto.EnrollmentDTO;

/**
 * Mapper for the entity {@link Enrollment} and its DTO {@link EnrollmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnrollmentMapper extends EntityMapper<EnrollmentDTO, Enrollment> {
    @Mapping(target = "account", source = "account", qualifiedByName = "accountsId")
    @Mapping(target = "courseSection", source = "courseSection", qualifiedByName = "courseSectionId")
    @Mapping(target = "course", source = "course", qualifiedByName = "courseId")
    EnrollmentDTO toDto(Enrollment s);

    @Named("accountsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountsDTO toDtoAccountsId(Accounts accounts);

    @Named("courseSectionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseSectionDTO toDtoCourseSectionId(CourseSection courseSection);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);
}

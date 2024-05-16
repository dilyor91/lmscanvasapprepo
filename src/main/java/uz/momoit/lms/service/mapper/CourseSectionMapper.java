package uz.momoit.lms.service.mapper;

import org.mapstruct.*;
import uz.momoit.lms.domain.Course;
import uz.momoit.lms.domain.CourseSection;
import uz.momoit.lms.service.dto.CourseDTO;
import uz.momoit.lms.service.dto.CourseSectionDTO;

/**
 * Mapper for the entity {@link CourseSection} and its DTO {@link CourseSectionDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseSectionMapper extends EntityMapper<CourseSectionDTO, CourseSection> {
    @Mapping(target = "course", source = "course", qualifiedByName = "courseId")
    CourseSectionDTO toDto(CourseSection s);

    @Named("courseId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CourseDTO toDtoCourseId(Course course);
}

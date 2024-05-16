package uz.momoit.lms.service.mapper;

import org.mapstruct.*;
import uz.momoit.lms.domain.Accounts;
import uz.momoit.lms.domain.Course;
import uz.momoit.lms.service.dto.AccountsDTO;
import uz.momoit.lms.service.dto.CourseDTO;

/**
 * Mapper for the entity {@link Course} and its DTO {@link CourseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CourseMapper extends EntityMapper<CourseDTO, Course> {
    @Mapping(target = "account", source = "account", qualifiedByName = "accountsId")
    CourseDTO toDto(Course s);

    @Named("accountsId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AccountsDTO toDtoAccountsId(Accounts accounts);
}

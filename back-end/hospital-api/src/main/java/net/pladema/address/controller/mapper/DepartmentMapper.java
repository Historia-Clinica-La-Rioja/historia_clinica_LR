package net.pladema.address.controller.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.address.controller.dto.DepartmentDto;
import net.pladema.address.repository.entity.Department;

@Mapper
public interface DepartmentMapper {

    @Named("fromDepartment")
    public DepartmentDto fromDepartment(Department department);
}

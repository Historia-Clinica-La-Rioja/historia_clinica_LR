package net.pladema.address.controller.mapper;

import net.pladema.address.controller.service.domain.DepartmentBo;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import net.pladema.address.controller.dto.DepartmentDto;
import net.pladema.address.repository.entity.Department;

@Mapper
public interface DepartmentMapper {

    @Named("fromDepartment")
    public DepartmentDto fromDepartment(Department department);

	@Named("fromDepartmentBo")
	public DepartmentDto fromDepartmentBo(DepartmentBo departmentBo);

	@Named("fromDepartmentToDepartmentBo")
	public DepartmentBo fromDepartmentToDepartmentBo(Department department);
}

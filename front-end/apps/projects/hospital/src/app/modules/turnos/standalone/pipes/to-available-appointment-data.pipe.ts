import { Pipe, PipeTransform } from "@angular/core";
import { DiaryAvailableAppointmentsDto } from "@api-rest/api-model";
import { dateTimeDtoToDate } from "@api-rest/mapper/date-dto.mapper";
import { AvailableAppointmentData } from "../components/available-appointment-data/available-appointment-data.component";

@Pipe({
	name: 'toAvailableAppointmentData',
    standalone: true,
})
export class ToAvailableAppointmentDataPipe implements PipeTransform {

	transform(availableAppointmentsDto: DiaryAvailableAppointmentsDto): AvailableAppointmentData {
        const dateTimeDto = { date: availableAppointmentsDto.date, time: availableAppointmentsDto.hour };
        const appointmentDate = dateTimeDtoToDate(dateTimeDto);
    
        return {
            departmentDescription: availableAppointmentsDto.department.description,
            date: appointmentDate,
            doctorOffice: availableAppointmentsDto.doctorOffice,
            institutionName: availableAppointmentsDto.institution.name,
            jointDiary: availableAppointmentsDto.jointDiary,
            professionalFullName: availableAppointmentsDto.professionalFullName,
            ...(availableAppointmentsDto.clinicalSpecialty && { clinicalSpecialtyName: availableAppointmentsDto.clinicalSpecialty.name }),
            ...(availableAppointmentsDto.practice && { practiceDescription: availableAppointmentsDto.practice.pt })
        }
	}
}
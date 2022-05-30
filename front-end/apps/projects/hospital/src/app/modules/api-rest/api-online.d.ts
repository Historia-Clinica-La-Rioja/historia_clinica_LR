/* tslint:disable */
/* eslint-disable */

export interface AvailabilityDto {
    date: Date;
    slots: string[];
}

export interface BookingAppointmentDto {
    coverageId: number;
    day: string;
    diaryId: number;
    hour: string;
    openingHoursId: number;
    phoneNumber: string;
    snomedId: number;
    specialtyId: number;
}

export interface BookingDiaryDto {
    appointmentDuration: number;
    doctorsOfficeDescription: string;
    doctorsOfficeId: number;
    endDate: Date;
    from: Date;
    id: number;
    openingHoursId: number;
    startDate: Date;
    to: Date;
}

export interface BookingDto {
    appointmentDataEmail: string;
    bookingAppointmentDto: BookingAppointmentDto;
    bookingPersonDto: BookingPersonDto;
}

export interface BookingHealthInsuranceDto {
    description: string;
    id: number;
}

export interface BookingInstitutionDto {
    description: string;
    id: number;
}

export interface BookingPersonDto {
    birthDate: string;
    email: string;
    firstName: string;
    genderId: number;
    idNumber: string;
    lastName: string;
}

export interface BookingProfessionalDto {
    coverage: boolean;
    id: number;
    name: string;
}

export interface BookingSpecialtyDto {
    description: string;
    id: number;
}

export interface DiaryAvailabilityDto {
    diary: BookingDiaryDto;
    slots: AvailabilityDto;
}

export interface PracticeDto {
    coverage: boolean;
    coverageText: string;
    description: string;
    id: number;
    snomedId: number;
}

export interface ProfessionalAvailabilityDto {
    availability: DiaryAvailabilityDto[];
    professional: BookingProfessionalDto;
}

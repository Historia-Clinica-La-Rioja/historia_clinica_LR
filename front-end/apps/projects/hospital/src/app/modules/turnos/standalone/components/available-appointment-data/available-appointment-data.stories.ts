import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { AvailableAppointmentDataComponent } from './available-appointment-data.component';
import { PresentationModule } from '@presentation/presentation.module';

const meta: Meta<AvailableAppointmentDataComponent> = {
    title: 'Turnos/AvailableAppointmentDataComponent',
    component: AvailableAppointmentDataComponent,
    tags: ['autodocs'],
    decorators: [
        moduleMetadata({
            imports: [PresentationModule]
        }),
    ],
};

export default meta;
type Story = StoryObj<AvailableAppointmentDataComponent>;

export const full: Story = {
    args: {
        availableAppointment: {
            clinicalSpecialtyName: "Clinica medica",
            practiceDescription: "hemograma completo",
            departmentDescription: "Tandil",
            date: new Date(),
            doctorOffice: "Consultorio A",
            institutionName: "Clinica chacabuco",
            jointDiary: true,
            professionalFullName: "Joselina valdez"
        }
    }
};

export const withoutPractice: Story = {
    args: {
        availableAppointment: {
            clinicalSpecialtyName: "Clinica medica",
            departmentDescription: "Tandil",
            date: new Date(),
            doctorOffice: "Consultorio A",
            institutionName: "Clinica chacabuco",
            jointDiary: true,
            professionalFullName: "Joselina valdez"
        }
    }
};

export const withoutClinicalSpecialty: Story = {
    args: {
        availableAppointment: {
            practiceDescription: "hemograma completo",
            departmentDescription: "Tandil",
            date: new Date(),
            doctorOffice: "Consultorio A",
            institutionName: "Clinica chacabuco",
            jointDiary: true,
            professionalFullName: "Joselina valdez"
        }
    }
};

export const isNotAJointDiary: Story = {
    args: {
        availableAppointment: {
            practiceDescription: "hemograma completo",
            departmentDescription: "Tandil",
            date: new Date(),
            doctorOffice: "Consultorio A",
            jointDiary: false,
            institutionName: "Clinica chacabuco",
            professionalFullName: "Joselina valdez"
        }
    }
};


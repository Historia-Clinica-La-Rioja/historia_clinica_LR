import { type Meta, type StoryObj } from '@storybook/angular';
import { TimePickerComponent } from './time-picker.component';

const meta: Meta<TimePickerComponent> = {
    title: 'Presentation/TimePickerComponent',
    component: TimePickerComponent,
    tags: ['autodocs'],
    argTypes: {
        timePickerData: {
            description: '<strong>defaultTime:</strong> objeto para settear hora por defecto en el componente <br> <strong>hoursMinValue y hoursMaxValue</strong> son inputs de tipo number que permiten limitar las opciones de las horas <br><strong>minuteStep</strong> es un input de tipo number que permite cambiar el step de los minutos <br> <strong>hideLabel</strong> es un input de tipo boolean que permite ocultar el label de Hora <br> <strong>isRequired</strong> es un input de tipo boolean que indica si pertenece a un formulario cuyo campo es requerido',
            table: { 
                defaultValue: { summary: 'Para utilizar el componente con valores por defecto no es necesario mandar el timePickerData como input' } 
            }
        },
        submitted: {
            description: 'Input de tipo boolean que le indica al componente que el formulario que lo utiliza se intentó enviar y que debe revisar la validez de sus campos',
            table: { 
                defaultValue: { summary: 'false' } 
            },
            options: [true, false],
            control: {
				type: 'select',
			}
        }
	},
    parameters: {
        docs: {
            description: {
                component: "Componente de selección de horario que permite tanto seleccionarlo de un dropdown como escribirlo en un input de texto. Devuelve el valor en un objeto de tipo TimePickerDto: { hours: number, minutes: number }"
            }
        }
    },
};

export default meta;

type Story = StoryObj<TimePickerComponent>;

export const defaultValue: Story = {
    args: {
        timePickerData: {
            hoursMinValue: 0,
            hoursMaxValue: 23,
            minuteStep: 5,
            hideLabel: false,
            isRequired: false,
        }
    }
};

export const setDefaultTimeViaInput: Story = {
    args: {
        timePickerData: {
            defaultTime: { hours: 10, minutes: 22},
        }
    }
};

export const hourInputLimitedBetween8And16: Story = {
    args: {
        timePickerData: {
            hoursMinValue: 8,
            hoursMaxValue: 16,
        }
    }
};

export const minutesOptionsIn2MinutesStep: Story = {
    args: {
        timePickerData: {
            minuteStep: 2,
        }
    }
};

export const WithoutHourLabel: Story = {
    args: {
        timePickerData: {
            hideLabel: true,
        }
    }
};
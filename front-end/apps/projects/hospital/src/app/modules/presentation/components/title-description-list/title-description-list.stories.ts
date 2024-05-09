import type { Meta, StoryObj } from '@storybook/angular';
import { TitleDescriptionListComponent } from './title-description-list.component';
import { DateFormat } from '../description-item/description-item.component';


const meta: Meta<TitleDescriptionListComponent> = {
	title: 'Presentation/TitleDescriptionListComponent',
	component: TitleDescriptionListComponent,
	tags: ['autodocs'],
	argTypes: {
		title: {
			description: 'String para el titulo',
		},
		descriptionData: {
            description: '<strong>descriptionData:</strong> arreglo de tipo DescriptionItemData<br> <strong>description</strong> atributo requerido de tipo string que tiene la descripción a mostrar <br> <strong>dateToShow</strong> atributo opcional que tiene un date y un format(dateFormat)',
        },
	},
    parameters: {
        docs: {
            description: {
                component: "Componente para mostrar un titulo y una descripción o listado de descripciones. Cada descripcion puede estar acompañada por una fecha, hora o fecha y hora debajo. A su vez, si se trata de un listado de descripciones, cada descripcion se mostrara con un bull adelante"
            }
        }
    },
};

export default meta;

type Story = StoryObj<TitleDescriptionListComponent>;

export const OnlyTitle: Story = {
	args: {
		title: "Diagnóstico principal"
	}
};

export const SingleDescriptionCase: Story = {
	args: {
		title: "Diagnóstico principal",
		descriptionData: [{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		}]
	}
};

export const SingleDescriptionCaseWithDate: Story = {
	args: {
		title: "Diagnóstico principal",
		descriptionData: [{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.DATE_TIME,
			}
		}]
	}
};

export const MultiDescriptionCase: Story = {
	args: {
		title: "Diagnóstico principal",
		descriptionData: [{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		},
		]
	}
};

export const MultiDescriptionCaseWithDate: Story = {
	args: {
		title: "Diagnóstico principal",
		descriptionData: [{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.DATE_TIME,
			}
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.DATE_TIME,
			}
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.DATE_TIME,
			}
		},
		]
	}
};

export const MultiDescriptionCaseWithMixedAttributes: Story = {
	args: {
		title: "Diagnóstico principal",
		descriptionData: [{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.DATE_TIME,
			}
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.DATE,
			}
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)",
			dateToShow: {
				date: new Date(),
                format: DateFormat.TIME,
			}
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		},
		{
			description: "Enfermedad causada por COVID-19 (Confirmado)"
		},
		]
	}
};
import type { Meta, StoryObj } from '@storybook/angular';
import { TitleDescriptionListComponent } from './title-description-list.component';


const meta: Meta<TitleDescriptionListComponent> = {
	title: 'Presentation/TitleDescriptionListComponent',
	component: TitleDescriptionListComponent,
	tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<TitleDescriptionListComponent>;

export const SingleDescriptionCase: Story = {
	args: {
		title: "Diagnóstico principal",
		description: ["Enfermedad causada por COVID-19 (Confirmado)"],
        colonsOnTitle: false,
	}
};

export const MultiDescriptionCase: Story = {
	args: {
		title: "Otros diagnósticos registrados",
		description: ["Dolor en la boca (Confirmado)", "Diagnostico 2", "Diagnostico 3", "Diagnostico 4"],
        colonsOnTitle: false,
	}
};

export const ColonsOnTitleCase: Story = {
	args: {
		title: "Tensión arterial Max",
		description: ["125"],
        colonsOnTitle: true,
	}
};
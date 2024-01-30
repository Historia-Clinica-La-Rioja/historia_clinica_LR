import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { TypeaheadV2Component } from './typeahead-v2.component';

const meta: Meta<TypeaheadV2Component> = {
	title: 'Presentation/TypeaheadV2Component',
	component: TypeaheadV2Component,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [ReactiveFormsModule],
		}),
	],
};

export default meta;

type Story = StoryObj<TypeaheadV2Component>;

export const clasic: Story = {
	args: {
		options: [
			{
				value: "ejemplos",
				compareValue: "ejemplos",
			},
			{
				value: "para",
				compareValue: "para",
			},
			{
				value: "mostrar",
				compareValue: "mostrar",
			},
			{
				value: "el",
				compareValue: "el",
			},
			{
				value: "1",
				compareValue: "Facu",
			}
		],
		externalSetValue: {
			value: "1",
			compareValue: "Facu",
		},
		titleInput: "Titulo de typeahead"
	}
};

export const externalValue: Story = {
	args: {
		placeholder: "Ejemplo de placeholder...",
		externalSetValue: {
			value: "1",
			compareValue: "FACUNDO",
		},
		titleInput: "Titulo de typeahead",
	},
	argTypes: {
		externalSetValue: {
			action: 'setExternalSetValue',
		},
	},
};


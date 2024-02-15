import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { TypeaheadComponent } from './typeahead.component';

const meta: Meta<TypeaheadComponent> = {
    title: 'Presentation/TypeaheadComponent',
    component: TypeaheadComponent,
    tags: ['autodocs'],
    decorators: [
        moduleMetadata({
            imports: [ReactiveFormsModule],
        }),
    ],
};

export default meta;

type Story = StoryObj<TypeaheadComponent>;

export const full: Story = {
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
                value: "typeahead",
                compareValue: "typeahead",
            }
        ],
        placeholder: "Ejemplo de placeholder...",
        externalSetValue: {
            value: "ejemplos",
            compareValue: "ejemplos",
        },
        titleInput: "Titulo de typeahead"
    }
};


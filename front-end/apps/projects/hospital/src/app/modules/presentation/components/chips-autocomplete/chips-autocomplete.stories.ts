import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { ReactiveFormsModule } from '@angular/forms';
import { ChipsAutocompleteComponent } from './chips-autocomplete.component';

const meta: Meta<ChipsAutocompleteComponent> = {
    title: 'Presentation/ChipsAutocompleteComponent',
    component: ChipsAutocompleteComponent,
    tags: ['autodocs'],
    decorators: [
        moduleMetadata({
            imports: [ReactiveFormsModule],
        }),
    ],
};

export default meta;

type Story = StoryObj<ChipsAutocompleteComponent>;


export const full: Story = {
    args: {
        options: [
            {
                value: "ejemplos",
                compareValue: "ejemplos",
                identifier: 1
            },
            {
                value: "para",
                compareValue: "para",
                identifier: 2
            },
            {
                value: "mostrar",
                compareValue: "mostrar",
                identifier: 3
            },
            {
                value: "los",
                compareValue: "los",
                identifier: 4
            },
            {
                value: "chips",
                compareValue: "chips",
                identifier: 5
            }
        ],
        placeholder: "Ejemplo de placeholder...",
        externalSetValues: [
            {
                value: "ejemplos",
                compareValue: "ejemplos",
                identifier: 1
            },
            {
                value: "para",
                compareValue: "mostrar",
                identifier: 2
            }
        ]
    },
};

export const withoutSelectedOptions: Story = {
    args: {
        options: [
            {
                value: "ejemplos",
                compareValue: "ejemplos",
                identifier: 1
            },
            {
                value: "para",
                compareValue: "para",
                identifier: 2
            },
            {
                value: "mostrar",
                compareValue: "mostrar",
                identifier: 3
            },
            {
                value: "los",
                compareValue: "los",
                identifier: 4
            },
            {
                value: "chips",
                compareValue: "chips",
                identifier: 5
            }
        ],
        placeholder: "Ejemplo de placeholder...",
    },
};


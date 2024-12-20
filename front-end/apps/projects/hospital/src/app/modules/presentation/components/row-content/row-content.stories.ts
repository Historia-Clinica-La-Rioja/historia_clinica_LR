import { Meta } from "@storybook/angular";
import { RowContentComponent } from "./row-content.component";

const meta: Meta<RowContentComponent> = {
    title: 'Presentation/RowContentComponent',
    component: RowContentComponent,
    tags: ['autodocs'],
    parameters: {
      docs: {
          description: {
              component: "La buena practica de este componente implica generar un div para cada columna para que en los casos en que la misma se oculte, no se pierda la estructura de las columnas."
          }
      }
  },

};
 
export default meta;

export const WithMinWidthStyle: Meta<RowContentComponent> = {
    title: 'Presentation/RowContentComponent',
    component: RowContentComponent,
    tags: ['autodocs'],
    render: () => {
        return {
            template: `

            <style>
              .custom-class {
                min-width: 350px;
                background-color:#FEFFCB
              }
            </style>

            <app-row-content>
                <span style="min-width: 300px; background-color:#FECBFF">min-width: 300px.</span>
                <span style="min-width: 200px ; background-color:#D4FFCB">min-width: 200px.</span>
                <span class="custom-class">custom class (350px) </span>
            </app-row-content>
          `
        };
    },
};

export const WithoutMinWidthStyle = {
    render: () => {
        return {
            template: `
            <app-row-content>
                <span>I have read the terms and conditions. </span>
                <span>I accept the privacy policy. </span>
                <span>I don't accept the privacy policy. </span>
            </app-row-content>
          `
        };
    },
};
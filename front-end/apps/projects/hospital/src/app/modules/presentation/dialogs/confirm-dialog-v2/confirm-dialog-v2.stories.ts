import { Meta, StoryObj } from "@storybook/angular";
import { OpenStorybookDialogComponent } from "@presentation/components/open-storybook-dialog/open-storybook-dialog.component";
import { DialogWidth } from "@presentation/components/dialog-presentation/dialog-presentation.component";

const meta: Meta<OpenStorybookDialogComponent> = {
    title: 'Presentation/ConfirmDialogV2Component',
    component: OpenStorybookDialogComponent,
    tags: ['autodocs'],
}

export default meta;

type Story = StoryObj<OpenStorybookDialogComponent>;

export const completeLargeConfirmDialog: Story = {
    args: {
        dialogData: {
            dialogWidth: DialogWidth.LARGE,
            title: 'Ejemplo de dialogo completo con todos los campos posibles',
            hasIcon: true,
            content: 'Este ejemplo contiene todas las modificaciones posibles para este dialogo',
            contentBold: '¿Esta seguro que desea seguir con este paso?',
            okButtonLabel: 'Deseo continuar al siguiente paso',
            cancelButtonLabel: 'No deseo continuar',
            color: 'accent',
            buttonClose: true
        }
    }
}

export const simpleMediumConfirmDialog: Story = {
    args: {
        dialogData: {
            dialogWidth: DialogWidth.MEDIUM,
            hasIcon: true,
            content: 'Ejemplo de dialogo simple y tamaño mediano'
        }
    }
}

export const warningSmallConfirmDialog: Story = {
    args: {
        dialogData: {
            dialogWidth: DialogWidth.SMALL,
            hasIcon: true,
            content: 'Ejemplo de dialogo tipo warning',
            contentBold: 'Mensaje resaltado',
            color: 'warn'
        }
    }
}
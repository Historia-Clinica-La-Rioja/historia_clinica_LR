import React from 'react';
import {
    Show,
    SimpleShowLayout,
    TextField,
    ReferenceManyField,
    Datagrid,
    BooleanField,
    DateField,
} from 'react-admin';

const PersonShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="firstName" />
            <TextField source="lastName" />
            
            <ReferenceManyField label="Usuario" reference="users" target="personId">
                <Datagrid rowClick="edit">

                    <TextField source="username" />
                    <BooleanField source="enable" />
                    <DateField source="lastLogin" />
                </Datagrid>
            </ReferenceManyField>

            <ReferenceManyField label="Profesional" reference="healthcareprofessionals" target="personId">
                <Datagrid rowClick="edit">
                    <TextField source="licenseNumber" />
                </Datagrid>
            </ReferenceManyField>

        </SimpleShowLayout>
    </Show>
);

export default PersonShow;

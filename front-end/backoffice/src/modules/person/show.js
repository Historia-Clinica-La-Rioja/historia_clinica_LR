import React from 'react';
import {
    Show,
    TextField,
    ReferenceManyField,
    Datagrid,
    BooleanField,
    DateField,
    TabbedShowLayout,
    Tab,
} from 'react-admin';

const PersonShow = props => (
    <Show {...props}>
        <TabbedShowLayout>
            <Tab label="Informacion personal">
                <TextField source="firstName" />
                <TextField source="lastName" />
                <TextField source="identificationNumber" />
            </Tab>

            <Tab label="Usuario">
                <ReferenceManyField label={false} reference="users" target="personId">
                    <Datagrid rowClick="edit">
                        <TextField source="username" />
                        <BooleanField source="enable" />
                        <DateField source="lastLogin" />
                    </Datagrid>
                </ReferenceManyField>
            </Tab>
            
            <Tab label="Profesiones">
                <ReferenceManyField label="Profesional" reference="healthcareprofessionals" target="personId">
                    <Datagrid rowClick="edit">
                        <TextField source="licenseNumber" />
                    </Datagrid>
                </ReferenceManyField>
            </Tab>

            <Tab label="Especialidad">

            </Tab>

        </TabbedShowLayout>
    </Show>
);

export default PersonShow;

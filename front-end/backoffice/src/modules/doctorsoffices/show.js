import React from 'react';
import {ReferenceField, Show, SimpleShowLayout, TextField} from 'react-admin';

const DoctorsOfficeShow = ({ permissions, ...props }) => (
    <Show  {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors" link="show">
                <TextField source="description"/>
            </ReferenceField>

            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>

            { permissions && permissions.isOn("HABILITAR_LLAMADO") && <TextField source="topic"/> }

        </SimpleShowLayout>
    </Show>
);

export default DoctorsOfficeShow;

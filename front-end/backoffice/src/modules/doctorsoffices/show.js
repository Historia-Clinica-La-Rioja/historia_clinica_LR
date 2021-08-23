import React from 'react';
import {ReferenceField, Show, SimpleShowLayout, TextField} from 'react-admin';

const DoctorsOfficeShow = props => (
    <Show  {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors" link="show">
                <TextField source="description"/>
            </ReferenceField>

            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>

            <TextField source="topic" />
        </SimpleShowLayout>
    </Show>
);

export default DoctorsOfficeShow;

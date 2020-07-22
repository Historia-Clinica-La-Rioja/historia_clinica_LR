import React from 'react';
import {ReferenceField, Show, SimpleShowLayout, TextField} from 'react-admin';

const DoctorsOfficeShow = props => (
    <Show  {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <TextField
                source="openingTime"
                label="resources.doctorsoffices.fields.openingTime"
                type="time"
            />
            <TextField
                source="closingTime"
                label="resources.doctorsoffices.fields.closingTime"
                type="time"
            />
            <ReferenceField source="clinicalSpecialtySectorId" reference="clinicalspecialtysectors" link="show">
                <TextField source="description"/>
            </ReferenceField>

            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
        </SimpleShowLayout>
    </Show>
);

export default DoctorsOfficeShow;

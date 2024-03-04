import React from 'react';
import {
    ReferenceField,
    Show,
    SimpleShowLayout,
    TextField
} from 'react-admin';

const InstitutionUserPersonsShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
            <TextField source="completeName"/>
            <TextField source="completeLastName"/>
            <TextField source="identificationNumber"/>
        </SimpleShowLayout>
    </Show>
);

export default InstitutionUserPersonsShow;

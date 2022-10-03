import React from 'react';
import {
    Edit,
    ReferenceField,
    SimpleForm,
    TextField,
} from 'react-admin';

const CareLineInstitutionEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show">
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>
            <ReferenceField source="careLineId" reference="carelines">
                <TextField source="description" />
            </ReferenceField>
        </SimpleForm>
    </Edit>
);

export default CareLineInstitutionEdit;

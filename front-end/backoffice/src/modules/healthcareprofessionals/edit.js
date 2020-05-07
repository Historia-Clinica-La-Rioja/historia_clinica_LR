import React from 'react';
import {
    TextInput,
    BooleanInput,
    Edit,
    SimpleForm,
    required
} from 'react-admin';

import PeopleReferenceInput from '../people/PeopleReferenceInput';

const HealthcareProfessionalEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <PeopleReferenceInput source="personId" validate={[required()]} />
            <TextInput source="licenseNumber" validate={[required()]} />
            <BooleanInput source="isMedicalDoctor" />
        </SimpleForm>
    </Edit>
);

export default HealthcareProfessionalEdit;

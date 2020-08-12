import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';

import PeopleReferenceInput from '../people/PeopleReferenceInput';

const HealthcareProfessionalCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <PeopleReferenceInput source="personId" validate={[required()]} />
            <TextInput source="licenseNumber" validate={[required()]} />
        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalCreate;

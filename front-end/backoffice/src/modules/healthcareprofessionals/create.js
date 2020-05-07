import React from 'react';
import {
    TextInput,
    BooleanInput,
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
            <BooleanInput source="isMedicalDoctor" defaultValue={true}/>
        </SimpleForm>
    </Create>
);

export default HealthcareProfessionalCreate;

import React from 'react';
import {
    List,
    Datagrid,
    BooleanField,
    TextField,
} from 'react-admin';

import PeopleReferenceField from '../people/PeopleReferenceField';

const HealthcareProfessionalList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="licenseNumber" />
            <PeopleReferenceField source="personId" />
            <BooleanField source="isMedicalDoctor" />
        </Datagrid>
    </List>
);

export default HealthcareProfessionalList;


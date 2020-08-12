import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

import PeopleReferenceField from '../people/PeopleReferenceField';

const HealthcareProfessionalList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="licenseNumber" />
            <PeopleReferenceField source="personId" />
        </Datagrid>
    </List>
);

export default HealthcareProfessionalList;


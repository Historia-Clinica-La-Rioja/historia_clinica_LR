import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

import PeopleReferenceField from '../people/PeopleReferenceField';

const HealthcareProfessionalFilter = (props) => (
    <Filter {...props}>
        <TextInput source="licenseNumber" />
    </Filter>
);

const HealthcareProfessionalList = props => (
    <List {...props} filters={<HealthcareProfessionalFilter />}>
        <Datagrid rowClick="show">
            <TextField source="licenseNumber" />
            <PeopleReferenceField source="personId" />
        </Datagrid>
    </List>
);

export default HealthcareProfessionalList;


import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

import PersonReferenceField from '../person/PersonReferenceField';

const HealthcareProfessionalFilter = (props) => (
    <Filter {...props}>
        <TextInput source="licenseNumber" />
    </Filter>
);

const HealthcareProfessionalList = props => (
    <List {...props} filters={<HealthcareProfessionalFilter />}>
        <Datagrid rowClick="show">
            <TextField source="licenseNumber" />
            <PersonReferenceField source="personId" />
        </Datagrid>
    </List>
);

export default HealthcareProfessionalList;


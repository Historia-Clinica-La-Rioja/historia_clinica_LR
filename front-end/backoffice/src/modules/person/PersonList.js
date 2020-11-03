import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    ReferenceField,
    DateField,
    Filter,
    TextInput,
} from 'react-admin';
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';

const PersonFilter = props =>(
    <Filter {...props}>
        <TextInput source="firstName" />
        <TextInput source="lastName" />
        <SgxSelectInput source="identificationTypeId" element="identificationTypes" optionText="description" allowEmpty={false} />
        <TextInput source="identificationNumber" />
        <SgxSelectInput source="genderId" element="genders" optionText="description" allowEmpty={false} />
    </Filter>
);

export const PersonList = props => (
    <List {...props} filters={<PersonFilter />}>
        <Datagrid rowClick="show">
            <TextField source="firstName" />
            <TextField source="lastName" />
            <ReferenceField source="identificationTypeId" reference="identificationTypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="identificationNumber" />
            <ReferenceField source="genderId" reference="genders" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <DateField source="birthDate" />
        </Datagrid>
    </List>
);
import React from 'react';
import {Datagrid, Filter, List, ReferenceField, TextField, TextInput,} from 'react-admin';
import SgxSelectInput from '../../sgxSelectInput/SgxSelectInput';
import SgxDateField from "../../dateComponents/sgxDateField";

const PersonFilter = props =>(
    <Filter {...props}>
        <TextInput source="firstName" />
        <TextInput source="lastName" />
        <SgxSelectInput source="identificationTypeId" element="identificationTypes" optionText="description" allowEmpty={false} />
        <TextInput source="identificationNumber" />
        <SgxSelectInput source="genderId" element="genders" optionText="description" allowEmpty={false} />
    </Filter>
);

const PersonList = props => (
    <List {...props} filters={<PersonFilter />} bulkActionButtons={false}>
        <Datagrid rowClick="show">
            <TextField source="firstName" />
            <TextField source="lastName" />
            <ReferenceField source="identificationTypeId" reference="identificationTypes" link={false} sortable={false}>
                <TextField source="description" />
            </ReferenceField>
            <TextField source="identificationNumber" />
            <ReferenceField source="genderId" reference="genders" link={false} sortable={false}>
                <TextField source="description" />
            </ReferenceField>
            <SgxDateField source="birthDate" />
        </Datagrid>
    </List>
);

export default PersonList;
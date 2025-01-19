import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput, 
    ReferenceField
} from 'react-admin';
import { SgxSelectInput } from '../../components';

const InstitutionFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name" />
        <TextInput source="sisaCode"/>
        <SgxSelectInput source="provinceId" element={"provinces"} optionText="description"/>
        <SgxSelectInput source="dependencyId" element={"dependencies"} optionText="description"/>
    </Filter>
);

const InstitutionPrescriptionList = props => (
    <List {...props} filters={<InstitutionFilter />} bulkActionButtons={false} exporter={false}>
        <Datagrid rowClick="">
            <TextField source="sisaCode" />
            <TextField source="name" />
            <ReferenceField source="provinceId" reference="provinces" link={false} sortable={false}>
                <TextField source="description"/>
            </ReferenceField>
            <ReferenceField source="dependencyId" reference="dependencies" link={false} sortable={false}>
                <TextField source="description" />
            </ReferenceField>
        </Datagrid>
    </List>
);

export default InstitutionPrescriptionList;


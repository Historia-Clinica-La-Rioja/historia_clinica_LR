import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput, ReferenceField,
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";

const MedicalCoverageFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name"/>
        <TextInput source="cuit"/>
        <SgxSelectInput source="type" element="medicalcoveragetypes" optionText="value" allowEmpty={false} />
    </Filter>
);

const MedicalCoverageList = props => (
    <List {...props} filters={<MedicalCoverageFilter/>}>
        <Datagrid rowClick="show">
            <TextField source="name"/>
            <ReferenceField source="type" reference="medicalcoveragetypes" link={false}>
                <TextField source="value" />
            </ReferenceField>
            <TextField source="cuit"/>
        </Datagrid>
    </List>
);

export default MedicalCoverageList;
import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
} from 'react-admin';

const MedicalCoverageFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name"/>
        <TextInput source="cuit"/>
    </Filter>
);

const MedicalCoverageList = props => (
    <List {...props} filters={<MedicalCoverageFilter/>}>
        <Datagrid rowClick="show">
            <TextField source="name"/>
            <TextField source="cuit"/>
        </Datagrid>
    </List>
);

export default MedicalCoverageList;
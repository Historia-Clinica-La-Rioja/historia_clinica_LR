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
    </Filter>
);

const MedicalCoverageList = props => (
    <List {...props} filters={<MedicalCoverageFilter/>}>
        <Datagrid rowClick="show">
            <TextField source="name"/>
        </Datagrid>
    </List>
);

export default MedicalCoverageList;
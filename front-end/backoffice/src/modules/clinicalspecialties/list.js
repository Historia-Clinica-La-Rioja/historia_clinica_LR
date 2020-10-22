import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

const ClinicalSpecialityFilter = (props) => (
    <Filter {...props}>
        <TextInput source="name" />
        <TextInput source="sctidCode" />
    </Filter>
);

const ClinicalSpecialtyList = props => (
    <List {...props} filters={<ClinicalSpecialityFilter />}>
        <Datagrid rowClick="show">
            <TextField source="name" />
            <TextField source="sctidCode" />
        </Datagrid>
    </List>
);

export default ClinicalSpecialtyList;


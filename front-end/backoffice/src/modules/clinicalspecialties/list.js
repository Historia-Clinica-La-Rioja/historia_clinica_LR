import React from 'react';
import {
    List,
    Datagrid,
    TextField,
} from 'react-admin';

const ClinicalSpecialtyList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="name" />
            <TextField source="description" />
            <TextField source="sctidCode" />
        </Datagrid>
    </List>
);

export default ClinicalSpecialtyList;


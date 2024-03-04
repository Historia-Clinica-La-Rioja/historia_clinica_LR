import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
} from 'react-admin';

const CareLineFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description"/>
    </Filter>
);

const CareLineList = props => (
    <List {...props} filters={<CareLineFilter/>}>
        <Datagrid rowClick="show">
            <TextField source="description"/>
        </Datagrid>
    </List>
);

export default CareLineList;
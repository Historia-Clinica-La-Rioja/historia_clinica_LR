import React from 'react';
import { List, Datagrid, TextField } from 'react-admin';


const WcDefinitionPathList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="name" />
            <TextField source="path" />
        </Datagrid>
    </List>
);

export default WcDefinitionPathList
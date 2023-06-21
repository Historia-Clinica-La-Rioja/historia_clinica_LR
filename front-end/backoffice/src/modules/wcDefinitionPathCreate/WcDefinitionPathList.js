import React from 'react';
import { List, Datagrid, TextField, UrlField } from 'react-admin';


const WcDefinitionPathList = props => (
    <List {...props} >
        <Datagrid rowClick="show">
            <TextField source="name" />
            <UrlField source="path" />
        </Datagrid>
    </List>
);

export default WcDefinitionPathList
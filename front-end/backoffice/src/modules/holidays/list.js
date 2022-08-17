import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput
} from 'react-admin';

const HolidayFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description" />
        <TextInput source="date" />
    </Filter>
);

const HolidayList = props => (
    <List {...props} filters={<HolidayFilter />}>
        <Datagrid rowClick="show">
            <TextField source="description" />
            <TextField source="date" />
        </Datagrid>
    </List>
);

export default HolidayList;
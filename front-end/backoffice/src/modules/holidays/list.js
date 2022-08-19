import React from 'react';
import {
    List,
    Datagrid,
    TextField,
    Filter,
    TextInput,
    DateField
} from 'react-admin';

const HolidayFilter = (props) => (
    <Filter {...props}>
        <TextInput source="description" />
    </Filter>
);

const HolidayList = props => (
    <List {...props} filters={<HolidayFilter />}>
        <Datagrid rowClick="show">
            <TextField source="description" />
            <DateField source="date" locales="es-ES" />
        </Datagrid>
    </List>
);

export default HolidayList;
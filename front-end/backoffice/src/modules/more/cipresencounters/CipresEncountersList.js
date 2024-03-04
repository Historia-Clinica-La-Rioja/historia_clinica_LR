import React from 'react';
import {
    Datagrid,
    List,
    TextField
} from 'react-admin';


const CipresEncountersList = props => {
    return (
        <List {...props} bulkActionButtons={false} hasCreate={false}>
            <Datagrid rowClick={"show"}>
                <TextField source="encounterId" />

                <TextField source="encounterApiId" />

                <TextField source="status" />

                <TextField  source="date" />
            </Datagrid>
        </List>
    );
};

export default CipresEncountersList;

import React from 'react';
import {
    Datagrid,
    List,
    TextField,
    ReferenceField,
    SelectField
} from 'react-admin';


const MoveStudiesList = props => {

    return (
        <List {...props} bulkActionButtons={false} hasCreate={false}>
            <Datagrid rowClick={"show"}>

                <ReferenceField link={false} source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <TextField source="imageId" />

                <SelectField source="status" choices={[
                    { id: 'PENDING', name: 'resources.movestudies.pending' },
                    { id: 'FINISHED', name: 'resources.movestudies.finished' },
                    { id: 'MOVING', name: 'resources.movestudies.moving' }
                ]} />
                <TextField source="result" />
                <TextField source="sizeImage" />

                <ReferenceField link={false} source="pacServerId"  reference="pacservers">
                    <TextField  source="name" />
                </ReferenceField>
                <ReferenceField link={false}source="orchestratorId" reference="orchestrator">
                    <TextField  source="name" />
                </ReferenceField>
            </Datagrid>
        </List>
    );
};

export default MoveStudiesList;

import React from 'react';
import {
    Datagrid,
    List,
    TextField,
    ReferenceField,
    SelectField,
    DateField,
    TextInput,
    Filter,
    SelectInput
} from 'react-admin';
import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';
const StudyFilter = props =>(
    <Filter {...props}>
        <TextInput source="imageId" />
    </Filter>
);

const MoveStudiesList = props => {

    return (
        <List {...props} bulkActionButtons={false} filters={<StudyFilter />} hasCreate={false}>
            <Datagrid rowClick={"show"}>

                <ReferenceField link={false} source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <DateField source="beginOfMove" showTime options={{ year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit', second: '2-digit' }}/>
                <TextField source="imageId" />

                <SelectField source="status" choices={[
                    { id: 'PENDING', name: 'resources.movestudies.pending' },
                    { id: 'FINISHED', name: 'resources.movestudies.finished' },
                    { id: 'MOVING', name: 'resources.movestudies.moving' },
                    { id: 'FAILED', name: 'resources.movestudies.failed' }
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

import React from 'react';
import {
    Datagrid,
    List,
    TextField,
    ReferenceField,
    SelectField,
    TextInput,
    Filter,
    SelectInput
} from 'react-admin';
import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';
import SgxDateField from '../../../dateComponents/sgxDateField';

const StudyFilter = props =>(
    <Filter {...props}>
        <TextInput source="imageId" />
        <SelectInput source="status" choices={[
                    { id: 'PENDING', name: 'resources.movestudies.pending' },
                    { id: 'FINISHED', name: 'resources.movestudies.finished' },
                    { id: 'MOVING', name: 'resources.movestudies.moving' },
                    { id: 'FAILED', name: 'resources.movestudies.failed' }
                ]}
                emptyOption={false}/>
        <SgxSelectInput source="institutionId" element="institutions" optionText="name" allowEmpty={false} />
        <TextInput source="result" />
        <TextInput source="firstName" />
        <TextInput source="lastName" />
        <TextInput source="identificationNumber" />
    </Filter>
);

const MoveStudiesList = props => {

    return (
        <List {...props} bulkActionButtons={false} filters={<StudyFilter />} hasCreate={false}>
            <Datagrid rowClick={"show"}>

                <ReferenceField link={false} source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>

                <TextField source="identificationNumber" />
                <TextField source="firstName" />
                <TextField source="lastName" />
                <SgxDateField source="appoinmentDate"/>
                <TextField source="appoinmentTime" type="time"/>
                <TextField source="imageId" />
                <SelectField source="status" choices={[
                                { id: 'PENDING', name: 'resources.allmovestudies.pending' },
                                { id: 'FINISHED', name: 'resources.allmovestudies.finished' },
                                { id: 'MOVING', name: 'resources.allmovestudies.moving' },
                                { id: 'FAILED', name: 'resources.allmovestudies.failed' }
                           ]} />
                <TextField source="result" />
                <TextField source="acronym" />

            </Datagrid>
        </List>
    );
};

export default MoveStudiesList;

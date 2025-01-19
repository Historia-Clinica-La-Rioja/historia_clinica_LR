import React from 'react';
import {
    SelectInput,
    Edit,
    ReferenceInput,
    SimpleForm,
    TextInput,
    ReferenceManyField,
    Datagrid,
    TextField,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import SgxDateField from '../../../dateComponents/sgxDateField';
import SgxDateInput from '../../../dateComponents/sgxDateInput';

const MoveStudiesEdit = props => {

    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar  />}>
                <ReferenceInput link={false} source="institutionId" reference="institutions" disabled>
                    <SelectInput optionText="name" optionValue="id" />
                </ReferenceInput>
                <TextInput source="identificationNumber" disabled/>
                <TextInput source="firstName" disabled/>
                <TextInput source="lastName" disabled/>
                <SgxDateInput  disabled source="appoinmentDate"/>
                <TextInput source="appoinmentTime" type="time" disabled/>
                <TextInput source="imageId"/>
                <TextInput source="result" disabled/>
                <SelectInput source="status" choices={[
                    { id: 'PENDING', name: 'resources.allmovestudies.pending' },
                    { id: 'FINISHED', name: 'resources.allmovestudies.finished' },
                    { id: 'MOVING', name: 'resources.allmovestudies.moving' },
                    { id: 'FAILED', name: 'resources.allmovestudies.failed' }
                ]} />

                <ReferenceManyField
                        reference="resultstudies"
                        target="idMove"
                        source="id"
                        label="Posibles estudios"
                        {...props}
                >
                        <Datagrid>
                            <TextField source="patientId" sortable={false}/>
                            <TextField source="patientName" sortable={false}/>
                            <SgxDateField source="studyDate" />
                            <TextField source="studyTime" sortable={false} />
                            <TextField source="studyInstanceUid" />
                            <TextField source="modality"/>
                        </Datagrid>
                </ReferenceManyField>

            </SimpleForm>

        </Edit>
    )
};

export default MoveStudiesEdit;
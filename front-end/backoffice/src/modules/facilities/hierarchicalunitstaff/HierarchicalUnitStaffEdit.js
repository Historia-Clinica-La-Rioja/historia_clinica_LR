import React from 'react';
import {
    Edit,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    BooleanInput
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { personInputData } from './HierarchicalUnitStaffCreate';


const goBack = () => {
    window.history.back();
}

const HierarchicalUnitStaffEdit = props => {
    return (
        <Edit {...props}>
            <SimpleForm redirect={goBack} toolbar={<CustomToolbar isEdit={true}/>}>
                <ReferenceInput
                    source="hierarchicalUnitId"
                    reference="hierarchicalunits"
                >
                    <AutocompleteInput optionText="alias" optionValue="id" options={{ disabled: true }}/>
                </ReferenceInput>
                <ReferenceInput
                    source="userId"
                    reference="institutionuserpersons"
                    label="resources.hierarchicalunitstaff.fields.userId"
                >
                    <AutocompleteInput optionText={personInputData} optionValue="id" options={{ disabled: true }}/>
                </ReferenceInput>
                <BooleanInput source="responsible" disabled={false}/>
            </SimpleForm>
        </Edit>
    );
}

export default HierarchicalUnitStaffEdit;

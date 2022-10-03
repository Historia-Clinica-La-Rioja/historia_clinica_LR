import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput, ReferenceField, TextField,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import SectionTitle from "../components/SectionTitle";

const CareLineInstitutionPracticeCreate = props => {
    const institutionId = props?.location?.state?.record?.institutionId;
    const redirect = `/carelineinstitution/${props?.location?.state?.record?.careLineInstitutionId}/show`;
    return(
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description" />
                </ReferenceField>

                <SectionTitle label="Nueva Practica"/>
                <ReferenceInput
                    source="snomedRelatedGroupId"
                    reference="practicesinstitution"
                    sort={{ field: 'description', order: 'ASC' }}
                    filterToQuery={searchText => ({institutionId: institutionId})}
                >
                    <AutocompleteInput optionText="description" optionValue="id" resettable />
                </ReferenceInput>

            </SimpleForm>
        </Create>
    );
};

export default CareLineInstitutionPracticeCreate;

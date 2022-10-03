import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput, ReferenceField, TextField,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import SectionTitle from "../components/SectionTitle";

const CareLineInstitutionSpecialtyCreate = props => {
    const institutionId = props?.location?.state?.record?.institutionId;
    const careLineId = props?.location?.state?.record?.careLineId;
    const redirect = `/carelineinstitution/${props?.location?.state?.record?.careLineInstitutionId}/show`;
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description" />
                </ReferenceField>

                <SectionTitle label="Nueva Especialidad"/>
                <ReferenceInput
                    source="clinicalSpecialtyId"
                    reference="carelinespecialtyinstitution"
                    filterToQuery={searchText => ({careLineId: careLineId, institutionId: institutionId})}
                >
                    <AutocompleteInput optionText="description" optionValue="id" resettable />
                </ReferenceInput>

            </SimpleForm>
        </Create>
    );
};

export default CareLineInstitutionSpecialtyCreate;

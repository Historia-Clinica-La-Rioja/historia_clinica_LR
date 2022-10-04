import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    ReferenceField,
    TextField,
    usePermissions,
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';
import SectionTitle from "../components/SectionTitle";
import {ADMINISTRADOR, ROOT} from "../roles";

const CareLineInstitutionSpecialtyCreate = props => {
    const institutionId = props?.location?.state?.record?.institutionId;
    const careLineId = props?.location?.state?.record?.careLineId;
    const redirect = `/carelineinstitution/${props?.location?.state?.record?.careLineInstitutionId}/show`;
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar/>}>

                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name" />
                </ReferenceField>
                <ReferenceField source="careLineId" reference="carelines">
                    <TextField source="description" />
                </ReferenceField>

                <SectionTitle label="resources.carelineinstitutionspecialty.fields.newspecialty"/>
                <ReferenceInput
                    source="clinicalSpecialtyId"
                    reference="carelinespecialtyinstitution"
                    filterToQuery={searchText => ({careLineId: careLineId, institutionId: institutionId})}
                >
                    <AutocompleteInput optionText="description" optionValue="id" disabled={userIsRootOrAdmin} resettable />
                </ReferenceInput>

            </SimpleForm>
        </Create>
    );
};

export default CareLineInstitutionSpecialtyCreate;

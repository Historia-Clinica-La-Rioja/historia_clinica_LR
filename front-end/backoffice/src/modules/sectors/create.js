import React from 'react';
import {
    AutocompleteInput,
    Create,
    FormDataConsumer,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    TextInput
} from 'react-admin';
import CustomToolbar from "../../modules/components/CustomToolbar";

const INTERNACION = 2;
const CUIDADOS_PROGRESIVOS = 2;

const redirect = (basePath, id, data) => `/institutions/${data.institutionId}/show`;

const SectorType = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="sectortypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);

};


const AgeGroups = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return ( <ReferenceInput
        {...rest}
        reference="agegroups"
        sort={{ field: 'description', order: 'ASC' }}
        perPage={10}
    >
        <SelectInput optionText="description" optionValue="id" />
    </ReferenceInput> );
};


const SectorOrganization = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return (
        <ReferenceInput
            {...rest}
            reference="sectororganizations"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const CareType = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return (
        <ReferenceInput
            {...rest}
            reference="caretypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const HospitalizationType = ({ formData, ...rest }) => {
    if (formData.sectorTypeId !== INTERNACION) return null;
    return (
        <ReferenceInput
            {...rest}
            reference="hospitalizationtypes"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const Sector = (sourceId) => {
    return (
        <ReferenceInput
            {...sourceId}
            reference="sectors"
            perPage={100}
            sort={{ field: 'description', order: 'ASC' }}
        >
            <SelectInput optionText="description" optionValue="id" />
        </ReferenceInput>);
};

const validateCareType = (value, allValues) =>{
    if (allValues.sectorOrganizationId &&
        allValues.sectorOrganizationId === CUIDADOS_PROGRESIVOS &&
        !allValues.careTypeId) {
            return 'El tipo de cuidado es requerido para esta organización de sector';
    }
    return [];
}

const CareTypeValidations = [validateCareType];

const SectorCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />} >

            <TextInput source="description" validate={[required()]} />
            <ReferenceInput
                source="institutionId"
                reference="institutions"
                sort={{ field: 'name', order: 'ASC' }}
                filterToQuery={searchText => ({name: searchText})}
            >
                <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }}/>
            </ReferenceInput>

            {/*Sector*/}
            <Sector source="parentSectorId"/>

            {/*Sector Type*/}
           <SectorType source="sectorTypeId"/>

            {/*Age Groups*/}
            <FormDataConsumer>
                {formDataProps => ( <AgeGroups {...formDataProps} source="ageGroupId"/>)}
            </FormDataConsumer>
            {/*Sector Organizations*/}
            <FormDataConsumer>
                {formDataProps => ( <SectorOrganization {...formDataProps} source="sectorOrganizationId"/>)}
            </FormDataConsumer>
            {/*Care Type*/}
            <FormDataConsumer>
                {formDataProps => ( <CareType {...formDataProps} source="careTypeId" validate={CareTypeValidations}/>)}
            </FormDataConsumer>
            {/*Hospitalization Type*/}
            <FormDataConsumer>
                {formDataProps => ( <HospitalizationType {...formDataProps} source="hospitalizationTypeId"/>)}
            </FormDataConsumer>

        </SimpleForm>
    </Create>
);

export default SectorCreate;

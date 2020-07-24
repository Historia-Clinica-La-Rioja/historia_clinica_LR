import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";

const DoctorsOfficeEdit = props => (
    <Edit {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <TextInput
                source="openingTime"
                label="resources.doctorsoffices.fields.openingTime"
                type="time"
                validate={[required()]}
            />
            <TextInput
                source="closingTime"
                label="resources.doctorsoffices.fields.closingTime"
                type="time"
                validate={[required()]}
            />

            <SgxSelectInput source="clinicalSpecialtySectorId"
                            element="clinicalspecialtysectors"
                            optionText="description"
                            alwaysOn
                            allowEmpty={false}/>

            <SgxSelectInput source="institutionId"
                            element="institutions"
                            optionText="name"
                            alwaysOn
                            allowEmpty={false}/>
        </SimpleForm>
    </Edit>
);

export default DoctorsOfficeEdit;
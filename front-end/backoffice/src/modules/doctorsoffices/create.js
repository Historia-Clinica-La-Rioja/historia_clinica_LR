import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
} from 'react-admin';
import SgxSelectInput from "../../sgxSelectInput/SgxSelectInput";

const DoctorsOfficeCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" >
            <TextInput source="description" validate={[required()]} />
            <TextInput source="openingTime"
                       label="resources.doctorsoffices.fields.closingTime"
                       type="time"
            />
            <TextInput source="closingTime"
                       label="resources.doctorsoffices.fields.closingTime"
                       type="time"
            />

            <SgxSelectInput source="clinicalSpecialtySectorId"
                            element="clinicalspecialtysectors"
                            optionText="description"
                            alwaysOn
                            allowEmpty={false}/>

            <SgxSelectInput source="institutionId" element="institutions" optionText="name" alwaysOn allowEmpty={false}/>

        </SimpleForm>
    </Create>
);

export default DoctorsOfficeCreate;
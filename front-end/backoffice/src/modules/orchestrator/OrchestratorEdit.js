import React from 'react';
import {
    SelectInput,
    Edit, FormDataConsumer,
    ReferenceInput,
    required,
    SimpleForm,
    TextInput,
    maxLength
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const SectorField = ({formData}) => {
    return   <ReferenceInput
                source="sectorId"
                reference="sectors"
                disabled={true}>
                <SelectInput optionText="description" optionValue="id" />
                </ReferenceInput>
}

const OrchestratorEdit = (props) => {
    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar  />}>
                <TextInput source="name" validate={[required(), maxLength(40)]}/>
                <TextInput source="baseTopic" validate={[required(), maxLength(250)]}/>
                <FormDataConsumer>
                    {formDataProps => (<SectorField {...formDataProps} source="sectorId"  />)}
                </FormDataConsumer>
            </SimpleForm>
        </Edit>
    )
};

export default OrchestratorEdit;
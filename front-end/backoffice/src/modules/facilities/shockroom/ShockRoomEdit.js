import React from 'react';
import {
    Edit, FormDataConsumer,
    ReferenceInput,
    required,
    SimpleForm,
    TextInput,
    usePermissions,
    AutocompleteInput,
} from 'react-admin';
import SgxSelectInput from '../../../sgxSelectInput/SgxSelectInput';
import CustomToolbar from '../../components/CustomToolbar';

const AMBULATORIA = 1;

const SectorField = ({formData}) => {
    return <ReferenceInput 
                source="sectorId"
                reference="sectors"
                perPage={100}
                sort={{ field: 'description', order: 'ASC' }}
                filter={{sectorTypeId: AMBULATORIA, institutionId: formData.institutionId}}
            >
                <AutocompleteInput optionText="description" optionValue="id" allowEmpty={false} options={{ disabled: true }} />
            </ReferenceInput>
}

const ShockRoomEdit = (props) => {
    const { permissions } = usePermissions();
    return (
        <Edit {...props}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true} />}>
                <TextInput source="description" validate={[required()]} />
                <SgxSelectInput source="institutionId"
                    element="institutions"
                    optionText="name"
                    alwaysOn
                    allowEmpty={false}
                    options={{ disabled: true }}/>
                <FormDataConsumer>
                    {formDataProps => (<SectorField {...formDataProps} source="sectorId"/>)}
                </FormDataConsumer>
                {permissions && permissions.isOn('HABILITAR_LLAMADO') && <TextInput source="topic" />}
            </SimpleForm>
        </Edit>
    )
};

export default ShockRoomEdit;
import {
    Create,
    required,
    SimpleForm,
    TextInput,
    usePermissions,
    SelectInput
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";

const EpisodeDocumentTypeCreate = (props) => {
    const {permissions} = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Create {...props} hasCreate={userIsAdminInstitutional}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list">
            <TextInput source="description" label="Tipo de documento" validate={[required()]} />
            <SelectInput source="consentId" label="Tipo de consentimiento" defaultValue={ConsentTypes()[0].id} choices={ConsentTypes()} />
        </SimpleForm>
    </Create>)
};

export const ConsentTypes = () => {
    return [
        {
            id: 1, name: 'Documento regular'
        },
        {
            id: 2, name: 'Consentimiento informado de ingreso',
        },
        {
            id: 3, name: 'Consentimiento informado quirúrgico'
        }
    ]
}

export default EpisodeDocumentTypeCreate;
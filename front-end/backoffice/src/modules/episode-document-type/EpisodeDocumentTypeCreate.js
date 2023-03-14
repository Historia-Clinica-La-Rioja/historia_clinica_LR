import {
    Create,
    required,
    SimpleForm,
    TextInput,
    usePermissions
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";

const EpisodeDocumentTypeCreate = (props) => {
    const {permissions} = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Create {...props} hasCreate={userIsAdminInstitutional}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list">
            <TextInput source="description" label="Tipo de documento" validate={[required()]} />
        </SimpleForm>
    </Create>)
};


export default EpisodeDocumentTypeCreate;
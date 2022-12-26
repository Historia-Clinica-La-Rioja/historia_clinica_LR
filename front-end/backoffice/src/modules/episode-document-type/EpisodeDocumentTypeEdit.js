import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    usePermissions
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";

const EpisodeDocumentTypeEdit = props => {
    const {permissions} = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Edit {...props} hasEdit={userIsAdminInstitutional}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true}/>}>
            <TextInput source="description" validate={[required()]} />
        </SimpleForm>
    </Edit>)
};

export default EpisodeDocumentTypeEdit;
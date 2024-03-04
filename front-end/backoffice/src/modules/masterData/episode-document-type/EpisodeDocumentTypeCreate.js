import { makeStyles } from '@material-ui/core/styles';
import {
    Create,
    required,
    SimpleForm,
    TextInput,
    usePermissions,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import {
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
} from '../../roles';


const EpisodeDocumentTypeCreate = (props) => {
    const {permissions} = usePermissions();
    const classes = RichTextStyles();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (<Create {...props} hasCreate={userIsAdminInstitutional}>
        <SimpleForm toolbar={<CustomToolbar />} redirect="list" className={classes.styles}>
            <TextInput source="description" label="Tipo de documento" validate={[required()]} />
        </SimpleForm>
    </Create>)
};

export const RichTextStyles = makeStyles({
    styles: {
        width: '45%',
    },
});

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
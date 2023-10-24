import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DescriptionIcon from '@material-ui/icons/Description';

import EpisodeDocumentTypeList from './EpisodeDocumentTypeList';
import EpisodeDocumentTypeShow from './EpisodeDocumentTypeShow';
import EpisodeDocumentTypeCreate from './EpisodeDocumentTypeCreate';
import EpisodeDocumentTypeEdit from './EpisodeDocumentTypeEdit';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../roles';

const episodesDocumentTypes = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: EpisodeDocumentTypeShow,
    create: permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0 ? EpisodeDocumentTypeCreate : undefined,
    list: EpisodeDocumentTypeList,
    edit: EpisodeDocumentTypeEdit,
    options: {
        submenu: 'masterData'
    }
});

export default episodesDocumentTypes;

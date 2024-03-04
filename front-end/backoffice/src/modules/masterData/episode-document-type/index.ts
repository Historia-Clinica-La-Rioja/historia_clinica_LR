import DescriptionIcon from '@material-ui/icons/Description';
import SGXPermissions from '../../../libs/sgx/auth/SGXPermissions';
import {
    ADMINISTRADOR,
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
} from '../../roles';
import EpisodeDocumentTypeList from './EpisodeDocumentTypeList';
import EpisodeDocumentTypeShow from './EpisodeDocumentTypeShow';
import EpisodeDocumentTypeCreate from './EpisodeDocumentTypeCreate';
import EpisodeDocumentTypeEdit from './EpisodeDocumentTypeEdit';

const episodesDocumentTypes = (permissions: SGXPermissions) => {
    const hasAccess = permissions.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE, ADMINISTRADOR);
    return {
        icon: DescriptionIcon,
        show: EpisodeDocumentTypeShow,
        create: hasAccess ? EpisodeDocumentTypeCreate : undefined,
        list: hasAccess ? EpisodeDocumentTypeList : undefined,
        edit: EpisodeDocumentTypeEdit,
        options: {
            submenu: 'masterData'
        }
    }
};

export default episodesDocumentTypes;

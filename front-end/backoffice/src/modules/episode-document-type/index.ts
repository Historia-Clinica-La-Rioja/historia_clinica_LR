import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DescriptionIcon from '@material-ui/icons/Description';

import { ADMINISTRADOR, ROOT } from '../roles';
import EpisodeDocumentTypeList from './EpisodeDocumentTypeList';
import EpisodeDocumentTypeShow from './EpisodeDocumentTypeShow';
import EpisodeDocumentTypeCreate from './EpisodeDocumentTypeCreate';
import EpisodeDocumentTypeEdit from './EpisodeDocumentTypeEdit';

const episodesDocumentTypes = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? EpisodeDocumentTypeShow: undefined,
    create: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? EpisodeDocumentTypeCreate : undefined,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? EpisodeDocumentTypeList : undefined,
    edit: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? EpisodeDocumentTypeEdit : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default episodesDocumentTypes;

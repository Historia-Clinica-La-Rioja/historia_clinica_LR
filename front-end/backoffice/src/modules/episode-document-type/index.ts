import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DescriptionIcon from '@material-ui/icons/Description';

import { ADMINISTRADOR, ROOT } from '../roles';
import EpisodeDocumentTypeList from './EpisodeDocumentTypeList';
import EpisodeDocumentTypeShow from './EpisodeDocumentTypeShow';
import EpisodeDocumentTypeCreate from './EpisodeDocumentTypeCreate';

const episodesDocumentTypes = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: EpisodeDocumentTypeShow,
    create: EpisodeDocumentTypeCreate,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? EpisodeDocumentTypeList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default episodesDocumentTypes;

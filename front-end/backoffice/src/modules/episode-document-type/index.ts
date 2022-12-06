import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DescriptionIcon from '@material-ui/icons/Description';

import { ADMINISTRADOR, ROOT } from '../roles';
import EpisodeDocumentTypeList from './EpisodeDocumentTypeList';
import EpisodeDocumentTypeShow from './EpisodeDocumentTypeShow';

const episodesDocumentTypes = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: EpisodeDocumentTypeShow,
    list: permissions.hasAnyAssignment(ROOT, ADMINISTRADOR) ? EpisodeDocumentTypeList : undefined,
    options: {
        submenu: 'masterData'
    }
});

export default episodesDocumentTypes;

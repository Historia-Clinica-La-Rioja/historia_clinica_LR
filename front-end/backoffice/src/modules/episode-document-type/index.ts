import SGXPermissions from '../../libs/sgx/auth/SGXPermissions';

import DescriptionIcon from '@material-ui/icons/Description';

import EpisodeDocumentTypeList from './EpisodeDocumentTypeList';
import EpisodeDocumentTypeShow from './EpisodeDocumentTypeShow';
import EpisodeDocumentTypeCreate from './EpisodeDocumentTypeCreate';
import EpisodeDocumentTypeEdit from './EpisodeDocumentTypeEdit';

const episodesDocumentTypes = (permissions: SGXPermissions) => ({
    icon: DescriptionIcon,
    show: EpisodeDocumentTypeShow,
    create: EpisodeDocumentTypeCreate,
    list: EpisodeDocumentTypeList,
    edit: EpisodeDocumentTypeEdit,
    options: {
        submenu: 'masterData'
    }
});

export default episodesDocumentTypes;

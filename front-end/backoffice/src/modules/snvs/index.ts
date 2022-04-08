import PolicyIcon from '@material-ui/icons/Policy';

import SnvsShow from './SnvsShow';
import SnvsList from './SnvsList';

const snvs = {
    icon: PolicyIcon,
    show: SnvsShow,
    list: SnvsList,
    options: {
        submenu: 'debug'
    }
};

export default snvs;

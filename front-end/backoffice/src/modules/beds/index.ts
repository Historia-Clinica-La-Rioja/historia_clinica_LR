import HotelIcon from '@material-ui/icons/Hotel';

import BedShow from './BedShow';
import BedList from './BedList';
import BedCreate from './BedCreate';
import BedEdit from './BedEdit';

const beds =  {
    icon: HotelIcon,
    show: BedShow,
    list: BedList,
    create: BedCreate,
    edit: BedEdit,
    options: {
        submenu: 'facilities'
    }
};

export default beds;

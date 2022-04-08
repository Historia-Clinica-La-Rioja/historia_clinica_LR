import SectorShow from './SectorShow';
import SectorList from './SectorList';
import SectorCreate from './SectorCreate';
import SectorEdit from './SectorEdit';

const sectors = {
    show: SectorShow,
    list: SectorList,
    create: SectorCreate,
    edit: SectorEdit,
    options: {
        submenu: 'facilities'
    }
};

export default sectors;

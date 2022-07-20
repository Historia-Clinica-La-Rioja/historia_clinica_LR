import SectorCreate from "../sectors/SectorCreate";
import SectorEdit from "../sectors/SectorEdit";
import SectorShow from "../sectors/SectorShow";

const rootSectors = {
    show: SectorShow,
    create: SectorCreate,
    edit: SectorEdit,
    options: {
        submenu: 'facilities'
    }
};

export default rootSectors;
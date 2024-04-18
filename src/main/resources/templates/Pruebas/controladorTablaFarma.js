let dataTable;
let dataTableIsInitialized = false;


const dataTableOptions = {
    columnDefs: [
        { className: "centered", targets: [0, 1, 2, 3, 4, 5, 6] },
        { orderable: false, targets: [6] }
    ],
    pageLength: 3,
    paging: true,
    info: false,
    language: {
        lengthMenu: "Mostrar _MENU_ farmacistas por pagina",
        search: "Buscar"
    }
};
const initDataTable = async () => {
    if (dataTableIsInitialized) {
        dataTable.destroy();
    }
    dataTable = $("#tableFarmacista").DataTable(dataTableOptions);
    dataTableIsInitialized = true;
};

initDataTable();
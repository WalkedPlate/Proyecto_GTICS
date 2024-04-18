let dataTable;
let dataTableIsInitialized = false;


const dataTableOptions = {
    columnDefs: [
        { className: "centered", targets: [0, 1, 2, 3, 4] },
        { orderable: false, targets: [0] }
    ],
    pageLength: 3,
    paging: true,
    info: false,
    language: {
        lengthMenu: "Mostrar _MENU_ medicinas por pagina",
        search: "Buscar"
    }
};
const initDataTable = async () => {
    if (dataTableIsInitialized) {
        dataTable.destroy();
    }
    dataTable = $("#tableMedicinas").DataTable(dataTableOptions);
    dataTableIsInitialized = true;
};

initDataTable();
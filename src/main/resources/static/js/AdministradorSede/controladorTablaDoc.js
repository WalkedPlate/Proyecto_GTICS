let dataTable;
let dataTableIsInitialized = false;
const registrosPorPagina = 3; // Número de registros por página

const dataTableOptions = {
    columnDefs: [
        { className: "centered", targets: [0, 1, 2, 3, 4, 5] }
    ],
    pageLength: 3,
    paging: true,
    info: false,
    language: {
        lengthMenu: "Mostrar _MENU_ doctores por pagina",
        search: "Buscar"
    }
};
const initDataTable = async () => {
    if (dataTableIsInitialized) {
        dataTable.destroy();
    }
    dataTable = $("#tableDoctores").DataTable(dataTableOptions);
    dataTableIsInitialized = true;
};

initDataTable();
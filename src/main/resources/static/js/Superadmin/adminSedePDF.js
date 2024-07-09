let table = new DataTable('#example', {
    pageLength: 5,
    lengthMenu: [5, 10, 25, 50, 100],
    columnDefs: [
        {orderable: false, target: [5, 6]},
    ],
    language: {
        "processing": "Procesando...",
        "lengthMenu": "Mostrar _MENU_ registros",
        "zeroRecords": "No se encontraron resultados",
        "emptyTable": "Ningún dato disponible en esta tabla",
        "infoEmpty": "Mostrando registros del 0 al 0 de un total de 0 registros",
        "infoFiltered": "(filtrado de un total de _MAX_ registros)",
        "search": "Buscar:",
        "loadingRecords": "Cargando...",
        "paginate": {
            "first": "Primero",
            "last": "Último",
            "next": "Siguiente",
            "previous": "Anterior"
        },
        "aria": {
            "sortAscending": ": Activar para ordenar la columna de manera ascendente",
            "sortDescending": ": Activar para ordenar la columna de manera descendente"
        },
        "buttons": {
            "copy": "Copiar",
            "colvis": "Visibilidad",
            "collection": "Colección",
            "colvisRestore": "Restaurar visibilidad",
            "copyKeys": "Presione ctrl o u2318 + C para copiar los datos de la tabla al portapapeles del sistema. <br \/> <br \/> Para cancelar, haga clic en este mensaje o presione escape.",
            "copySuccess": {
                "1": "Copiada 1 fila al portapapeles",
                "_": "Copiadas %ds fila al portapapeles"
            },
            "copyTitle": "Copiar al portapapeles",
            "csv": "CSV",
            "excel": "Excel",
            "pageLength": {
                "-1": "Mostrar todas las filas",
                "_": "Mostrar %d filas"
            },
            "pdf": "PDF",
            "print": "Imprimir",
            "renameState": "Cambiar nombre",
            "updateState": "Actualizar",
            "createState": "Crear Estado",
            "removeAllStates": "Remover Estados",
            "removeState": "Remover",
            "savedStates": "Estados Guardados",
            "stateRestore": "Estado %d"
        },
        "autoFill": {
            "cancel": "Cancelar",
            "fill": "Rellene todas las celdas con <i>%d<\/i>",
            "fillHorizontal": "Rellenar celdas horizontalmente",
            "fillVertical": "Rellenar celdas verticalmente"
        },
        "decimal": ",",
        "searchBuilder": {
            "add": "Añadir condición",
            "button": {
                "0": "Constructor de búsqueda",
                "_": "Constructor de búsqueda (%d)"
            },
            "clearAll": "Borrar todo",
            "condition": "Condición",
            "conditions": {
                "date": {
                    "before": "Antes",
                    "between": "Entre",
                    "empty": "Vacío",
                    "equals": "Igual a",
                    "notBetween": "No entre",
                    "not": "Diferente de",
                    "after": "Después",
                    "notEmpty": "No Vacío"
                },
                "number": {
                    "between": "Entre",
                    "equals": "Igual a",
                    "gt": "Mayor a",
                    "gte": "Mayor o igual a",
                    "lt": "Menor que",
                    "lte": "Menor o igual que",
                    "notBetween": "No entre",
                    "notEmpty": "No vacío",
                    "not": "Diferente de",
                    "empty": "Vacío"
                },
                "string": {
                    "contains": "Contiene",
                    "empty": "Vacío",
                    "endsWith": "Termina en",
                    "equals": "Igual a",
                    "startsWith": "Empieza con",
                    "not": "Diferente de",
                    "notContains": "No Contiene",
                    "notStartsWith": "No empieza con",
                    "notEndsWith": "No termina con",
                    "notEmpty": "No Vacío"
                },
                "array": {
                    "not": "Diferente de",
                    "equals": "Igual",
                    "empty": "Vacío",
                    "contains": "Contiene",
                    "notEmpty": "No Vacío",
                    "without": "Sin"
                }
            },
            "data": "Data",
            "deleteTitle": "Eliminar regla de filtrado",
            "leftTitle": "Criterios anulados",
            "logicAnd": "Y",
            "logicOr": "O",
            "rightTitle": "Criterios de sangría",
            "title": {
                "0": "Constructor de búsqueda",
                "_": "Constructor de búsqueda (%d)"
            },
            "value": "Valor"
        },
        "searchPanes": {
            "clearMessage": "Borrar todo",
            "collapse": {
                "0": "Paneles de búsqueda",
                "_": "Paneles de búsqueda (%d)"
            },
            "count": "{total}",
            "countFiltered": "{shown} ({total})",
            "emptyPanes": "Sin paneles de búsqueda",
            "loadMessage": "Cargando paneles de búsqueda",
            "title": "Filtros Activos - %d",
            "showMessage": "Mostrar Todo",
            "collapseMessage": "Colapsar Todo"
        },
        "select": {
            "cells": {
                "1": "1 celda seleccionada",
                "_": "%d celdas seleccionadas"
            },
            "columns": {
                "1": "1 columna seleccionada",
                "_": "%d columnas seleccionadas"
            },
            "rows": {
                "1": "1 fila seleccionada",
                "_": "%d filas seleccionadas"
            }
        },
        "thousands": ".",
        "datetime": {
            "previous": "Anterior",
            "hours": "Horas",
            "minutes": "Minutos",
            "seconds": "Segundos",
            "unknown": "-",
            "amPm": [
                "AM",
                "PM"
            ],
            "months": {
                "0": "Enero",
                "1": "Febrero",
                "10": "Noviembre",
                "11": "Diciembre",
                "2": "Marzo",
                "3": "Abril",
                "4": "Mayo",
                "5": "Junio",
                "6": "Julio",
                "7": "Agosto",
                "8": "Septiembre",
                "9": "Octubre"
            },
            "weekdays": {
                "0": "Dom",
                "1": "Lun",
                "2": "Mar",
                "4": "Jue",
                "5": "Vie",
                "3": "Mié",
                "6": "Sáb"
            },
            "next": "Próximo"
        },
        "editor": {
            "close": "Cerrar",
            "create": {
                "button": "Nuevo",
                "title": "Crear Nuevo Registro",
                "submit": "Crear"
            },
            "edit": {
                "button": "Editar",
                "title": "Editar Registro",
                "submit": "Actualizar"
            },
            "remove": {
                "button": "Eliminar",
                "title": "Eliminar Registro",
                "submit": "Eliminar",
                "confirm": {
                    "_": "¿Está seguro de que desea eliminar %d filas?",
                    "1": "¿Está seguro de que desea eliminar 1 fila?"
                }
            },
            "error": {
                "system": "Ha ocurrido un error en el sistema (<a target=\"\\\" rel=\"\\ nofollow\" href=\"\\\">Más información&lt;\\\/a&gt;).<\/a>"
            },
            "multi": {
                "title": "Múltiples Valores",
                "restore": "Deshacer Cambios",
                "noMulti": "Este registro puede ser editado individualmente, pero no como parte de un grupo.",
                "info": "Los elementos seleccionados contienen diferentes valores para este registro. Para editar y establecer todos los elementos de este registro con el mismo valor, haga clic o pulse aquí, de lo contrario conservarán sus valores individuales."
            }
        },
        "info": "Mostrando _START_ a _END_ de _TOTAL_ registros",
        "stateRestore": {
            "creationModal": {
                "button": "Crear",
                "name": "Nombre:",
                "order": "Clasificación",
                "paging": "Paginación",
                "select": "Seleccionar",
                "columns": {
                    "search": "Búsqueda de Columna",
                    "visible": "Visibilidad de Columna"
                },
                "title": "Crear Nuevo Estado",
                "toggleLabel": "Incluir:",
                "scroller": "Posición de desplazamiento",
                "search": "Búsqueda",
                "searchBuilder": "Búsqueda avanzada"
            },
            "removeJoiner": "y",
            "removeSubmit": "Eliminar",
            "renameButton": "Cambiar Nombre",
            "duplicateError": "Ya existe un Estado con este nombre.",
            "emptyStates": "No hay Estados guardados",
            "removeTitle": "Remover Estado",
            "renameTitle": "Cambiar Nombre Estado",
            "emptyError": "El nombre no puede estar vacío.",
            "removeConfirm": "¿Seguro que quiere eliminar %s?",
            "removeError": "Error al eliminar el Estado",
            "renameLabel": "Nuevo nombre para %s:"
        },
        "infoThousands": "."
    }
});

// Obtener la fecha actual en formato deseado
let currentDate = new Date().toLocaleDateString();

// Agregar funcionalidad de botones
new DataTable.Buttons(table, {
    buttons: [{
        extend:    'pdfHtml5',
        title:     'Lista de Administradores de sedes',
        orientation: 'landscape',

        customize: function(doc) {

            // Logo
            doc.content.splice(0, 0, {
                margin: [0, 0, 0, 6],
                alignment: 'left',
                image:
                    'data:image/png;base64,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',
                width: 120,
            });

            // Modificar el título
            doc.styles.title = {
                fontSize: 18,
                bold: true,
                alignment: 'center',
            };

            //doc.content[2].margin = [ 100, 20, 100, 0 ] //left, top, right, bottom
            doc.content[2].table.widths = [ '25%',  '10%', '10%', '10%',
                '20%', '25%'];

            // Configurar alineación específica para las columnas
            var columnsToCenter = [1, 2, 3, 4]; // Índices de las columnas que se centrarán

            // Aplicar estilos específicos a las celdas de las columnas centradas
            doc.content[2].table.body.forEach(function(row) {
                columnsToCenter.forEach(function(colIndex) {
                    row[colIndex].alignment = 'center';
                });
            });

            // Cambiar el color del encabezado de la tabla
            let tableHeaderColor = '#5B7DFA';
            doc.styles.tableHeader = {
                fillColor: tableHeaderColor,
                color: 'white',
                alignment: 'center',
                bold: true,
                fontSize: 12,
            };



            // Agregar mensaje de reporte realizado en la parte inferior
            doc.content.push({
                text: 'Reporte realizado el ' + currentDate,
                alignment: 'right',
                margin: [0, 30, 0, 0] // Espaciado [left, top, right, bottom]
            });

        },
        exportOptions: {
            columns: [0,1,2,3,4,5] // columnas que se exportarán
        }
    }]
});

$('#download-pdf').on('click', function() {
    table.button(0).trigger();
});
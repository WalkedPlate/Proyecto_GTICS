const ctx5 = document.getElementById('myChart5')
const nombresMedicamentos = window.listaCantProductoSedeFechaPor3Meses.map(item => item.nombre);
const datosSede = window.listaCantProductoSedeFechaPor3Meses.map(item => item.cantidad);
const sede = window.listaCantProductoSedeFechaPor3Meses[0].sede;
const myChart5 = new Chart(ctx5, {
    type: 'line',
    data: {
        labels: nombresMedicamentos,
        datasets: [
            {
                label: sede,
                data: datosSede,
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75,192,192,0.2)',
                fill: true
            },
        ]
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                position: 'right',
            },
            title: {
                display: true,
                text: 'Medicamentos más solicitados en los ultimos 3 meses'
            }
        }
    }
})

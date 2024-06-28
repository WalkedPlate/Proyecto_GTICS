const ctx3 = document.getElementById('myChart3')
const nombresMedicamentos = window.listaCantProductoSedeFecha.map(item => item.nombre);
const datosSede = window.listaCantProductoSedeFecha.map(item => item.cantidad);
const sede = window.listaCantProductoSedeFecha[0].sede;
const myChart3 = new Chart(ctx3, {
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
                text: 'Medicamentos más solicitados en los ultimos 7 dias'
            }
        }
    }
})

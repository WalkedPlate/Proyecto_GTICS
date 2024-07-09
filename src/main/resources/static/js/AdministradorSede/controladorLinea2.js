const ctx3 = document.getElementById('myChart3')
const nombresMedicamentos2 = window.listaCantProductoSedeFechaPor7Dias.map(item => item.nombre);
const datosSede2 = window.listaCantProductoSedeFechaPor7Dias.map(item => item.cantidad);
const sede2 = window.listaCantProductoSedeFechaPor7Dias[0].sede;
const myChart3 = new Chart(ctx3, {
    type: 'line',
    data: {
        labels: nombresMedicamentos2,
        datasets: [
            {
                label: sede2,
                data: datosSede2,
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

const ctx4 = document.getElementById('myChart4')
const nombresMedicamentos3 = window.listaCantProductoSedeFechaPor15Dias.map(item => item.nombre);
const datosSede3 = window.listaCantProductoSedeFechaPor15Dias.map(item => item.cantidad);
const sede3 = window.listaCantProductoSedeFechaPor15Dias[0].sede;
const myChart4 = new Chart(ctx4, {
    type: 'line',
    data: {
        labels: nombresMedicamentos3,
        datasets: [
            {
                label: sede3,
                data: datosSede3,
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
                text: 'Medicamentos más solicitados en los ultimos 15 dias'
            }
        }
    }
})

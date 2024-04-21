const ctx3 = document.getElementById('myChart3')
const myChart3 = new Chart(ctx3, {
    type: 'line',
    data: {
        labels: ['Cetirizina', 'Ibuprofeno', 'Paracetamol', 'Omeprazol', 'Loratadina', 'Amoxicilina', 'Diazepam'],
        datasets: [
            {
                label: 'San Miguel 1',
                data: [65, 59, 80, 81, 56, 55, 40],
                fill: false,
                borderColor: 'rgb(75, 192, 192)',
                backgroundColor: 'rgba(75,192,192,0.2)',
                fill: true
            },
            {
                label: 'San Miguel 2',
                data: [20, 90, 100, 70, 10, 50, 30],
                fill: false,
                borderColor: 'rgba(255,206,86)',
                backgroundColor: 'rgba(255,206,86,0.2)',
                fill: true
            }
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
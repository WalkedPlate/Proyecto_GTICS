const ctx = document.getElementById('myChart')
const names = ['San Miguel 1' , 'San Miguel 2','San Miguel 3','San Miguel 4','San Miguel 5']
const ages = [24,10,54,51,15]

const myChart = new Chart(ctx , {
    type: 'bar',
    data: {
        labels: names,
        datasets: [{
            data: ages,
            backgroundColor: [
                'rgba(255,99,132,0.2)',
                'rgba(54,162,235,0.2)',
                'rgba(255,206,86,0.2)',
                'rgba(75,192,192,0.2)',
                'rgba(153,202,255,0.2)',
                'rgba(255,159,64,0.2)'
            ],
            borderColor: [
                'rgba(255,99,132,1)',
                'rgba(54,162,235,1)',
                'rgba(255,206,86,1)',
                'rgba(75,192,192,1)',
                'rgba(153,102,255,1)',
                'rgba(255,159,64,1)'
            ],
            borderWidth: 1.5
        }],
    },
    options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: false,
            },
            title: {
                display: true,
                text: 'Transacciones de medicamentos por sede'
            }
        }
    } 
})
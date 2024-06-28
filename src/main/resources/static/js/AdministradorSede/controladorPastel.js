const ctx2 = document.getElementById('myChart2')
var labels = Object.keys(window.listaCantidadProductosSede)
var data = Object.values(window.listaCantidadProductosSede);
const myChart2 = new Chart(ctx2 , {
    type: 'doughnut',
    data: {
        labels: labels,
        datasets: [{
          label: 'My First Dataset',
          data: data,
          backgroundColor: [
            'rgba(255,99,132,0.2)',
            'rgba(54,162,235,0.2)',
            'rgba(255,206,86,0.2)'
          ],
          borderColor: [
            'rgba(255,99,132,1)',
            'rgba(54,162,235,1)',
            'rgba(255,206,86,1)'
        ],
          hoverOffset: 4
        }]
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
                text: 'Medicamentos con poco inventariado'
            }
        }
    }  
})
const ctx2 = document.getElementById('myChart2')
var labels = Object.keys(window.listaCantidadProductosSede)
var data = Object.values(window.listaCantidadProductosSede);

function getRandomColor() {
    const r = Math.floor(Math.random() * 256);
    const g = Math.floor(Math.random() * 256);
    const b = Math.floor(Math.random() * 256);
    return {
        backgroundColor: `rgba(${r}, ${g}, ${b}, 0.2)`,
        borderColor: `rgba(${r}, ${g}, ${b}, 1)`
    };
}
const colors = data.map(() => getRandomColor());

const myChart2 = new Chart(ctx2 , {
    type: 'doughnut',
    data: {
        labels: labels,
        datasets: [{
          label: 'My First Dataset',
          data: data,
          backgroundColor: colors.map(color => color.backgroundColor),
          borderColor: colors.map(color => color.borderColor),
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
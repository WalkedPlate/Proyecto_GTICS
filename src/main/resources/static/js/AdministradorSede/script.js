
const table = document.getElementById('table')
const exampleModal = document.getElementById('modal')

table.addEventListener('click',(e)=>{
  e.stopPropagation();
  console.log(e.target.parent);
})
// cambie table por example
const table = document.getElementById('example')
const exampleModal = document.getElementById('modal')

table.addEventListener('click',(e)=>{
  e.stopPropagation();
  console.log(e.target.parent);
})
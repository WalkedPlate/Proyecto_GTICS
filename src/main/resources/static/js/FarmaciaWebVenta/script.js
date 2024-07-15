'use strict';

// notification toast variables
const notificationToast = document.querySelector('[data-toast]');
const toastCloseBtn = document.querySelector('[data-toast-close]');

// notification toast eventListener
toastCloseBtn.addEventListener('click', function () {
  notificationToast.classList.add('closed');
});


// Chatbot
const chatInput = document.querySelector(".chat-input textarea");
const sendChatBtn = document.querySelector(".chat-input span");
const clearChatBtn = document.querySelector(".clear-chat-btn");
const chatbox = document.querySelector(".chatbox");
const chatbotToggler = document.querySelector(".chatbot-toggler");
const chatbotCloseBtn = document.querySelector(".close-btn");

let userMessage;

const createChatLi = (message, className) => {
  const chatLi = document.createElement("li");
  chatLi.classList.add("chat", className);
  let chatContent = className === "outgoing" ? `<p>${message}</p>` : `<span class="material-symbols-outlined">smart_toy</span><p>${message}</p>`;
  chatLi.innerHTML = chatContent;
  return chatLi;
}

const generateResponse = async () => {
  const API_URL = "/api/gpt";

  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        message: userMessage,
        prompt: "sin usar"
      })
    });
    const data = await response.text();
    return data;
  } catch (error) {
    console.error("Error:", error);
    return "Lo siento, hubo un error al procesar tu mensaje.";
  }
}

const handleChat = async () => {
  userMessage = chatInput.value.trim();
  if (!userMessage) return;

  // Añade el mensaje del usuario al chatbox
  chatbox.appendChild(createChatLi(userMessage, "outgoing"));
  chatInput.value = '';

  // Muestra el mensaje de "Pensando..."
  const thinkingLi = createChatLi("Pensando...", "incoming");
  chatbox.appendChild(thinkingLi);

  // Scroll automático hacia el final del chatbox
  chatbox.scrollTop = chatbox.scrollHeight;

  // Obtiene la respuesta del chatbot
  const responseMessage = await generateResponse();

  // Remueve el mensaje "Pensando..." y añade la respuesta del chatbot
  thinkingLi.remove();
  chatbox.appendChild(createChatLi(responseMessage, "incoming"));

  // Scroll automático hacia el final del chatbox
  chatbox.scrollTop = chatbox.scrollHeight;
}

chatbotCloseBtn.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));
chatbotToggler.addEventListener("click", () => document.body.classList.toggle("show-chatbot"));
sendChatBtn.addEventListener("click", handleChat);

// Enviar mensaje al presionar Enter
chatInput.addEventListener("keydown", (e) => {
  if (e.key === "Enter") {
    e.preventDefault();
    handleChat();
  }
});

// Enviar orden generada por chatbot
const enviarOrden = async (orden) => {
  const API_URL = "/api/orden";

  const requestOptions = {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(orden)
  };

  try {
    const response = await fetch(API_URL, requestOptions);
    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`HTTP error! status: ${response.status}, message: ${errorMessage}`);
    }
    const data = await response.json();
    return data;
  } catch (error) {
    console.error("Error:", error);
    return `Error occurred while fetching response from server: ${error.message}`;
  }
}

// Función para limpiar el chatbox
const clearChatbox = () => {
  chatbox.innerHTML = '';
}

// Listener para el botón de limpiar chat
clearChatBtn.addEventListener("click", clearChatbox);





// mobile menu variables
const mobileMenuOpenBtn = document.querySelectorAll('[data-mobile-menu-open-btn]');
const mobileMenu = document.querySelectorAll('[data-mobile-menu]');
const mobileMenuCloseBtn = document.querySelectorAll('[data-mobile-menu-close-btn]');
const overlay = document.querySelector('[data-overlay]');

for (let i = 0; i < mobileMenuOpenBtn.length; i++) {

  // mobile menu function
  const mobileMenuCloseFunc = function () {
    mobileMenu[i].classList.remove('active');
    overlay.classList.remove('active');
  }

  mobileMenuOpenBtn[i].addEventListener('click', function () {
    mobileMenu[i].classList.add('active');
    overlay.classList.add('active');
  });

  mobileMenuCloseBtn[i].addEventListener('click', mobileMenuCloseFunc);
  overlay.addEventListener('click', mobileMenuCloseFunc);

}





// accordion variables
const accordionBtn = document.querySelectorAll('[data-accordion-btn]');
const accordion = document.querySelectorAll('[data-accordion]');

for (let i = 0; i < accordionBtn.length; i++) {

  accordionBtn[i].addEventListener('click', function () {

    const clickedBtn = this.nextElementSibling.classList.contains('active');

    for (let i = 0; i < accordion.length; i++) {

      if (clickedBtn) break;

      if (accordion[i].classList.contains('active')) {

        accordion[i].classList.remove('active');
        accordionBtn[i].classList.remove('active');

      }

    }

    this.nextElementSibling.classList.toggle('active');
    this.classList.toggle('active');

  });

}



//carrito
const cards = document.getElementById('cards')
const templateCard = document.getElementById('template-card').content
const items = document.getElementById('items')
const footer = document.getElementById('footer')
const templateFooter = document.getElementById('template-footer').content
const templateCarrito = document.getElementById('template-carrito').content
const fragment = document.createDocumentFragment()
let carrito = {}


document.addEventListener('DOMContentLoaded', ()=>{
      fetchData()
      if(localStorage.getItem('carrito')){
        carrito = JSON.parse(localStorage.getItem('carrito'))
        pintarCarrito()
      }
    }
)

cards.addEventListener('click', e =>{
  addCarrito(e)
})

items.addEventListener('click', e=>{
  btnAccion(e)
})

const fetchData = async()=>{
  try{
    const res = await fetch('productos.json')
    const data = await res.json()
    pintarCard(data)

  }catch(error){
    console.log(error)
  }
}

const pintarCard = data=>{
  data.forEach(item => {
    templateCard.querySelector('h5').textContent = item.title
    templateCard.querySelector('p').textContent = item.precio
    templateCard.querySelector('img').setAttribute('src', item.thumbnailUrl)
    templateCard.querySelector('.btn-dark').dataset.id = item.id
    const clone = templateCard.cloneNode(true)
    fragment.appendChild(clone)
  })
  cards.appendChild(fragment)
}

const addCarrito = e =>{
  //console.log(e.target)
  //console.log(e.target.classList.contains('btn-dark'))
  if(e.target.classList.contains('btn-dark')){
    setCarrito(e.target.parentElement)
  }

  e.stopPropagation()
}



const setCarrito = item => {
  //console.log(objeto)
  const producto = {
    title: item.querySelector('h5').textContent,
    precio: item.querySelector('p').textContent,
    id: item.querySelector('.btn-dark').dataset.id,
    cantidad: 1
  }

  if(carrito.hasOwnProperty(producto.id)){
    producto.cantidad = carrito[producto.id].cantidad + 1
  }

  carrito[producto.id] = { ...producto}
  pintarCarrito()
}

const pintarCarrito = ()=> {

  items.innerHTML = ''
  Object.values(carrito).forEach(producto => {
    templateCarrito.querySelector('th').textContent = producto.id
    templateCarrito.querySelectorAll('td')[0].textContent = producto.title
    templateCarrito.querySelectorAll('td')[1].textContent = producto.cantidad
    templateCarrito.querySelector('.btn-info').dataset.id = producto.id
    templateCarrito.querySelector('.btn-danger').dataset.id = producto.id
    templateCarrito.querySelector('span').textContent = producto.cantidad * producto.precio
    const clone = templateCarrito.cloneNode(true)
    fragment.appendChild(clone)
  })

  items.appendChild(fragment)

  pintarFooter()

  localStorage.setItem('carrito', JSON.stringify(carrito))

}

const pintarFooter = () => {
  footer.innerHTML = ''
  if(Object.keys(carrito).length === 0){
    footer.innerHTML = `
			<th scope="row" colspan="5">Carrito vacío - comience a comprar!</th>
			`
    return
  }

  const nCantidad = Object.values(carrito).reduce((acc, {cantidad})=> acc + cantidad, 0)
  const nPrecio = Object.values(carrito).reduce((acc, {cantidad, precio}) => acc + cantidad * precio, 0)

  templateFooter.querySelectorAll('td')[0].textContent = nCantidad
  templateFooter.querySelector('span').textContent = nPrecio

  const clone = templateFooter.cloneNode(true)
  fragment.appendChild(clone)
  footer.appendChild(fragment)

  const btnVaciar = document.getElementById('vaciar-carrito')
  btnVaciar.addEventListener('click', ()=>{
    carrito = {}
    pintarCarrito()
  })
}


const btnAccion = e =>{

  if(e.target.classList.contains('btn-info')){

    const producto = carrito[e.target.dataset.id]
    producto.cantidad++

    carrito[e.target.dataset.id] = {...producto}
    pintarCarrito()
  }

  if(e.target.classList.contains('btn-danger')){
    const producto = carrito[e.target.dataset.id]
    producto.cantidad--
    if(producto.cantidad ===0){
      delete carrito[e.target.dataset.id]
    }
    pintarCarrito()

  }

  e.stopPropagation()
}
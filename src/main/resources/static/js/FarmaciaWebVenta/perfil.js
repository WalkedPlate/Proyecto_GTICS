// Tomar el valor de lo seleccionado
document.querySelectorAll('.menu-distrito-edit a.item-distrito-edit').forEach(item => {
    item.addEventListener('click', function () {
        let selectedValue = this.textContent.trim();
        let dropdownButton = this.closest('.dropdown-center').querySelector('.dropdown-toggle');
        let hiddenInput = this.closest('.dropdown-center').querySelector('input[type="hidden"]');

        dropdownButton.textContent = selectedValue;
        hiddenInput.value = selectedValue;
    });
});

// Foto de perfil
let profileImage = document.getElementById("profileImage");
let imageUpload = document.getElementById("imageUpload");

imageUpload.onchange = function (){
    profileImage.src = URL.createObjectURL(imageUpload.files[0]);
}

// Evitar que los formularios se envíen
document.getElementById('editProfile').addEventListener('keydown', function (event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // Evitar que el formulario se envíe

    }

});

// Validación en tiempo real de los campos
function validateInput(input, errorElement, validationFn) {
    input.addEventListener('input', function () {
        if (!validationFn(input.value)) {
            errorElement.style.display = 'block';
            input.classList.add('is-invalid');
            input.classList.remove('is-valid');
        } else {
            errorElement.style.display = 'none';
            input.classList.remove('is-invalid');
            input.classList.add('is-valid');
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    // Validar Dirección
    let direccionInput = document.querySelector('input[name="direccion"]');
    let direccionError = document.getElementById("direccionError");
    validateInput(direccionInput, direccionError, function (value) {
        if (!value) {
            direccionError.innerText = 'Por favor, ingrese una dirección.';
            return false;
        } else if (value.length > 99) {
            direccionError.innerText = 'La dirección puede tener como máximo 99 dígitos.';
            return false;
        }
        return true;
    });


    // Validar correo
    let correoInput = document.querySelector('input[name="correo"]');
    let correoError = document.getElementById("correoError");
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    validateInput(correoInput, correoError, function (value) {
        if (!value) {
            correoError.innerText = 'Por favor, ingrese su correo.';
            return false;
        } else if (!emailPattern.test(value)) {
            correoError.innerText = 'El formato del correo no es válido.';
            return false;
        } else if (value.length > 99) {
            correoError.innerText = 'El correo puede tener como máximo 99 dígitos.';
            return false;
        }
        return true;
    });




    // Validar el formulario register Form
    document.getElementById('editProfile').addEventListener('submit', function (event) {
        let isValid = true;


        let direccionInput = document.querySelector('input[name="direccion"]');
        let direccionError = document.getElementById("direccionError");
        if (!direccionInput.value) {
            direccionError.innerText = 'Por favor, ingrese una dirección.';
            direccionError.style.display = 'block';
            direccionInput.classList.add('is-invalid');
            direccionInput.classList.remove('is-valid');
            isValid = false;
        } else if (direccionInput.value.length > 99) {
            direccionError.innerText = 'La dirección puede tener como máximo 99 dígitos.';
            direccionError.style.display = 'block';
            direccionInput.classList.add('is-invalid');
            direccionInput.classList.remove('is-valid');
            isValid = false;
        } else {
            direccionError.style.display = 'none';
            direccionInput.classList.remove('is-invalid');
            direccionInput.classList.add('is-valid');
        }

        let correoInput = document.querySelector('input[name="correo"]');
        let correoError = document.getElementById("correoError");
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!correoInput.value) {
            correoError.innerText = 'Por favor, ingrese su correo.';
            correoError.style.display = 'block';
            correoInput.classList.add('is-invalid');
            correoInput.classList.remove('is-valid');
            isValid = false;
        } else if (!emailPattern.test(correoInput.value)) {
            correoError.innerText = 'El formato del correo no es válido.';
            correoError.style.display = 'block';
            correoInput.classList.add('is-invalid');
            correoInput.classList.remove('is-valid');
            isValid = false;
        } else if (correoInput.value.length > 99) {
            correoError.innerText = 'El correo puede tener como máximo 99 dígitos.';
            correoError.style.display = 'block';
            correoInput.classList.add('is-invalid');
            correoInput.classList.remove('is-valid');
            isValid = false;
        } else {
            correoError.style.display = 'none';
            correoInput.classList.remove('is-invalid');
            correoInput.classList.add('is-valid');
        }

        if (!isValid) {
            event.preventDefault();
        }
    });


});
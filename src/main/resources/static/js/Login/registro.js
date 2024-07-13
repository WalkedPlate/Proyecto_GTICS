//Autocompletar Nombre y Apellidos con el DNI
function autocompletarDni() {
    let dni = document.getElementById("dni").value;
    fetch(`/api/dni?dni=${dni}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === 422 || data.status === 404) {
                document.getElementById("dniError").style.display = 'none';
                document.getElementById("dniError").classList.remove('is-invalid');
                document.getElementById("dniError").innerText = 'DNI no válido.';
                document.getElementById("dniError").style.display = 'block';
            } else {
                document.getElementById("nombre").value = data.data.nombres;
                document.getElementById("apellido").value = `${data.data.apellido_paterno} ${data.data.apellido_materno}`;
            }
        })
        .catch(error => console.error('Error:', error));

}

// JavaScript para permitir solo la entrada de números
document.getElementById("dni").addEventListener("keydown", function (event) {
    if (event.key === "ArrowLeft" || event.key === "ArrowRight" || event.key === "Backspace" || event.key === "Delete") {
        return;
    }
    if (event.key < "0" || event.key > "9") {
        event.preventDefault();
    }
});

document.getElementById('registerForm').addEventListener('keydown', function (event) {
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
        } else {
            errorElement.style.display = 'none';
            input.classList.remove('is-invalid');
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    let dniInput = document.getElementById("dni");
    let dniError = document.getElementById("dniError");

    dniInput.addEventListener('input', function () {
        if (dniInput.value.length === 8) {
            autocompletarDni(dniInput.value);
        }
        if (!dniInput.value) {
            dniError.innerText = 'Por favor, ingrese su DNI.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
        } else if (dniInput.value.length !== 8) {
            dniError.innerText = 'El DNI debe tener 8 dígitos.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
        } else {
            dniError.style.display = 'none';
            dniInput.classList.remove('is-invalid');
        }
    });

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

    document.querySelectorAll('.dropdown-menu a.dropdown-item').forEach(item => {
        item.addEventListener('click', function () {
            let selectedValue = this.textContent.trim();
            document.getElementById('dropdownMenuButton').textContent = selectedValue;
            document.getElementById('distritoResidencia').value = selectedValue;

            let distritoError = document.getElementById("distritoError");
            let distritoContainer = document.querySelector(".dttobtn");
            if (selectedValue) {
                distritoError.style.display = 'none';
                distritoContainer.classList.remove('is-invalid-container');
            }
        });
    });

    document.querySelectorAll('input[name="seguro"]').forEach(radio => {
        radio.addEventListener('change', function () {
            let seguroError = document.getElementById("seguroError");
            let seguroContainer = document.querySelector(".seguro-container");
            if (document.querySelector('input[name="seguro"]:checked')) {
                seguroError.style.display = 'none';
                seguroContainer.classList.remove('is-invalid-container');
            }
        });
    });

    document.getElementById('registerForm').addEventListener('submit', function (event) {
        let isValid = true;

        let dniInput = document.getElementById("dni");
        let dniError = document.getElementById("dniError");
        if (!dniInput.value) {
            dniError.innerText = 'Por favor, ingrese su DNI.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
            isValid = false;
        } else if (dniInput.value.length !== 8) {
            dniError.innerText = 'El DNI debe tener 8 dígitos.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
            isValid = false;
        } else {
            dniError.style.display = 'none';
            dniInput.classList.remove('is-invalid');
        }

        let direccionInput = document.querySelector('input[name="direccion"]');
        let direccionError = document.getElementById("direccionError");
        if (!direccionInput.value) {
            direccionError.innerText = 'Por favor, ingrese una dirección.';
            direccionError.style.display = 'block';
            direccionInput.classList.add('is-invalid');
            isValid = false;
        } else if (direccionInput.value.length > 99) {
            direccionError.innerText = 'La dirección puede tener como máximo 99 dígitos.';
            direccionError.style.display = 'block';
            direccionInput.classList.add('is-invalid');
            isValid = false;
        } else {
            direccionError.style.display = 'none';
            direccionInput.classList.remove('is-invalid');
        }

        let distritoInput = document.getElementById("distritoResidencia");
        let distritoError = document.getElementById("distritoError");
        let distritoContainer = document.querySelector(".dttobtn");
        if (!distritoInput.value) {
            distritoError.innerText = 'Por favor, seleccione un distrito.';
            distritoError.style.display = 'block';
            distritoContainer.classList.add('is-invalid-container');
            isValid = false;
        } else {
            distritoError.style.display = 'none';
            distritoContainer.classList.remove('is-invalid-container');
        }

        let correoInput = document.querySelector('input[name="correo"]');
        let correoError = document.getElementById("correoError");
        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!correoInput.value) {
            correoError.innerText = 'Por favor, ingrese su correo.';
            correoError.style.display = 'block';
            correoInput.classList.add('is-invalid');
            isValid = false;
        } else if (!emailPattern.test(correoInput.value)) {
            correoError.innerText = 'El formato del correo no es válido.';
            correoError.style.display = 'block';
            correoInput.classList.add('is-invalid');
            isValid = false;
        } else if (correoInput.value.length > 99) {
            correoError.innerText = 'El correo puede tener como máximo 99 dígitos.';
            correoError.style.display = 'block';
            correoInput.classList.add('is-invalid');
            isValid = false;
        } else {
            correoError.style.display = 'none';
            correoInput.classList.remove('is-invalid');
        }

        let seguroError = document.getElementById("seguroError");
        let seguroContainer = document.querySelector(".seguro-container");
        if (!document.querySelector('input[name="seguro"]:checked')) {
            seguroError.innerText = 'Por favor, seleccione si tiene seguro.';
            seguroError.style.display = 'block';
            seguroContainer.classList.add('is-invalid-container');
            isValid = false;
        } else {
            seguroError.style.display = 'none';
            seguroContainer.classList.remove('is-invalid-container');
        }

        if (!isValid) {
            event.preventDefault();
        }
    });
});
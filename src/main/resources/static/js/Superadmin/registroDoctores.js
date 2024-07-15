// Script para confirmación de cierre de sesión solo para dashboard superadmin
function confirmLogout() {
    $('#confirmLogoutModal').modal('show');
}

function submitLogout() {
    document.getElementById('logoutForm').submit();
}

//Autocompletar Nombre y Apellidos con el DNI
function autocompletarDni() {

    let dni = document.getElementById("dni").value;

    fetch(`/api/dni?dni=${dni}`)
        .then(response => response.json())
        .then(data => {
            if (data.status === 422 || data.status === 404) {
                alert(data.message);
            } else {
                document.getElementById("nombre").value = `${data.data.nombres} ${data.data.apellido_paterno} ${data.data.apellido_materno}` ;
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

// JavaScript para permitir solo la entrada de números para el código de colegio
document.getElementById("codigoColegio").addEventListener("keydown", function (event) {
    // Permite las teclas de flecha, retroceso y suprimir
    if (event.key === "ArrowLeft" || event.key === "ArrowRight" || event.key === "Backspace" || event.key === "Delete") {
        return;
    }
    // Permite los dígitos numéricos
    if (event.key < "0" || event.key > "9") {
        event.preventDefault();
    }
});


// Evitar que los formularios se envíen
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
            input.classList.remove('is-valid');
        } else {
            errorElement.style.display = 'none';
            input.classList.remove('is-invalid');
            input.classList.add('is-valid');
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    // Validar DNI
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
            dniInput.classList.remove('is-valid');
        } else if (dniInput.value.length !== 8) {
            dniError.innerText = 'El DNI debe tener 8 dígitos.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
            dniInput.classList.remove('is-valid');
        } else {
            dniError.style.display = 'none';
            dniInput.classList.remove('is-invalid');
            dniInput.classList.add('is-valid');
        }
    });


    // Validar Código de colegio
    let codigoInput = document.querySelector('input[name="codigoColegio"]');
    let codigoError = document.getElementById("codigoError");
    validateInput(codigoInput, codigoError, function (value) {
        if (!value) {
            codigoError.innerText = 'Por favor, ingrese el código de colegio.';
            return false;
        } else if (value.length !== 6) {
            codigoError.innerText = 'El código puede tener como máximo 6 dígitos.';
            return false;
        }
        return true;
    });



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

    // Tomar el valor de lo seleccionado
    document.querySelectorAll('.menu-distrito a.item-distrito').forEach(item => {
        item.addEventListener('click', function () {
            let selectedValue = this.textContent.trim();
            document.getElementById('dropdownMenuButton').textContent = selectedValue;
            document.getElementById('distritoResidencia').value = selectedValue;

            let distritoError = document.getElementById("distritoError");
            let distritoContainer = document.querySelector(".dttobtn");
            if (selectedValue) {
                distritoError.style.display = 'none';
                distritoContainer.classList.remove('is-invalid-container');
                distritoContainer.classList.add('is-valid-container');
            } else {
                distritoContainer.classList.remove('is-valid-container');
            }
        });
    });


    document.querySelectorAll('.menu-sede a.item-sede').forEach(item => {
        item.addEventListener('click', function () {
            let selectedValue = this.textContent.trim();
            let selectedId = this.getAttribute('value');
            document.getElementById('sedeAdmin').textContent = selectedValue;
            document.getElementById('sede').value = selectedId;

            let sedeError = document.getElementById("sedeError");
            let sedeContainer = document.querySelector(".sedebtn");
            if (selectedValue) {
                sedeError.style.display = 'none';
                sedeContainer.classList.remove('is-invalid-container');
                sedeContainer.classList.add('is-valid-container');
            } else {
                sedeContainer.classList.remove('is-valid-container');
            }
        });
    });


    // Validar el formulario register Form
    document.getElementById('registerForm').addEventListener('submit', function (event) {
        let isValid = true;

        let dniInput = document.getElementById("dni");
        let dniError = document.getElementById("dniError");
        if (!dniInput.value) {
            dniError.innerText = 'Por favor, ingrese su DNI.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
            dniInput.classList.remove('is-valid');
            isValid = false;
        } else if (dniInput.value.length !== 8) {
            dniError.innerText = 'El DNI debe tener 8 dígitos.';
            dniError.style.display = 'block';
            dniInput.classList.add('is-invalid');
            dniInput.classList.remove('is-valid');
            isValid = false;
        } else {
            dniError.style.display = 'none';
            dniInput.classList.remove('is-invalid');
            dniInput.classList.remove('is-valid');
        }

        let codigoInput = document.querySelector('input[name="codigoColegio"]');
        let codigoError = document.getElementById("codigoError");
        if (!codigoInput.value) {
            codigoError.innerText = 'Por favor, ingrese el código de colegio.';
            codigoError.style.display = 'block';
            codigoInput.classList.add('is-invalid');
            codigoInput.classList.remove('is-valid');
            isValid = false;
        } else if (codigoInput.value.length !== 6) {
            codigoError.innerText = 'El código puede tener 6 dígitos.';
            codigoError.style.display = 'block';
            codigoInput.classList.add('is-invalid');
            codigoInput.classList.remove('is-valid');
            isValid = false;
        } else {
            codigoError.style.display = 'none';
            codigoInput.classList.remove('is-invalid');
            codigoInput.classList.add('is-valid');
        }

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

        let distritoInput = document.getElementById("distritoResidencia");
        let distritoError = document.getElementById("distritoError");
        let distritoContainer = document.querySelector(".dttobtn");
        if (!distritoInput.value) {
            distritoError.innerText = 'Por favor, seleccione un distrito.';
            distritoError.style.display = 'block';
            distritoContainer.classList.add('is-invalid-container');
            distritoContainer.classList.remove('is-valid-container');
            isValid = false;
        } else {
            distritoError.style.display = 'none';
            distritoContainer.classList.remove('is-invalid-container');
            distritoContainer.classList.add('is-valid-container');
        }

        let sedeInput = document.getElementById("sede");
        let sedeError = document.getElementById("sedeError");
        let sedeContainer = document.querySelector(".sedebtn");
        if (!sedeInput.value) {
            sedeError.innerText = 'Por favor, seleccione una sede.';
            sedeError.style.display = 'block';
            sedeContainer.classList.add('is-invalid-container');
            sedeContainer.classList.remove('is-valid-container');
            isValid = false;
        } else {
            sedeError.style.display = 'none';
            sedeContainer.classList.remove('is-invalid-container');
            sedeContainer.classList.add('is-valid-container');
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
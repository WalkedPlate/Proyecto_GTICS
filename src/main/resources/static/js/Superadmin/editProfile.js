const form = document.getElementById('myForm');

form.addEventListener('submit', function(event) {
    event.preventDefault();
    let isValid = true;

    // Reset previous error messages
    //document.getElementById('apellidoError').textContent = '';
    //document.getElementById('correoError').textContent = '';
    //document.getElementById('distritoError').textContent = '';


    // Validate direccion
    const direccionInput = document.getElementById('direccion');
    if (!direccionInput.value.trim()) {
        document.getElementById('direccionError').textContent = 'Por favor, ingresa tu dirección.';
        isValid = false;
    }

    // Validate correo
    const correoInput = document.getElementById('correo');
    const correoRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!correoRegex.test(correoInput.value.trim())) {
        document.getElementById('correoError').textContent = 'Por favor, ingresa un correo electrónico válido.';
        isValid = false;
    }

    // Validate distrito
    const distritoInput = document.getElementById('distrito');
    if (!distritoInput.value.trim()) {
        document.getElementById('distritoError').textContent = 'Por favor, ingresa tu distrito de residencia.';
        isValid = false;
    }

    if (isValid) {
        form.submit();
        //successMessage.textContent = 'Se guardaron los cambios exitosamente';
    }
});
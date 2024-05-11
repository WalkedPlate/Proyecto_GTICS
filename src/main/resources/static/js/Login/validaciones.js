// VALIDACIONES PARA EL INGRESO A LA PÁGINA AL COMPLETAR LOS CAMPOS
document.getElementById('signin-form').addEventListener('submit', function(event) {
    // Detener el envío del formulario
    event.preventDefault();

    // Obtener los valores de los campos de correo electrónico y contraseña
    var email = document.getElementById('signin-email').value;
    var password = document.getElementById('signin-pass').value;

    // Obtener los elementos donde se mostrarán los mensajes de error
    var emailError = document.getElementById('signinemail-error');
    var passwordError = document.getElementById('password-error');

    // Expresión regular para validar el formato de correo electrónico
    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    // Expresión regular para validar el formato de correo electrónico
    if (email.trim() === '') {
        // Mostrar un mensaje de error de campo vacío para el correo electrónico
        emailError.textContent = 'Complete el campo de correo electrónico';
        emailError.style.color = 'red';
        emailError.style.fontSize = '0.8em';
    } else if (!emailPattern.test(email)) {
        // Mostrar un mensaje de error si el correo electrónico no cumple con el formato válido
        emailError.textContent = 'El formato del correo electrónico no es válido';
        emailError.style.color = 'red';
        emailError.style.fontSize = '0.8em';
    } else {
        // Limpiar el mensaje de error si el correo está completo y es válido
        emailError.textContent = '';
    }

    // Validar si el campo de contraseña está vacío
    if (password.trim() === '') {
        // Mostrar un mensaje de error de campo vacío para la contraseña
        passwordError.textContent = 'Complete el campo de contraseña';
        passwordError.style.color = 'red';
        passwordError.style.fontSize = '0.8em';
    } else {
        // Limpiar el mensaje de error si la contraseña está completa
        passwordError.textContent = '';
    }

    // Si ambos campos están completos y el correo electrónico tiene un formato válido, enviar el formulario
    if (email.trim() !== '' && password.trim() !== '' && emailPattern.test(email)) {
        // enviar el formulario
        this.submit();
    }
});


// VALIDACIONES PARA EL ENVÍO DE ENLACE DE RECUPERACIÓN DE CONTRASEÑA
document.getElementById('newpass-form').addEventListener('submit', function(event) {
    // Detener el envío del formulario
    event.preventDefault();

    // Obtener el valor del campo de correo electrónico
    var email = document.getElementById('email').value;

    // Obtener el elemento donde se mostrarán los mensajes de error
    var emailError = document.getElementById('email-error');

    // Validar si el campo de correo electrónico está vacío
    if (email.trim() === '') {
        // Mostrar un mensaje de error de campo vacío
        emailError.textContent = 'Por favor, ingrese su correo electrónico.';
        emailError.style.color = 'red';
        emailError.style.fontSize = '0.8em';
        return; // Detener la ejecución de la función
    }

    // Expresión regular para validar el correo electrónico
    var emailPattern = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

    // Validar si el correo electrónico no cumple con el formato
    if (!emailPattern.test(email)) {
        // Mostrar un mensaje de error de formato de correo electrónico
        emailError.textContent = 'El correo electrónico no es válido.';
        emailError.style.color = 'red';
        emailError.style.fontSize = '0.8em';
        return; // Detener la ejecución de la función
    }

    // Limpiar el mensaje de error si el correo es válido
    emailError.textContent = '';

    // Si la validación es exitosa, mostrar mensaje de éxito
    emailError.textContent = 'Se ha enviado un correo electrónico con el enlace para restablecer tu contraseña';
    emailError.style.color = 'green';
    emailError.style.fontSize = '0.8em';
    // Reiniciar el formulario
    this.reset();
});

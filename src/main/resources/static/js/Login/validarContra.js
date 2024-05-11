document.getElementById('passwordForm').addEventListener('submit', function(event) {
    // Detener el envío del formulario
    event.preventDefault();

    // Obtener los valores de las contraseñas
    var newPassword = document.getElementById('newPassword').value;
    var confirmPassword = document.getElementById('confirmPassword').value;

    // Obtener los elementos donde se mostrarán los mensajes de error
    var newPasswordError = document.getElementById('newPasswordError');
    var confirmPasswordError = document.getElementById('confirmPasswordError');

    // Expresión regular para validar la contraseña
    var passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+}{"':;?/>.<,])[A-Za-z\d!@#$%^&*()_+}{"':;?/>.<,]{8,}$/;

    // Validar si la nueva contraseña cumple con los requisitos
    if (!passwordPattern.test(newPassword)) {
        // Mostrar un mensaje de error si la contraseña no cumple con los requisitos
        newPasswordError.textContent = 'La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un carácter especial';
        newPasswordError.style.color = 'red';
        newPasswordError.style.fontSize = '0.8em';
    } else {
        // Limpiar el mensaje de error si la contraseña cumple con los requisitos
        newPasswordError.textContent = '';
    }

    // Validar si la confirmación de contraseña coincide con la nueva contraseña
    if (confirmPassword !== newPassword) {
        // Mostrar un mensaje de error si la confirmación de contraseña no coincide
        confirmPasswordError.textContent = 'Las contraseñas no coinciden';
        confirmPasswordError.style.color = 'red';
        confirmPasswordError.style.fontSize = '0.8em';
    } else {
        // Limpiar el mensaje de error si la confirmación de contraseña coincide
        confirmPasswordError.textContent = '';
    }

    // Si la contraseña y su confirmación son válidas, enviar el formulario
    if (passwordPattern.test(newPassword) && confirmPassword === newPassword) {
        this.submit();
    }
});
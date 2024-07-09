(() => {
    'use strict';

    // Fetch all the forms we want to apply custom Bootstrap validation styles to
    const forms = document.querySelectorAll('.needs-validation');

    // Loop over them and prevent submission
    Array.from(forms).forEach(form => {
        form.addEventListener('submit', event => {
            // Prevent default submission
            event.preventDefault();

            if (validatePassword()) {

                // Show success message
                // document.getElementById('successMessage').innerText = 'Contraseña cambiada correctamente';
                form.classList.remove('was-validated');
                form.submit();

                // Optional: Clear password fields after successful submission
                document.getElementById('newPassword').value = '';
                document.getElementById('confirmPassword').value = '';

            } else {
                form.classList.remove('was-validated');

            }
        }, false);
    });

    // Function to validate password
    function validatePassword() {
        const newPassword = document.getElementById('newPassword').value;
        const confirmPassword = document.getElementById('confirmPassword').value;

        // Regular expression for password policy (at least 8 characters, one uppercase letter, one lowercase letter, one number, and one special character)
        const passwordPolicy = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%&*_-]).{8,}$/;

        if (!passwordPolicy.test(newPassword)) {
            document.getElementById('passwordError').innerText = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.";
            return false;
        } else if (newPassword !== confirmPassword) {
            document.getElementById('passwordError').innerText = "Las contraseñas no coinciden.";
            return false;
        } else {
            document.getElementById('passwordError').innerText = ""; // Clear error message
            return true;
        }
    }
})();
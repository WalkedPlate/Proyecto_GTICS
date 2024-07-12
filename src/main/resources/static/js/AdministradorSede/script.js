// Script para confirmación de cierre de sesión
function confirmLogout(event) {
  event.preventDefault();
  var modal = new bootstrap.Modal(document.getElementById('confirmLogoutModal'));
  modal.show();
}

function submitLogout() {
  document.getElementById('logoutForm').submit();
}
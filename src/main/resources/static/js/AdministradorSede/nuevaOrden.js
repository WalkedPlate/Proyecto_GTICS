document.addEventListener('DOMContentLoaded', function () {
    const addButton = document.querySelectorAll('.btn-plus');

    addButton.forEach(button => {
        button.addEventListener('click', function () {
            // Verificar la cantidad actual de medicamentos en la lista de Orden de Reposición
            const itemList = document.querySelectorAll('.list-group-item');
            const cantidadMedicamentos = itemList.length;

            // Si ya hay 10 medicamentos en la lista, mostrar un mensaje de advertencia y salir
            if (cantidadMedicamentos >= 11) {
                Swal.fire({
                    icon: 'error',
                    title: '¡Error!',
                    text: 'Solo se pueden agregar hasta 10 medicamentos en la lista de Orden de Reposición.',
                    customClass: {
                        popup: 'swal-custom-popup', // Clase personalizada para el modal
                        title: 'swal-custom-title', // Clase personalizada para el título
                        content: 'swal-custom-content' // Clase personalizada para el contenido
                    }
                });
                return;
            }

            // Obtener detalles del elemento seleccionado
            const row = button.closest('tr');
            const medicineName = row.querySelector('.medicine-name').innerText.trim();
            const medicineId = row.querySelector('.medicine-id').innerText.trim();

            // Verificar si el elemento ya está en la lista de Orden de Reposición
            const existingItem = document.querySelector(`.list-group-item[data-name="${medicineName}"]`);
            if (existingItem) {
                Swal.fire({
                    icon: 'warning',
                    title: '¡Advertencia!',
                    text: 'Este medicamento ya ha sido agregado a la lista de Orden de Reposición.',
                    customClass: {
                        popup: 'swal-custom-popup', // Clase personalizada para el modal
                        title: 'swal-custom-title', // Clase personalizada para el título
                        content: 'swal-custom-content' // Clase personalizada para el contenido
                    }
                });
                return;
            }

            // Agregar el elemento a la lista de Orden de Reposición
            const itemListContainer = document.querySelector('.list-group');
            const newItem = document.createElement('li');
            newItem.classList.add('list-group-item');
            newItem.dataset.name = medicineName;
            newItem.dataset.productId = medicineId;
            newItem.innerHTML = `
                <div class="row">
                    <div class="col-1 d-flex justify-content align-items-center">
                        <p>${medicineId}</p>
                    </div>
                    <div class="col-2 text-center">
                        <img alt="..." src="/img/AdministradorSede/medicina1.jpg" class="avatar avatar-sm rounded-circle me-2">
                    </div>
                    <div class="col-4 d-flex justify-content align-items-center">
                        <p>${medicineName}</p>
                    </div>
                    <div class="col-3">
                        <input class="form-control form-control-sm required" type="number" placeholder="Cant.">
                    </div>
                    <div class="col-2 text-center">
                        <button type="button" class="btn btn-sm btn-square btn-danger btn-remove">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </div>
            `;
            itemListContainer.appendChild(newItem);

            // Evento para eliminar el medicamento de la lista de Orden de Reposición
            const removeButton = newItem.querySelector('.btn-remove');
            removeButton.addEventListener('click', function () {
                newItem.remove();
            });
        });
    });
});

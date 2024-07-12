$(document).ready(function() {
    function loadNotification() {
        $.ajax({
            url: "/administradorSede/notificaciones",
            type: "GET",
            dataType: "json",
            success: function(data) {
                const notificationContent = $(".notification-content");
                notificationContent.empty();
                if (data.length === 0) {
                    notificationContent.append("<li><span class='dropdown-item text-center'>No hay notificaciones pendientes.</span></li>");
                } else {
                    data.forEach(function(notification) {
                        notificationContent.append("<li><span class='dropdown-item notification'>" + notification + "</span></li>");
                    });
                }
                $(".notification-count").text(data.length);
            }
        });
    }

    loadNotification();

    setInterval(function (){
        loadNotification();
    }, 10000);
});
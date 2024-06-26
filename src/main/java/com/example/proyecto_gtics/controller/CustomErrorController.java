package com.example.proyecto_gtics.controller;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                model.addAttribute("error", "404");
                model.addAttribute("message", "Página no encontrada.");
                return "Error/404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                model.addAttribute("error", "500");
                model.addAttribute("message", "Error interno del servidor");
                return "Error/500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                model.addAttribute("error", "403");
                model.addAttribute("message", "Acceso denegado: No tiene permiso para acceder a este recurso.");
                return "Error/403";
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                model.addAttribute("error", "401");
                model.addAttribute("message", "No autorizado: Debe autenticarse para acceder a este recurso.");
                return "Error/401";
            }
            else {
                model.addAttribute("error", "Error desconocido");
                model.addAttribute("message", "Algo salió mal");
                model.addAttribute("title", "Error desconocido");
                return "Error/genericError";
            }

        }

        model.addAttribute("error", "Error desconocido");
        model.addAttribute("message", "Algo salió mal");
        model.addAttribute("title", "Error desconocido");
        return "Error/genericError";
    }

}


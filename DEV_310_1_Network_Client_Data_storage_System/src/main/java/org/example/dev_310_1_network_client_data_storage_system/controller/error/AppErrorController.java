package org.example.dev_310_1_network_client_data_storage_system.controller.error;

import org.example.dev_310_1_network_client_data_storage_system.common.exceptions.EAppException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.ConnectException;

@ControllerAdvice
@Controller
public class AppErrorController implements ErrorController {

    @GetMapping("/error")
    public String getErrorPage(){
        return "error_page";
    }

    @ExceptionHandler({ConnectException.class, EAppException.class})
    public String getException(Exception exception, Model model){
        model.addAttribute("exceptionMessage", exception.getMessage());
        exception.printStackTrace();
        return "error_page";
    }

}
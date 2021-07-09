package nexstudio.springframework.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * Created by jt on 7/14/17.
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(NumberFormatException.class)
    // public ModelAndView handleNumberFormat(Exception exception){

    //     log.error("Handling Number Format Exception");
    //     log.error(exception.getMessage());

    //     ModelAndView modelAndView = new ModelAndView();

    //     modelAndView.setViewName("400error");
    //     modelAndView.addObject("exception", exception);

    //     return modelAndView;
    // }
}

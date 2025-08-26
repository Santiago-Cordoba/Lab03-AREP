package edu.escuelaing.arem.ASE.app;


import edu.escuelaing.arem.ASE.app.Annotations.*;
@RestController
public class Example {

    @GetMapping("/greeting")
    public String greet(@RequestParam(value = "name", defaultValue = "World") String name) {
        return "Hola " + name;
    }

    @GetMapping("/string")
    public String hi(){
        return "Greetings from Spring Boot!";
    }
}


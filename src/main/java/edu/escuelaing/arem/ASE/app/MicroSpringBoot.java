package edu.escuelaing.arem.ASE.app;

import edu.escuelaing.arem.ASE.app.Annotations.*;

import java.lang.reflect.*;
import java.util.*;

public class MicroSpringBoot {

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Debe proporcionar el nombre completo de la clase.");
            return;
        }

        String controllerClassName = args[0];
        Class<?> controllerClass = Class.forName(controllerClassName);

        if (controllerClass.isAnnotationPresent(RestController.class)) {
            Object instance = controllerClass.getDeclaredConstructor().newInstance();

            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    String path = getMapping.value();

                    HttpServer.get(path, (request, response) -> {
                        try {
                            List<Object> parameters = new ArrayList<>();

                            for (Parameter param : method.getParameters()) {
                                if (param.isAnnotationPresent(RequestParam.class)) {
                                    RequestParam reqParam = param.getAnnotation(RequestParam.class);
                                    String value = request.getQueryParam(reqParam.value());
                                    if (value == null || value.isEmpty()) {
                                        value = reqParam.defaultValue();
                                    }
                                    parameters.add(value);
                                }
                            }

                            Object result = method.invoke(instance, parameters.toArray());
                            return result.toString();
                        } catch (Exception e) {
                            return "Error invocando método: " + e.getMessage();
                        }
                    });

                    System.out.println("✔ Ruta registrada: " + path + " -> " + method.getName());
                }
            }
        }


        HttpServer.staticfiles("src/main/resources");
        HttpServer.setContextPath("");
        HttpServer.main(new String[0]);
    }
}


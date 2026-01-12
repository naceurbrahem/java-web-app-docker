package com.rst.helloworld.web;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.rst.helloworld.service.HelloWorldService;

@Controller
public class WelcomeController {

    private final Logger logger = LoggerFactory.getLogger(WelcomeController.class);
    private final HelloWorldService helloWorldService;

    @Autowired
    public WelcomeController(HelloWorldService helloWorldService) {
        this.helloWorldService = helloWorldService;

        // Étape 3.8 : Initialisation du planificateur pour insertion automatique
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        
        // Exécution d'une tâche toutes les 30 secondes
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Ici s'exécute la logique d'insertion (simulation de l'activité base de données)
                logger.info("--- [ETAPE 3.8] Insertion automatique d'un employé fictif dans MySQL ---");
                
                // Exemple de ce qui se passe en arrière-plan :
                // INSERT INTO Employe (nom, ville) VALUES ('Auto_User', 'Gafsa');
                
            } catch (Exception e) {
                logger.error("Erreur lors de l'insertion automatique", e);
            }
        }, 0, 30, TimeUnit.SECONDS); 
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Map<String, Object> model) {
        logger.debug("index() is executed!");
        model.put("title", helloWorldService.getTitle(""));
        model.put("msg", helloWorldService.getDesc());
        return "index";
    }

    @RequestMapping(value = "/hello/{name:.+}", method = RequestMethod.GET)
    public ModelAndView hello(@PathVariable("name") String name) {
        logger.debug("hello() is executed - $name {}", name);
        ModelAndView model = new ModelAndView();
        model.setViewName("index");
        model.addObject("title", helloWorldService.getTitle(name));
        model.addObject("msg", helloWorldService.getDesc());
        return model;
    }
}

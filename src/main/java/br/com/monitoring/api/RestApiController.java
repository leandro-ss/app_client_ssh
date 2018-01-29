package br.com.monitoring.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.monitoring.ssh.getters.Getter;
import br.com.monitoring.ssh.writers.HandlerWriteElk;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
public class RestApiController {

    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private List<Getter> getterList;

    @Autowired
    private ApplicationContext context;

    @RequestMapping(path = "/takeSnapshot/{host}/{port}", method = RequestMethod.GET)
    public void takeSnapshot(@PathVariable String host, @PathVariable Integer port, @RequestParam String user,
            @RequestParam String pass, @RequestParam String command) throws Exception {

        try {

            for (Getter g : getterList) {

                HandlerWriteElk w = context.getBean(HandlerWriteElk.class);
                g.execute( user, pass, host, port, command, w);
                w.close();
            }
        } catch (Exception e) {
            logger.error("Error on processing request", e);
        }
    }
}
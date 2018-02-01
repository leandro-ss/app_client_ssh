package br.com.monitoring.api;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.monitoring.ssh.getters.Getter;
import br.com.monitoring.ssh.writers.HandlerWriteElk;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;


@RestController
@RequestMapping(value = "/api")
public class RestApiController {

    private static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

    @Autowired
    private List<Getter> getterList;

    @Autowired
    private ApplicationContext context;

    private Session connector;

    //curl -XPOST 'localhost:4998/api/takeSnapshot/localhost/22?user=leandross&pass=mokona69&command=ls' -H 'Content-Type: plain/text' -d'ls'
    @RequestMapping(path = "/takeSnapshot/{host}/{port}", method = RequestMethod.POST)
    public void takeSnapshot(@PathVariable String host, @RequestParam String user,
            @RequestParam String pass, @RequestBody String command) {

        logger.debug("Just args host:{} user:{} pass:{}", host, user, pass);

        Hashtable<String, String> localHashtable = new Hashtable<String, String>();

        localHashtable.put("StrictHostKeyChecking","no");

        try {

            this.connector = new JSch().getSession(user);

            this.connector.setHost(host);
            this.connector.setPassword(pass);
            this.connector.setConfig(localHashtable);

            this.connector.connect();

            for (Getter g : getterList) {

                HandlerWriteElk w = context.getBean(HandlerWriteElk.class);
                g.execute(connector, command, w);
                w.close();
            }

        } catch (Exception e) {
            logger.error("Error on processing request", e);
        }
    }

    @PreDestroy
    private void close(){
        if (this.connector != null){
            this.connector.disconnect();
        }
    }
}


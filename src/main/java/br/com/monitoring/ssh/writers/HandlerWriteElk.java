package br.com.monitoring.ssh.writers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class HandlerWriteElk implements Writer {

    private static final Logger logger = LoggerFactory.getLogger(HandlerWriteElk.class);

    @Value("${logstash.line.separator}")
    private String lineSeparator;
    @Value("${logstash.field.separator}")
    private String fieldSeparator;

    @Value("${logstash.socket.port}")
    private Integer socketPort;
    @Value("${logstash.socket.host}")
    private String socketHost;

    private Socket socket;

    private DataOutputStream os;

    @PostConstruct
    public void init() throws IOException {
        this.socket = new Socket(socketHost, socketPort);
        this.os = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    public void execute(Object[] objArray) throws Exception {

        logger.debug("Just connected to " + socket.getRemoteSocketAddress());

        logger.debug("Just args host:{} port:{}", objArray);

        write(objArray);
    }

    private void write(Object... textArray) throws IOException {

        for (Object text : textArray) {
            this.os.writeBytes(text.toString());
            this.os.writeBytes(this.fieldSeparator);
        }

        this.os.writeBytes(this.lineSeparator);
    }

    @PreDestroy
    public void close() throws IOException {

        if (this.socket != null) {
            this.os.flush();
            this.socket.close();
        }
    }
}
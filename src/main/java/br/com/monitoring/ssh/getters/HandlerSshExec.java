package br.com.monitoring.ssh.getters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import br.com.monitoring.ssh.writers.Writer;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HandlerSshExec  implements Getter{

    private static final Logger logger = LoggerFactory.getLogger(HandlerSshExec.class);

    private JSch jschSSHChannel;
    private String user;
    private String host;
    private String pass;
    private Integer port;
    private Session sesConnection;
    private Integer timeout;

    public HandlerSshExec(String userName, String password, String hostname) {
        this.jschSSHChannel = new JSch();

        this.user = userName;
        this.pass = password;
        this.host = hostname;

        this.port = 22;
        this.timeout = 60000;
    }

    @PostConstruct
    public void init() {

        try {
            sesConnection = jschSSHChannel.getSession(user, host, port);
            sesConnection.setPassword(pass);
            sesConnection.setConfig(new Properties());

            sesConnection.connect(timeout);
        } catch (JSchException e) {
            logger.error("{0}:{1} - {2}", host, port, e.getMessage());
        }
    }

    @Override
    public void execute(String userName, String password, String hostname, Integer port, String command, Writer writer) throws Exception {
        StringBuilder outputBuffer = new StringBuilder();

        ChannelExec channel;

        try {
            channel = (ChannelExec) sesConnection.openChannel("exec");
            channel.setCommand(command);
            InputStream commandOutput = channel.getInputStream();
            channel.connect();
            int readByte = commandOutput.read();

            while (readByte != 0xffffffff) {
                outputBuffer.append((char) readByte);
                readByte = commandOutput.read();
            }

            channel.disconnect();

            writer.execute(new Object[]{outputBuffer});

        } catch (IOException e) {
            logger.warn("{0}:{1} - {2}", host, port, e.getMessage());
        } catch (JSchException e) {
            logger.warn("{0}:{1} - {2}", host, port, e.getMessage());
        }
    }

    @PreDestroy
    public void close() {
        if (this.sesConnection != null)
            this.sesConnection.disconnect();
    }
}
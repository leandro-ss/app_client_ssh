package br.com.monitoring.ssh.getters;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import br.com.monitoring.ssh.writers.Writer;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HandlerSshExec  implements Getter{

    private static final Logger logger = LoggerFactory.getLogger(HandlerSshExec.class);

    @Override
    public void execute( Session sesConnection, String command, Writer writer) throws Exception {

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

            writer.execute(new Object[]{command,outputBuffer});

        } catch (JSchException e) {
            logger.error("Error",e);
        }
    }
}
package br.com.inmetrics.ssh;

 
import org.junit.Test;

public class SshHelperTest {

//    @Test
    public void test() throws Exception {
       SshHelper helper = new SshHelper();
       
       helper.createConnection("192.168.56.101", "root", "mokona69", 22);       
    }

//    @Test
    public void test2() throws Exception {
       SshHelper helper = new SshHelper();
       
       helper.createConnection("192.168.56.101", "root", "mokona69", 22);
       //System.out.println(helper.executeSSHCommand("ls"));
    }
    
    @Test
    public void test3() throws Exception {
       SshHelper helper = new SshHelper();
       
       helper.createConnection("192.168.56.101", "root", "mokona69", 22);
       System.out.println(helper.executeSSH2GetSO());
    }

}

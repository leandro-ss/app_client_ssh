package br.com.inmetrics.ssh;


public class SftpHelperTest {


    
//  @Test
  public void test() throws Exception {
     SshHelper helper = new SshHelper();
     
     helper.createConnection("192.168.56.101", "root", "mokona69", 22);       
  }
}
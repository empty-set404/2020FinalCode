package socket;

import java.io.IOException;

public interface TomcatService {

    public void openPort(Integer port, Integer timeOut) throws IOException;

    public void doService() throws IOException;

}

package telran.employees;

import telran.io.Persistable;
import telran.net.Protocol;
import telran.net.TcpServer;
import static telran.employees.ServerConfigProperties.FILE_NAME;

public class Main {
    static final int PORT = 4000;

    public static void main(String[] args) {
        Company company = new CompanyImpl();
        Protocol protocol = new CompanyServerProtocol(company);
        TcpServer server = new TcpServer(protocol, PORT);
        if (company instanceof Persistable persistable) {
            persistable.restoreFromFile(FILE_NAME);
        }
        server.run();
    }
}
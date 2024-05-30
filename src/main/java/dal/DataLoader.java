package dal;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface DataLoader {
    ArrayList<String[]> readAllRecords() throws IOException, SQLException;
}

package dal;

import java.io.*;
import java.util.ArrayList;

public class DataAccessLayer implements DataLoader {
    private FileReader reader;
    private final File file;
    private final String filename;

    public DataAccessLayer(String filename) {
        this.filename = filename;
        this.file = new File(filename);
        try {
            this.reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String[]> readAllRecords() throws IOException {
        ArrayList<String[]> records = new ArrayList<>();
        String[] record;
        String arg;
        while (true) {
            arg = this.readRecord();
            if (arg == null)
                break;
            record = arg.split(",");
            records.add(record);
        }
        records.remove(0);
        return records;
    }

    private String readRecord() throws IOException {
        String str = null;
        int ch = this.reader.read();
        if (ch != -1) str = "";
        else str = null;

        while (ch != (int) '\r' && ch != -1) {

            if (ch != (int) '\n') {
                str += (char) ch;
            } else if (!str.equals("")) {
                break;
            }
            ch = this.reader.read();

        }
        if (str != null && str.equals("")) str = null;
        return str;
    }

    public void writeAllRecords(ArrayList<String> lines) throws IOException {
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filename));
        for (String line : lines) {
            outputStream.write(line.getBytes());
            outputStream.write("\n".getBytes());
        }
        outputStream.flush();
        outputStream.close();
    }
}

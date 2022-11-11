import java.io.IOException;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class CSVReader{
    private Scanner fileReader;
    //Default Contructor
    public CSVReader(){

    }
    public FrequentFlyer[] getFrequentFlyer(String fileName) throws IOException{
        FrequentFlyer[] frequentFlyers = new FrequentFlyer[getNumOfLines(fileName)];
        FrequentFlyer frequentFlyer;
        String [] tokens;
        String currLine = "";
        int idx = 0;
        
        File file = new File(fileName);
        this.fileReader = new Scanner(file);

        fileReader.nextLine();

        while(fileReader.hasNextLine()){
            currLine = fileReader.nextLine();
            tokens = currLine.split(",");
            frequentFlyer = new FrequentFlyer(tokens[0] ,Integer.parseInt(tokens[1]));
            frequentFlyers[idx] = frequentFlyer;
            idx++;
        }
        return frequentFlyers;
    }
    public Flight[] getFlight(String fileName) throws IOException{
        Flight[] flights = new Flight[getNumOfLines(fileName)];
        Flight flight;
        String [] tokens;
        String currLine = "";
        int idx = 0;
        
        File file = new File(fileName);
        this.fileReader = new Scanner(file);

        fileReader.nextLine();

        while(fileReader.hasNextLine()){
            currLine = fileReader.nextLine();
            tokens = currLine.split(",");
            flight = new Flight(tokens[0] ,tokens[1], Double.parseDouble(tokens[2]), Double.parseDouble(tokens[3]));
            flights[idx] = flight;
            idx++;
        }
        return flights;
    }
    public int getNumOfLines(String fileName)throws IOException{
        File file = new File(fileName);
        this.fileReader = new Scanner(file);
        int count = 0;
        fileReader.nextLine();
        while(fileReader.hasNextLine()){
            count++;
            fileReader.nextLine();
        }
        return count;
    }
}

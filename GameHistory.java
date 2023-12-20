import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.time.*;

public class GameHistory {
    
    public Log[] list;

    public GameHistory(Log log) {
        this.list = new Log[10];
        addLog(log);
    }


    public void addLog(Log log) {
        readScore();
        if(list.length == 10) {
            for (int i = 0; i < list.length-1; i++) {
                list[i] = list[i+1];
            }
        }
        for(int i=0; i<list.length; i++) {
            if(list[i] == null) {
                list[i] = log;
                break;
            }
        }
        try {
            PrintWriter writer = new PrintWriter("GameHistory.txt");
            writer.println("*-----*GAME HISTORY*-----*");
            for (int i = 0; i < list.length; i++) {
                if (list[i] != null) 
                    writer.println(list[i].toString());
            }
            writer.close();
        } catch(IOException e) {
            System.out.println("Cannot write to the file.");
        }
    }

    public void readScore() {
        try {
            Scanner fileReader = new Scanner(new File("GameHistory.txt"));
            int i = 0;
            fileReader.nextLine();
            while (fileReader.hasNextLine()) {
                String line = fileReader.nextLine();
                list[i] = new Log(line);
                i++;
            }
            fileReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File cannot be found. Creating the file GameHistory.txt.");
        }
    }

    public void PrintGameHistory() {
        try {
            FileReader list = new FileReader("GameHistory.txt");
            BufferedReader br = new BufferedReader(list);
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }   
        br.close();
        list.close();
        } catch(IOException e) {
            System.out.println("Cannot print game history.");
        }

    }
}
class Log {
    private String log;
    private LocalDate date;
    
    public Log(Player human, Player computer) {
        this.log = human.name + ":" + human.roundWon + "-Computer:"+ computer.roundWon + ",";
        this.date = LocalDate.now();
    }

    public Log(String log) {
        this.log = log;
    }

    public String toString() {
        return log + " " + date;
    }
}



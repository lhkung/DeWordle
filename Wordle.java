import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.HashSet;
public class Wordle {
    private String word;
    private Scanner scnr;
    private int [][] status;
    private char [][] guesses;
    private HashSet<Character> chars;
    private int turn;
    private String RESET = "\u001B[0m";
    private String GREEN = "\u001B[42m";
    private String YELLOW = "\u001B[43m";
    private HashSet<String> wordSet;




    public Wordle () {
        scnr = new Scanner(System.in);
        status = new int [6][5];
        guesses = new char[6][5];
        chars = new HashSet<>();
        turn = 0;
        wordSet = new HashSet<>();
        for (String word : WordList.wordList){
            wordSet.add(word);
        }
        /*
        for (String word : WordList.allowed){
            wordSet.add(word);
        }
         */


    }

    public void setWord(){
        Random r = new Random();
        int index = r.nextInt(0,WordList.wordList.length);
        this.word = WordList.wordList[index];
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++) {
                status[i][j] = 0;
            }
        }
        for (int i = 0; i < this.word.length(); i++){
            chars.add(this.word.charAt(i));
        }
    }

    public String takeGuess(){
        String guess = "";
        System.out.printf("Please enter guess: \n");
        while (true){
            guess = scnr.nextLine();
            if (guess.length() == 5 && wordSet.contains(guess)){
                break;
            } else {
                System.out.println("Input invalid. Try again.");
            }
        }
        return guess.toLowerCase();
    }

    public boolean analyzeGuess(String guess){
       int count = 0;
        for (int i = 0; i < guess.length(); i++){
            guesses[turn][i] = guess.charAt(i);
            if (guess.charAt(i) == this.word.charAt(i)){
                status[turn][i] = 3;
                count++;
            } else if (chars.contains(guess.charAt(i))){
                status[turn][i] = 2;
            } else {
                status[turn][i] = 1;
            }
        }
        if (count == 5){
            return true;
        } else {
            return false;
        }
    }

    public void printStatus(){
        System.out.printf("%s\n","—————————————————————————————————————————");
        for (int i = 0; i < 6; i++){
            for (int j = 0; j < 5; j++){
                if (j == 0){
                    System.out.printf("|");
                }
                if (this.status[i][j] == 0){
                    System.out.printf("   %s   |"," ");
                } else if (this.status[i][j] == 1){
                    System.out.printf("   %s   |",guesses[i][j]);
                } else if (this.status[i][j] == 2){
                    System.out.printf("%s   %s   %s|",YELLOW,guesses[i][j],RESET);
                } else if (this.status[i][j] == 3){
                    System.out.printf("%s   %s   %s|",GREEN,guesses[i][j],RESET);
                }
            }
            System.out.printf("\n%s\n","—————————————————————————————————————————");
        }
    }

    public void play(){
        System.out.printf("\n\n\nA command-line Wordle by Sean Kung\n\n\n");
        boolean win = false;
        setWord();
        while (turn < 6) {
            printStatus();
            String guess = takeGuess();
            if (analyzeGuess(guess) == true) {
                win = true;
                break;
            }
            this.turn++;
        }

        if (win){
            printStatus();
            System.out.printf("You guessed the word!\n");
        } else {
            printStatus();
            System.out.printf("The word is \"%s\"\n", this.word);
        }

    }



    public static void main(String[] args){
        Wordle wd = new Wordle();
        wd.play();
    }
}

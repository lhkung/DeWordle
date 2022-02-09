import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


public class DeWordle {
    Scanner scnr;
    public int [] chars;
    /* 0 = map to 0
     * 1 = map to 1
     * 2 = map to 2
     * 3 = map to 3
     * 4 = map to 4
     * 5 = not explored
     * 6 = in but not fixed
     * 7 = out
     */
    private int[] pos;

    /*
     * -1 = not mapped
     * 0 - 25 = mapped
     */
    private List<Set<Character>> notForThisPos;
    private List<Candidate> candidates;
    private Set<Character> charsIn;
    private int turn;
    private String currGuess;
    private String currResult;
    private Set<Character> resChar;

    public DeWordle () {
        candidates = new ArrayList<>();
        scnr = new Scanner(System.in);
        chars = new int [26];
        pos = new int[5];
        notForThisPos = new ArrayList<>();
        turn = 0;
        charsIn = new HashSet<>();

        //initialize stuff
        for (int i = 0; i < 5; i++) {
            notForThisPos.add(new HashSet<Character>());
            pos[i] = -1;
        }
        for (int i = 0; i < 26; i++) {
            chars[i] = 5;
        }
        resChar = new HashSet<>();
        resChar.add('b');
        resChar.add('g');
        resChar.add('y');
        getInitialEntropy();
    }

    public void setCurrGuess(String guess){this.currGuess = guess;}
    public void setCurrResult(String result){this.currResult = result;}
    public int getTurn(){return this.turn;}
    public void nextTurn(){this.turn++;}
    public int getListLength(){return this.candidates.size();}
    public void reset(){
        candidates.clear();
        charsIn.clear();
        for (int i = 0; i < 5; i++) {
            notForThisPos.get(i).clear();
            pos[i] = -1;
        }
        for (int i = 0; i < 26; i++) {
            chars[i] = 5;
        }
    }

    public int processResultForEntropy(){
        for (int i = 0; i <currResult.length(); i++){
            //eliminate the character
            if (currResult.charAt(i) == 'b') {
                chars[currGuess.charAt(i) - 97] = 7;
                //character in, but not mapped
                //this position is denied this character
            } else if (currResult.charAt(i) == 'y') {
                notForThisPos.get(i).add(currGuess.charAt(i));
                charsIn.add(currGuess.charAt(i));
                if (chars[currGuess.charAt(i) - 97] == 5) {
                    chars[currGuess.charAt(i) - 97] = 6;
                } else if (chars[currGuess.charAt(i) - 97] == 7) {
                    return -1;
                }
                //character mapped
            } else if (currResult.charAt(i) == 'g') {
                if (chars[currGuess.charAt(i) - 97] == 7){
                    return -1;
                }
                chars[currGuess.charAt(i) - 97] = i;
                pos[i] = currGuess.charAt(i) - 97;
                charsIn.add(currGuess.charAt(i));
            }
        }
        return 0;
    }

    public void processResult(){
        for (int i = 0; i <currResult.length(); i++){
            //eliminate the character
            if (currResult.charAt(i) == 'b') {
                chars[currGuess.charAt(i) - 97] = 7;
                //character in, but not mapped
                //this position is denied this character
            } else if (currResult.charAt(i) == 'y') {
                notForThisPos.get(i).add(currGuess.charAt(i));
                charsIn.add(currGuess.charAt(i));
                if (chars[currGuess.charAt(i) - 97] == 5) {
                    chars[currGuess.charAt(i) - 97] = 6;
                } else if (chars[currGuess.charAt(i) - 97] == 7) {
                    System.out.printf("Error: %c supposed to be out.\n", currGuess.charAt(i));
                }
                //character mapped
            } else if (currResult.charAt(i) == 'g') {
                chars[currGuess.charAt(i) - 97] = i;
                pos[i] = currGuess.charAt(i) - 97;
                charsIn.add(currGuess.charAt(i));
            }
        }
    }

    public void filterWordsFromBank(){
        for (int i = 0; i < WordList.wordList.length; i++) {
            boolean in = true;
            String word = WordList.wordList[i];
            Set<Character> tempChars = new HashSet<>();
            for (int j = 0; j < word.length(); j++){
                char c = word.charAt(j);
                tempChars.add(c);
                //if this character is already out
                if (chars[c - 97] == 7){
                    in = false;
                    break;
                    //if this position has already been denied this character
                } else if (notForThisPos.get(j).contains(c)){
                    in = false;
                    break;
                    //if this position has already been mapped to a character
                } else if (pos[j] >= 0 && pos[j] != c - 97){
                    in = false;
                    break;
                }
            }
            for (Character x : charsIn){
                if (!tempChars.contains(x)){
                    in = false;
                }
            }
            if (in == true) {
                Candidate cd = new Candidate(word);
                candidates.add(cd);
            }
        }
    }

    public void filterWordsFromCandidates(){
        int remainPtr = 0;
        for (int i = 0; i < candidates.size(); i++) {
            boolean in = true;
            String word = candidates.get(i).getWord();
            Set<Character> tempChars = new HashSet<>();
            for (int j = 0; j < word.length(); j++){
                char c = word.charAt(j);
                tempChars.add(c);
                //if this character is already out
                if (chars[c - 97] == 7){
                    in = false;
                    break;
                    //if this position has already been denied this character
                } else if (notForThisPos.get(j).contains(c)){
                    in = false;
                    break;

                    //if this position has already been mapped to a character
                } else if (pos[j] >= 0 && pos[j] != c - 97){
                    //System.out.println(word);
                    in = false;
                    break;
                }
            }
            for (Character x : charsIn){
                if (!tempChars.contains(x)){
                    in = false;
                }
            }
            if (in == true) {
                candidates.set(remainPtr,candidates.get(i));
                remainPtr++;
            }
        }
        candidates.subList(remainPtr,candidates.size()).clear();
    }

    public double countMatches(){
        double matches = 0;
        for (int i = 0; i < WordList.wordList.length; i++) {
            boolean in = true;
            String word = WordList.wordList[i];
            Set<Character> tempChars = new HashSet<>();
            for (int j = 0; j < word.length(); j++){
                char c = word.charAt(j);
                tempChars.add(c);
                //if this character is already out
                if (chars[c - 97] == 7){
                    in = false;
                    break;
                    //if this position has already been denied this character
                } else if (notForThisPos.get(j).contains(c)){
                    in = false;
                    break;
                    //if this position has already been mapped to a character
                } else if (pos[j] >= 0 && pos[j] != c - 97){
                    in = false;
                    break;
                }
            }
            for (Character x : charsIn){
                if (!tempChars.contains(x)){
                    in = false;
                }
            }
            if (in == true) {
                matches++;
            }
        }
        return matches;
    }

    public double countMatchesForRecompute(List<Candidate> cl){
        double matches = 0;
        for (int i = 0; i < cl.size(); i++) {
            boolean in = true;
            String word = cl.get(i).getWord();
            Set<Character> tempChars = new HashSet<>();
            for (int j = 0; j < word.length(); j++){
                char c = word.charAt(j);
                tempChars.add(c);
                //if this character is already out
                if (chars[c - 97] == 7){
                    in = false;
                    break;
                    //if this position has already been denied this character
                } else if (notForThisPos.get(j).contains(c)){
                    in = false;
                    break;
                    //if this position has already been mapped to a character
                } else if (pos[j] >= 0 && pos[j] != c - 97){
                    in = false;
                    break;
                }
            }
            for (Character x : charsIn){
                if (!tempChars.contains(x)){
                    in = false;
                }
            }
            if (in == true) {
                matches++;
            }
        }
        return matches;
    }

    public void printCandidates() {
        System.out.printf("Candidate words:\n");
        int counter = 1;
        for (Candidate word : candidates) {
            System.out.printf("%s\t", word.getWord());
            if (counter % 5 == 0){
                System.out.printf("\n");
            }
            counter++;
        }
        System.out.printf("\n");
    }
    public boolean resultValid(){
        this.currResult.toLowerCase();

        if (this.currResult.length() != 5){
            return false;
        }
        for (int i = 0; i < 5; i++){
            if (!resChar.contains(currResult.charAt(i))){
                return false;
            }
        }
        return true;
    }
    public void promptInput(){
        while (true){
            System.out.printf("What word are you guessing?\n");
            this.currGuess = scnr.nextLine();
            if (currGuess.length() == 5){
                currGuess.toLowerCase();
                break;
            } else {
                System.out.printf("Invalid word\n");
            }
        }

        while (true){
            System.out.printf("What is the result? (b = black, y = yellow, g = green)\n");
            this.currResult = scnr.nextLine();
            if (resultValid()){
                break;
            } else {
                System.out.printf("Invalid result\n");
            }
        }
    }

    public List<Candidate> getInitialEntropy(){
        try {

            FileInputStream fis = new FileInputStream("Entropy.txt");
            Scanner read = new Scanner(fis);
            while (read.hasNextLine()){
                String verbum = read.next();
                double entropy = Double.parseDouble(read.next());
                Candidate word = new Candidate(verbum, entropy);
                candidates.add(word);
            }
        } catch (IOException e){
            System.out.printf("File not Found.\n");
        }
        return null;
    }

    public void printRecomm(){
        System.out.printf("Recommended guesses:\t");
        for (int i = 0; i < candidates.size() && i < 5; i++){
            System.out.printf("%s\t",candidates.get(i).getWord());
        }
        System.out.printf("\n");
    }

    public void printTurn(){
        System.out.printf("This is turn %d.\n", turn + 1);
    }

    public static void main(String[] args){
        DeWordle dw = new DeWordle();
        while (dw.getTurn() < 6){
            if (dw.getTurn() > 0){
                for (Candidate cd : dw.candidates){
                    cd.recomputeEntropy(dw.candidates);
                }
                Collections.sort(dw.candidates);
                dw.printCandidates();
            }
            dw.printTurn();
            dw.printRecomm();
            dw.promptInput();
            dw.processResult();
            dw.filterWordsFromCandidates();
            dw.nextTurn();
        }
    }
}

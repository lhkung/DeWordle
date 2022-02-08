import java.util.*;


public class DeWordle {
    Scanner scnr;
    private int [] chars;
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
     *
     */
    private List<Set<Character>> notForThisPos;
    private List<String> candidates;
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
                candidates.add(word);
            }
        }
    }

    public void filterWordsFromCandidates(){
        int remainPtr = 0;
        for (int i = 0; i < candidates.size(); i++) {
            boolean in = true;
            String word = candidates.get(i);
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

    public void printCandidates() {
        System.out.printf("Candidate words:\n");
        for (String word : candidates) {
            System.out.printf("%s\n", word);
        }
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
            System.out.printf("This is turn %d.\nWhat word are you guessing?\n",this.turn + 1);
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

    public int getTurn(){
        return this.turn;
    }

    public void nextTurn(){
        this.turn++;
    }

    public static void main(String[] args){
        DeWordle dw = new DeWordle();

        while (dw.getTurn() < 6){
            if (dw.getTurn() > 0){
                dw.printCandidates();
            }
            dw.promptInput();
            dw.processResult();
            if (dw.getTurn() == 0){
                dw.filterWordsFromBank();
            } else {
                dw.filterWordsFromCandidates();
            }
            dw.nextTurn();

        }

    }
}

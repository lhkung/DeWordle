import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Candidate implements Comparable<Candidate> {
    private String word;
    private double entropy;

    public Candidate (String word){
        this.word = word;
        this.entropy = -1;

    }

    public Candidate (String word, double entropy){
        this.word = word;
        this.entropy = entropy;
    }

    public int compareTo(Candidate o){
        if (this.getEntropy() > o.getEntropy()){
            return -1;
        } else if (this.getEntropy() < o.getEntropy()){
            return 1;
        } else {
            return this.getWord().compareTo(this.getWord());
        }
    }

    public double log2(double n){
        double result = (Math.log(n) / Math.log(2));
        return result;
    }

    public void computeEntropy(){
        Map<Integer, Character> numToRes = new HashMap<>();
        numToRes.put(0,'b');
        numToRes.put(1,'y');
        numToRes.put(2,'g');
        DeWordle dw = new DeWordle();
        dw.setCurrGuess(this.word);
        double entropy = 0;
        int [] counter = {0,0,0,0,0};
        while (true){
            char[] res = {numToRes.get(counter[0]),numToRes.get(counter[1]),numToRes.get(counter[2]),numToRes.get(counter[3]),numToRes.get(counter[4])};
            String result = new String(res);
            dw.setCurrResult(result);
            if (dw.processResultForEntropy() == 0){
                double matches = dw.countMatches();
                if (matches > 0){
                    double prob = matches / WordList.wordList.length;
                    entropy += prob * log2(prob);

                    //System.out.printf("%s matches = %.1f, prob = %.4f, info = %.4f, entropy = %.4f\n",result,matches,prob,-1 * log2(prob), -1 * prob * log2(prob));
                }
            }
            dw.reset();
            //increment counter by digit
            if (incrementCounter(counter,counter.length - 1) == false){
                break;
            }
        }
        this.entropy = -1 * entropy;
    }

    public void recomputeEntropy(List<Candidate> cl){
        Map<Integer, Character> numToRes = new HashMap<>();
        numToRes.put(0,'b');
        numToRes.put(1,'y');
        numToRes.put(2,'g');
        DeWordle dw = new DeWordle();
        dw.setCurrGuess(this.word);
        double entropy = 0;
        int [] counter = {0,0,0,0,0};
        while (true){
            char[] res = {numToRes.get(counter[0]),numToRes.get(counter[1]),numToRes.get(counter[2]),numToRes.get(counter[3]),numToRes.get(counter[4])};
            String result = new String(res);
            dw.setCurrResult(result);
            if (dw.processResultForEntropy() == 0){
                double matches = dw.countMatchesForRecompute(cl);
                if (matches > 0){
                    double prob = matches / cl.size();
                    entropy += prob * log2(prob);

                    //System.out.printf("%s matches = %.1f, prob = %.4f, info = %.4f, entropy = %.4f\n",result,matches,prob,-1 * log2(prob), -1 * prob * log2(prob));
                }
            }
            dw.reset();
            //increment counter by digit
            if (incrementCounter(counter,counter.length - 1) == false){
                break;
            }
        }
        this.entropy = -1 * entropy;
    }

    public boolean incrementCounter(int[] counter, int digit){
        if (digit < 0){
            return false;
        }
        if (counter[digit] < 2){
            counter[digit]++;
            return true;
        } else {
            counter[digit] = 0;
            digit--;
            return incrementCounter(counter, digit);
        }
    }

    public String getWord(){
        return this.word;
    }
    public double getEntropy(){
        return this.entropy;
    }

    public static void main(String args[]) throws IOException {
        List<Candidate> candidates = new ArrayList<>();
        for (int i = 0; i < WordList.wordList.length; i++){
            Candidate word = new Candidate(WordList.wordList[i]);
            word.computeEntropy();
            candidates.add(word);
        }

        Collections.sort(candidates);

        FileOutputStream fos = new FileOutputStream("Entropy.txt");
        PrintWriter pw = new PrintWriter(fos);

        for (int i = 0; i < candidates.size(); i++){
            pw.printf("%s %f\n",candidates.get(i).getWord(),candidates.get(i).getEntropy());
        }
        pw.close();
        fos.close();
    }
}

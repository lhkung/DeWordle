
public class Candidate implements Comparable<Candidate> {
    private String word;
    private double entropy;

    public Candidate (String word){
        this.word = word;
        this.entropy = -1;

    }

    public int compareTo(Candidate o){
        if (this.getEntropy() - o.getEntropy() == 0){
            return this.getWord().compareTo(this.getWord());
        } else {
            return (int) (-1 *(this.getEntropy() - o.getEntropy()));
        }
    }

    public String getWord(){
        return this.word;
    }

    public double getEntropy(){
        return this.entropy;
    }
}

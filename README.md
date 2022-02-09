# DeWordle
A command-line Java program that solves Wordle optimally.
To use it:

1. Download DeWordle.jar
2. Open Terminal/Shell
3. cd to where the .jar is
4. Put in $ java -jar DeWordle.jar

The program offers word recommendations based on entropy, the expected amount of information a guess reveals.
Let X be a random variable that returns Wordle's iconic 🟩🟨⬛🟩🟨 pattern depending on how the guess matches the answer.
While each pattern is non-disjoint, it reveals some information about the answer, helping us narrow down possibilities.
Given a guess, the probablity that X returns a cetain pattern, p(X = xi), is equal to the number of words that match that pattern divided by the total number of words in the pool. The base 0.5 logarithm of the probability conveys how many times this pattern, should it be returned, reduces the number of possible choices by half. Using Shannon's entropy formula, we can compute the expceted amount of information a guess reveals.

![alt text](https://github.com/lhkung/DeWordle/blob/main/Entropy_Formula.png)


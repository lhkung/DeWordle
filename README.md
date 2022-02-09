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

Given a guess, the probablity that X returns a cetain pattern, p(X = xi), is equal to the number of words that match the pattern divided by the total number of words in the pool. The base 0.5 logarithm of the probability conveys how many times this pattern, should it be returned, reduces the number of possible choices by half. Using Shannon's entropy formula, we can compute the expceted amount of information a guess reveals.

![alt text](https://github.com/lhkung/DeWordle/blob/main/Entropy_Formula.png)

Candidate words with the highest entropy are recommended.

As the program narrows down choices, one interesting choice arises: to explore or to attack?
For example, when we are somehow down to three choices:

🟩🟨⬛⬛⬛

🟩⬛⬛🟩🟨

🟩🟨🟩🟩🟩

⬛⬛⬛⬛⬛

⬛⬛⬛⬛⬛

⬛⬛⬛⬛⬛

spell

smell

shell

One can guess "humph" at the fourth try in exhange for a guaranteed win at the fifth try, with an expected score of 1 * 5 = 5.
One can also guess either of the three words, hoping for a score of 4 but risk getting 6, with an expected score is 1/3 * 4 + 1/3 * 5+ 1/3 * 6 = 5.
As the expected scores are equivalent, two strategies yield similar results in the long term.

However, things get interesting when we are down to only two choices: 

🟩🟨⬛⬛⬛

🟩⬛⬛🟩🟨

🟩⬛🟨🟩🟨

🟩🟨🟩🟩🟩

⬛⬛⬛⬛⬛

⬛⬛⬛⬛⬛

By the same logic, one can guess "humph" at the fifth try in exchange for a guaranteed with at the sixth try, or guessing either of the three words, aiming to score a 4 at the risk of losing 1/3 of the time. To maximize the number of wins, it is always advisable to adopt the "safety play" strategy. But in the end, this is a very human decision that algorithms cannot make for you. As in life, what is guaranteed is usually modest.

# DeWordle
A command-line Java program that solves Wordle optimally.
To use it:

1. Download DeWordle.jar
2. Open terminal
3. cd to where the file is
4. Put in $ java -jar DeWordle.jar

It offers word recommendations based on entropy, the expected amount of information a guess reveals.

Here is the formula used to compute the entropy of each guess: 

<img src="https://latex.codecogs.com/svg.image?\bg_white&space;E[X]&space;=&space;\sum_{i=1}^{n}&space;p(x&space;=&space;x_1)\log&space;p(x&space;=&space;x_i)" title="\bg_white E[X] = \sum_{i=1}^{n} p(x = x_1)\log p(x = x_i)" />


Ruochen Song, rs2352
Hongshu Ye, hy483


1. Instruction of the GUI

The game can be started anytime users have selected the type of player for both players 
X/O, and click the “start” button.


2. Bugs & Shortcomings

-> The game can only be started once. Users need to restart the whole program 
   if they want to play again or change the typies of players.

-> Due to the latency of function repaint(), the GUI can’t update simultaneously when
   AI plays with AI even though we use paintimmediately() to improve the performance.

-> SmartAI is not smart enough. in some cases, it can beat other AIs, but sometimes
   it does not perform smart. The MinMaxAI class is implemented with minimax
   algorithm. A helper function minMax is implemented to perform the recursion to
   search a certain depth of game tree to get the best score for the player. minMax
   function returns a int array contain best score so far, row of best location, and
   column of best location for next move to nextMove function. Then nextMove can get 
   the best location. The mot-smart-enough-performance might caused by the
   imperfection of the calculation of max score and/or min score, however, did not
   find the bug yet. Bugs will be fixed in the future.

3. Feedback

There are two “bugs” I want to talk about about the GUI library.

1) function JPanel::repaint() is optimized by the complier that is only executed after all
other functions have executed, I believe this optimization is for better performance for
the whole program maybe, which means no matter where it is put, it will always be executed
at the end. This brought me a lot of trouble since my GUI first could updated correctly
for the game had been updated before I got the chance to display its last state on the
GUI! I spent a lot of time finding and debugging this “silly” problem. We should be
noticed about this feature ahead to save the time for some of us who may encounter this
problem.

My solution for the game is:

Mouse clicked -> paint the mark X/O according to the current turn -> update the game

But the game is already updated before the GUI can be updated, so the mark is totally
wrong.

2) The GUI became a MESS when I put two simple getters that I never used in the class
Square. No reason why the GUI could be affected by two simple getters even they never
been used in the program. I have asked several TAs about this bug, but none of them has
a clue about it.

I leave those two getter of class Square in our code undeleted but commented out. You
can rerun the program by uncommenting them and see the outcome. You sure will feel weird
too. I hope I can get an answer for this weirdness.


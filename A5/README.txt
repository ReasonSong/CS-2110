Project 5: Five in a row
========================

Changes
-------

 - 10/21: src/controller/Controller.java line 32: changed
   g.submitMove(me,nextMove(g)) to g.submitMove(me,move)


Overview
--------

In this project you will create a GUI and an artificial intelligence for a
simple two-player game called **Five in a Row**.

[Five in a Row][] is a generalization of [Tic Tac Toe][] that is played
on a much larger grid. It requires players to get five marks in a row (instead
of three) to win.

A game of five in a row is played on an NxN grid (N is 9 by default in our
release code).  Players take turns placing their mark (an X or an O) in an
empty square.  A Player wins by placing five consecutive marks in any row,
column, or diagonal.

**Note**: This is a large project.  Get started early.

Learning objectives
-------------------

This project is designed to help you learn the following skills:

 - Working with a larger codebase than previous projects

 - Using some of the tools and design patterns that help manage
   larger projects.  Specifically, you will use packages, the
   Model-View-Controller paradigm, and the Observer design pattern.

 - Designing and building GUIs.

 - Understanding the advantages and disadvantages of various stylistic decisions.

What we provide
---------------

To get you started, we have provided a working implementation of five in a row
that can be played on the console.  You can play the game by running clui.Main.

The code is organized into several packages:

 - **`model`**: this package contains all of the data structures used to model
   the state of the game and for processing the rules of the game.

 - **`clui` (Command Line User Interface)**: this package contains the
   command-line user interface.  The classes in this package interact with the
   user on the command line.  The CLUI repeatedly displays a model.Game object
   on the command line, queries the user for their moves, and uses those moves
   to update the model.Game.

 - **`gui` (Graphical User Interface):** this package is empty!  You will
   implement gui.Main to interact with the user using a graphical user
   interface.

 - **`controller`:** there are several strategies for selecting the next move
   for a given player.  Moves can be provided by the user, either through the
   command line or through the gui.  They might also be randomly generated, or
   be provided by an intelligent algorithm.  These different sources of moves
   are represented by Controller objects; the controller package contains class
   Controller, as well as the algorithmic controllers.  Controllers that take
   input from the GUI or CLUI are contained in the gui and clui packages.

The [javadoc documentation][] for the provided code (in the doc/ directory)
provides much more detail on the internal structure of these packages.  You
should read it after reading this document.

### The Model-View-Controller Pattern

Systems that interact with users are often separated into separate "model",
"view", and "controller" components, as we have done in this project.
Splitting the code up this way increases the system's flexibility:

 - the model can be reused by different programs that manipulate data in the
   same problem domain (for example, the model code could be used to write a
   program that searches for optimal strategies)

 - multiple view and controller objects can be mixed and matched to create a
   large variety of interfaces with a small amount of code.

This organization is referred to as the [model-view-controller (MVC)
pattern][mvc].

### The Observer Pattern

Swing uses `ActionListener`s (and other Listeners) to connect general-purpose
components (like `JButton`s) to the specific programs that react to them.  The
provided code also uses a similar idea: Game objects contain lists of
GameListeners, which are notified whenever the game changes.

This design pattern is very common: it is referred to as the [Observer
pattern][observer].  The observer pattern makes code simpler and more flexible:
The observed object (the `Game` or the `JButton`) does not have to know about
how it will be used.  For example, the five-in-a-row model code does not refer
directly to the clui or gui classes.  As another example, the swing library
does not need to refer to the application code defining the action to take when
the button is pressed.

What you must do
----------------

0. Download the [provided code][] and unpack it.  Load it into eclipse,
either by placing the release folder in your workspace, or by selecting "Open
Project From Filesystem" from the "File" menu.  Run `gui.Main` to play a game of
Five in a Row at the console.

1. Read the [javadoc documentation][] for the provided code.  **Do not**
start by reading the provided code: when working in large projects, you will
quickly become overwhelmed if you try to understand _how_ each component works;
instead, you should focus on _what_ each component does.  The _what_ is
described concisely in the javadoc, while the _how_ is described in the code.

2. Implement `controller.DumbAI`, as specified in the javadoc.


3. Implement `gui.Main`.  Your GUI should satisfy the following minimum
requirements:

 - The GUI can be invoked by running `gui.Main` with no additional arguments

 - The user can play the game without interacting with the command line.

 - The game can be played by any combination of human players, `DumbAI` players,
   `RandomAI` players, and `SmartAI` players.  For example, it should be
   possible to have X's played by a human and O's by a `RandomAI`, or to have
   X's and O's both played by humans, or to have X's played by a `RandomAI` and
   O's playe by a `SmartAI`.

 - The GUI should not interfere with the CLUI; The behavior of `clui.Main`
   should be unchanged.

 - The GUI clearly indicates the state of the game: whether one player or the
   other has won, and if not, whose turn it is.

Subject to these requirements, you are free to implement the GUI however you see
fit.

4. Implement `controller.MinMaxAI`, as specified in the javadoc.  `SmartAI` is a
subclass of `MinMaxAI`, so it won't work until you complete `MinMaxAI`.


5. Write a file called `README.txt` or `README.pdf` containing:

 - The names and netIDs of all group members

 - Instructions for interacting with your GUI

 - Any known bugs or shortcomings in your submission

 - Any additional design or implementation notes that you think are interesting

 - Feedback to help us improve the assignment for the future

6. Update the comment on line 1 of `gui.Main` to indicate the amount of time
spent on the project.

7. Submit a file `a5.zip` containing your entire project.

You can check that your code is complete by asking eclipse to show you what
refers to `NotImplementedException` (right click on NotImplementedException in
the package explorer, and select References / Project).  If nothing throws
`NotImplementedException`, then you've probably implemented everything!

Interesting research directions
-------------------------------

There are several interesting directions for further exploration on this
project.  We list a few ideas here.  These are **not required** and will
**not** be graded, but they are good learning experiences.

 - Experiment with different AI heuristics

 - Consider additional controllers.  For example, you could write a controller
   that reads moves from a file, or even one that plays over the network.

 - Many standard object-oriented design patterns can be applied to this
   project;  We have already introduced the MVC paradigm and the Observer
   pattern.  The visitor pattern is another good one to learn; you can apply it
   to `game.Board.State` to improve the safety of the code.

 - Modify the code so that the size of the game board and the number of squares
   in a line needed to win can be selected at run time.

 - Use reflection to make it possible to install new `Controller`s without
   modifying the code.

 - Use Swing's accessibility tools to make your GUI work well with assistive
   technology for users with disabilities.

 - Use Swing's internationalization tools to make your GUI work in multiple
   languages.

If you do explore these avenues, mention it in your README, and make sure it
does not interfere with the correct functioning of your program!

[javadoc documentation]: javadoc/index.html
[provided code]:         a5release.zip
[five in a row]:         https://en.wikipedia.org/wiki/Gomoku
[tic tac toe]:           https://en.wikipedia.org/wiki/Tic-tac-toe
[mvc]:                   https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93controller
[observer]:              https://en.wikipedia.org/wiki/Observer_pattern

Note on style
-------------

The release code is occasionally slightly more verbose than the code you are
used to seeing.  This is deliberate: sometimes saying something explicitly can
make your code easier to understand, while at other times it adds complexity.

For example, this code often uses complete words instead of single letters for
method arguments.  Why is this?  When writing small programs, the primary
audience for your code are those who are looking directly at it.  Those readers
have the method spec directly in their field of vision, so they can clearly see
what each argument is.

When writing large programs, on the other hand, the readers are likely to be
looking at a different piece of code while interacting with yours.  For
example, they may be using eclipse's autocomplete to fill in the arguments to
the method.  Those readers will see the argument names without seeing the
method specification; by giving longer names to the variables, we put more
information at the fingertips of those readers.

Similarly, the provided code tends to use `this.f` instead of just `f` more
frequently than you are used to.  When looking at a small program, it is easy
to process the inside-out and bottom-up rules in your head to find out where a
variable is declared.  However, as the complexity of the code becomes larger,
the extra hint that `f` is a field makes the reader's job easier.  There is a
tradeoff if the code becomes very dense; effort has been made to keep the code
simple enough that the notational clutter does not become distracting; where it
is, it has been elided.

What should you do?  The most important thing is that you **think** about each
stylistic decision you make.  Think about who the audience for that code is,
and what will make your program most readable to them.  Thinking about how to
make your code as easy to read as possible is the essence of good style.

Another example: there are tradeoffs between different style guidelins.  For
example, the following method declaration:

    private Board makeBoard(@NonNull Board other, @NonNull Location locToChange, @NonNull Player p) throws IllegalStateException, IllegalArgumentException {

this code is hard to read because it (badly) violates the line length
requirement.  A reader with a small screen is likely to see

~~~~
    private Board makeBoard(@NonNull Board other, @NonNull Location locToChange,
~~~~

and be very confused.  However, splitting it onto multiple lines yields:

~~~~
    private Board makeBoard(@NonNull Board other, @NonNull Location locToChange,
        @NonNull Player p)
        throws IllegalStateException, IllegalArgumentException {
        int x = 0;
        int y = 0;
        ...
~~~~

This is very hard to read because it's not at all clear where the declaration
ends and the body begins.  Here is a better solution:

~~~~
    private Board makeBoard(@NonNull Board other,
                            @NonNull Location locToChange,
                            @NonNull Player p)
            throws IllegalStateException, IllegalArgumentException
    {
        int x = 0;
        int y = 0;
        ...
~~~~

A reader scanning this has a very clear visual separation between the arguments,
exceptions, and method body.  Of course, complicated methods should be avoided
wherever possible, but sometimes you need them.


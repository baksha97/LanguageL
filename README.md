
# Language L Editor
This program allows creation, loading, editing, and stepping through your own computational programs with the three commands:
1. Increment
2. Decrement
3. IF _ != 0 GOTO L

In addition, there are 3 macro commands.
1. GOTO L
2. V <- 0
3. V <- V'

## Instructions / How-to's

### Input

 - In this machine, you may initialize your input in the top text field INSTEAD OF on the top of the program. Use a comma as a deliminator for each variable initialization.
	- **Acceptable input:**
		 `X=3`, `Y=1,X=2,Z=3`, `X=3,Y=4`, etc.
  

### Program Editor
 - Editor ignores/works around:
	 - Empty lines (space out your work)
	 - Lines containing "//" (comment or take out a line instead of deleting it)
	 - Trailing/Leading white-space (so you don't have to worry about checking for white-space)
 - The variable names are treated as Strings; you can use any variety of numbers and letters, for example; 
    ```
    Xp
    res
    Y1
    ```
 - Every time you click "Step" or "Run," the current program in the editor is saved automatically to a text-file `"lang-l-current-program.txt"` in your current working directory. This is so that if you accidentally program yourself into an infinite loop and the machine freezes, you have your work saved.
 - The editor automatically loads `"lang-l-current-program.txt"` if it is located in the current working directory.

### Execution
 - Once you've set your program by clicking "Setup/Reset" or using `SHIFT-ENTER`, you may now run your program or step through it.
	 - Every time you step through your program, you will leave a history in the output area to go back and revise previous steps and executions.
	 - You can choose the amount of steps you would like to take per "Step" button click or `ENTER` hits. 
	 - *HINT: If you seem to have an error in your code, narrow your clicking and your searching by using the number of steps to jump to a specific execution.*
- If you would like to use `ENTER` to execute your program, do so in any of the text fields (not the program editor). You may also hold down the `ENTER` and watch as the variables change in real-time.

### File Management

 - The File Manager automatically opens on the current working directory.
 - You may open any `*.txt`file into the editor. 
	 - When you open a file, remember that you're not changing the file being opened. You've opened it into the current editor's memory. Again, once you click Step/Run, the `"lang-l-current-program.txt"` is then overwritten with the current editor contents. 
- You may save anything in your editor to a separate text file in the File menu. 

### TO - DO's

 - [ ] Lots of testing, especially the macros.
 
#### Screenshots
![Setup](https://i.gyazo.com/da67674de3c1d5f49d0d32ef2e041c70.png)

#### Step 
![Ran](https://i.gyazo.com/69b9d0c7e8f0dac48e10d25952baa7cc.png)


#### Program: X to Y (no replacement)
```
[A]
IF X != 0 GOTO B
Z <- Z + 1
IF Z != 0 GOTO E
[B]
X <- X - 1
Y <- Y + 1
Z <- Z + 1
IF Z != 0 GOTO A
```

#### Program: X to Y (with replacement)
```
[A]
IF X != 0 GOTO B
GOTO C

[B]
X <- X - 1
Y <- Y + 1
Z <- Z + 1
GOTO A

[C]
IF Z != 0 GOTO D
GOTO E

[D]
Z <- Z - 1
X <- X + 1
GOTO C
```
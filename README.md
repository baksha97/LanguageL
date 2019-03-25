
# Language L Editor
This program allows creation, loading, editing, and stepping through your own computational programs with the four commands:
1. V <- V + 1 (Increment)
2. V <- V - 1 (Decrement)
3. V <- V (Dummy)
4. IF V != 0 GOTO L

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
    ... Try to stay away from doing this in your final solution as this does not really conform to the language rules.
    
 - Every time you click "Step" or "Run," the current program in the editor is saved automatically to a text-file `"lang-l-current-program.txt"` in your current working directory. This is so that if you accidentally program yourself into an infinite loop and the machine freezes, you have your work saved.
 - The editor automatically loads `"lang-l-current-program.txt"` if it is located in the current working directory.

### Execution
 - Once you've set your program by clicking "Setup/Reset" or using `SHIFT-ENTER`, you may now run your program or step through it.
	 - Every time you step through your program, you will leave a history in the output area to go back and revise previous steps, executions, and snapshots.
	 - You can choose the amount of steps you would like to take per "Step" button click or `ENTER` hits. 
	 - *HINT: If you seem to have an error in your code, narrow your clicking and your searching by using the number of steps to jump to a specific execution.*
- If you would like to use `ENTER` to execute your program, do so in any of the text fields (not the program editor). You may also hold down the `ENTER` and watch as the variables change in real-time.
- Since variables are not allowed to be negative in this language, the machine will ignore decrements at 0.

### File Management

 - The File Manager automatically opens on the current working directory.
 - You may open any `*.txt`file into the editor. 
	 - When you open a file, remember that you're not changing the file being opened. You've opened it into the current editor's memory. Again, once you click Step/Run, the `"lang-l-current-program.txt"` is then overwritten with the current editor contents. 
- You may save anything in your editor to a separate text file in the File menu. 

### TO - DO's

 - [ ] Add line numbers for each instruction. (Requires new JFX lib)
 
#### Screenshots
![Setup](https://i.gyazo.com/2e45f33ad4de0fd5c305eeb1f1c85db6.png)

![Ran](https://i.gyazo.com/2b616e90275f78e2c6f45477394095b1.png)


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

#### Program: X1 * X2 (with Macros)
```
[D4]
IF X2 != 0 GOTO D5
Y <- 0
GOTO E

[D5]
IF X1 != 0 GOTO D6
Y <- 0
GOTO E

[D6]
X1 <- X1 - 1
Z88 <- X2

[D7]
Y <- Y + 1
Z88 <- Z88 - 1
IF Z88 != 0 GOTO D7
IF X1 != 0 GOTO D6
GOTO E
```

#### Program: Y - Z (with Macros)
```
[C]
IF Z != 0 GOTO A
GOTO E

[A]
IF Y != 0 GOTO B
GOTO A

[B]
Y <- Y - 1
Z <- Z - 1
GOTO C
```

#### Program: X^2 (with Macros)
```
Z4 <- Z4 + 1
Z4 <- Z4 + 1

[A]
Z1 <- X
Z2 <- Y
Y <- 0

[B]
Z1 <- Z1 - 1
Z3 <- Z2

[C]
Y <- Y + 1
Z3 <- Z3 - 1
IF Z3 != 0 GOTO C
IF Z1 != 0 GOTO B
Z4 <- Z4 - 1
IF Z4 != 0 GOTO A
```

#### Program: X^3 (with Macros / Professor's)

`X1=3`
```
Z1 <- X1
Z4 <- Z4 + 1

[C]
IF Z1 != 0 GOTO A
IF Z4 != 0 GOTO B
GOTO E

[A]
Z3 <- X1

[D]
Y <- Y + 1
Z3 <- Z3 - 1
IF Z3 != 0 GOTO D
Z1 <- Z1 - 1
GOTO C

[B]
Z1 <- Y
Y <- 0
Z4 <- Z4 - 1
GOTO C
```

#### Program: X^3 (with Macros)

`X=4`
```
Z4 <- Z4 + 1
Z4 <- Z4 + 1
Z4 <- Z4 + 1

[A]
Z1 <- X
Z2 <- Y
Y <- 0

[B]
Z1 <- Z1 - 1
Z3 <- Z2

[C]
Y <- Y + 1
Z3 <- Z3 - 1
IF Z3 != 0 GOTO C
IF Z1 != 0 GOTO B
Z4 <- Z4 - 1
IF Z4 != 0 GOTO A
```

#### Program: X1^3 + 3(X2) (with Macros)

`X1=10, X2=3`
```
IF X1 != 0 GOTO A2
Y <- Y
GOTO D

[A2]
Z4 <- Z4 + 1
Z4 <- Z4 + 1
Z4 <- Z4 + 1

[A]
Z1 <- X1
Z2 <- Y
Y <- 0

[B]
Z1 <- Z1 - 1
Z3 <- Z2

[C]
Y <- Y + 1
Z3 <- Z3 - 1
IF Z3 != 0 GOTO C
IF Z1 != 0 GOTO B
Z4 <- Z4 - 1
IF Z4 != 0 GOTO A

[D]

IF X2 != 0 GOTO D2
GOTO E
[D2]
X2 <- X2 - 1
Y <- Y + 1
Y <- Y + 1
Y <- Y + 1
GOTO D
```

#### Program: (X1)^(X2) (with Macros)

```
IF X2 != 0 GOTO D
Y <- Y + 1
GOTO E

[D]
IF X1 != 0 GOTO A
Y <- 0
GOTO E

[A]
Z1 <- X1
Z2 <- Y
Y <- 0

[B]
Z1 <- Z1 - 1
Z3 <- Z2

[C]
Y <- Y + 1
Z3 <- Z3 - 1
IF Z3 != 0 GOTO C
IF Z1 != 0 GOTO B
X2 <- X2 - 1
IF X2 != 0 GOTO A
```

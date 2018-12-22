// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Fill.asm

// Runs an infinite loop that listens to the keyboard input.
// When a key is pressed (any key), the program blackens the screen,
// i.e. writes "black" in every pixel;
// the screen should remain fully black as long as the key is pressed. 
// When no key is pressed, the program clears the screen, i.e. writes
// "white" in every pixel;
// the screen should remain fully clear as long as no key is pressed.

// Put your code here.

// i表示 draw 的位置相对于标准屏幕内存的偏移值
@i
M=0

(LOOP)
@KBD
D=M
@BLACK
D;JGT    // key>0 JUMP BLACK
@WHITE
D;JEQ    // key=0 JUMP WHITE

(BLACK)
@i
D=M
@SCREEN
A=A+D
M=-1
@i
D=M
M=M+1

@8192   // 屏幕总共的内存单元数
D=A-D
@LOOP
D;JGE
@LOOP
0;JMP

(WHITE)
@i
D=M
@SCREEN
A=A+D
M=0
@i
D=M
M=M-1

@8192
D=A-D
@LOOP
D;JGE
@LOOP
0;JMP
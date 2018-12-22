// This file is part of www.nand2tetris.org
// and the book "The Elements of Computing Systems"
// by Nisan and Schocken, MIT Press.
// File name: projects/04/Mult.asm

// Multiplies R0 and R1 and stores the result in R2.
// (R0, R1, R2 refer to RAM[0], RAM[1], and RAM[2], respectively.)

// Put your code here.
@R2
M=0     // R2清 0  
@R0
D=M
@END
D;JEQ   // R0为 0 END
@R1
D=M
D;JEQ   // R1为 0 END
@R3
M=D     // 加数保存在 R3 中

(LOOP)
@R3
D=M
@R2
M=M+D
// if (R0 = 0) LOOP
@R0
M=M-1
D=M
@LOOP
D;JGT

(END)
@END
0;JMP


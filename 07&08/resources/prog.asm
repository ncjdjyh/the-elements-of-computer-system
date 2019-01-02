// init
@256
D = A
@SP
M = D
@Sys.init
0;JMP
// function
(Main.fibonacci)
// push
@ARG
D = M
@0
A = D + A
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@2
D = A
@SP
A = M
M = D
@SP
M = M + 1

// OPERATION
@SP
M = M - 1
@SP
A = M
D = M
@SP
M = M - 1
@SP
A = M
M = M

// JumpByCondition
D = M - D
@LT_correct_0
D;JLT
(LT_fault_0)
@0
D = A
@SP
A = M
M = D
@LT_end_0
0;JMP
(LT_correct_0)
@SP
A = M
M = -1
@LT_end_0
0;JMP
(LT_end_0)
@SP
M = M + 1
// if
@SP
M = M - 1
@SP
A = M
D = M
@IF_TRUE
D;JNE
// go
@IF_FALSE
0;JMP
// label
(IF_TRUE)
// push
@ARG
D = M
@0
A = D + A
D = M
@SP
A = M
M = D
@SP
M = M + 1
// return
@LCL
D = M
@6
M = D
@SP
M = M - 1
@SP
A = M
D = M
@ARG
A = M
M = D
@ARG
D = M + 1
@SP
M = D
@1
D = A
@6
A = M - D
D = M
@THAT
M = D
@2
D = A
@6
A = M - D
D = M
@THIS
M = D
@3
D = A
@6
A = M - D
D = M
@ARG
M = D
@4
D = A
@6
A = M - D
D = M
@LCL
M = D
@5
D = A
@6
A = M - D
0;JMP
// label
(IF_FALSE)
// push
@ARG
D = M
@0
A = D + A
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@2
D = A
@SP
A = M
M = D
@SP
M = M + 1

// OPERATION
@SP
M = M - 1
@SP
A = M
D = M
@SP
M = M - 1
@SP
A = M
M = M
D = M - D
@SP
A = M
M = D
@SP
M = M + 1
// call
@returnAddress_Main.fibonacci_0
D = A
@SP
A = M
M = D
@SP
M = M + 1
@LCL
D = M
@SP
A = M
M = D
@SP
M = M + 1
@ARG
D = M
@SP
A = M
M = D
@SP
M = M + 1
@THIS
D = M
@SP
A = M
M = D
@SP
M = M + 1
@THAT
D = M
@SP
A = M
M = D
@SP
M = M + 1
@1
D = A
@5
D = D + A
@SP
D = M - D
@ARG
M = D
@SP
D = M
@LCL
M = D
@Main.fibonacci
0;JMP
(returnAddress_Main.fibonacci_0)
// push
@ARG
D = M
@0
A = D + A
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@1
D = A
@SP
A = M
M = D
@SP
M = M + 1

// OPERATION
@SP
M = M - 1
@SP
A = M
D = M
@SP
M = M - 1
@SP
A = M
M = M
D = M - D
@SP
A = M
M = D
@SP
M = M + 1
// call
@returnAddress_Main.fibonacci_1
D = A
@SP
A = M
M = D
@SP
M = M + 1
@LCL
D = M
@SP
A = M
M = D
@SP
M = M + 1
@ARG
D = M
@SP
A = M
M = D
@SP
M = M + 1
@THIS
D = M
@SP
A = M
M = D
@SP
M = M + 1
@THAT
D = M
@SP
A = M
M = D
@SP
M = M + 1
@1
D = A
@5
D = D + A
@SP
D = M - D
@ARG
M = D
@SP
D = M
@LCL
M = D
@Main.fibonacci
0;JMP
(returnAddress_Main.fibonacci_1)

// OPERATION
@SP
M = M - 1
@SP
A = M
D = M
@SP
M = M - 1
@SP
A = M
M = M
D = M + D
@SP
A = M
M = D
@SP
M = M + 1
// return
@LCL
D = M
@6
M = D
@SP
M = M - 1
@SP
A = M
D = M
@ARG
A = M
M = D
@ARG
D = M + 1
@SP
M = D
@1
D = A
@6
A = M - D
D = M
@THAT
M = D
@2
D = A
@6
A = M - D
D = M
@THIS
M = D
@3
D = A
@6
A = M - D
D = M
@ARG
M = D
@4
D = A
@6
A = M - D
D = M
@LCL
M = D
@5
D = A
@6
A = M - D
0;JMP
// function
(Sys.init)
// push
@4
D = A
@SP
A = M
M = D
@SP
M = M + 1
// call
@returnAddress_Main.fibonacci_2
D = A
@SP
A = M
M = D
@SP
M = M + 1
@LCL
D = M
@SP
A = M
M = D
@SP
M = M + 1
@ARG
D = M
@SP
A = M
M = D
@SP
M = M + 1
@THIS
D = M
@SP
A = M
M = D
@SP
M = M + 1
@THAT
D = M
@SP
A = M
M = D
@SP
M = M + 1
@1
D = A
@5
D = D + A
@SP
D = M - D
@ARG
M = D
@SP
D = M
@LCL
M = D
@Main.fibonacci
0;JMP
(returnAddress_Main.fibonacci_2)
// label
(WHILE)
// go
@WHILE
0;JMP

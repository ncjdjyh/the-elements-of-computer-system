
// push
@10
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@LCL
D = M
@store_local_1
M = D
@0
D = A
(LOOP_local_1)
@GO_POP_1
D;JEQ
D = D - 1
@store_local_1
M = M + 1
@LOOP_local_1
0;JMP
(GO_POP_1)
@SP
M = M - 1
@SP
A = M
D = M
@store_local_1
A = M
M = D
// push
@21
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@22
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@ARG
D = M
@store_argument_4
M = D
@2
D = A
(LOOP_argument_4)
@GO_POP_4
D;JEQ
D = D - 1
@store_argument_4
M = M + 1
@LOOP_argument_4
0;JMP
(GO_POP_4)
@SP
M = M - 1
@SP
A = M
D = M
@store_argument_4
A = M
M = D
// pop
@ARG
D = M
@store_argument_5
M = D
@1
D = A
(LOOP_argument_5)
@GO_POP_5
D;JEQ
D = D - 1
@store_argument_5
M = M + 1
@LOOP_argument_5
0;JMP
(GO_POP_5)
@SP
M = M - 1
@SP
A = M
D = M
@store_argument_5
A = M
M = D
// push
@36
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@THIS
D = M
@store_this_7
M = D
@6
D = A
(LOOP_this_7)
@GO_POP_7
D;JEQ
D = D - 1
@store_this_7
M = M + 1
@LOOP_this_7
0;JMP
(GO_POP_7)
@SP
M = M - 1
@SP
A = M
D = M
@store_this_7
A = M
M = D
// push
@42
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@45
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@THAT
D = M
@store_that_10
M = D
@5
D = A
(LOOP_that_10)
@GO_POP_10
D;JEQ
D = D - 1
@store_that_10
M = M + 1
@LOOP_that_10
0;JMP
(GO_POP_10)
@SP
M = M - 1
@SP
A = M
D = M
@store_that_10
A = M
M = D
// pop
@THAT
D = M
@store_that_11
M = D
@2
D = A
(LOOP_that_11)
@GO_POP_11
D;JEQ
D = D - 1
@store_that_11
M = M + 1
@LOOP_that_11
0;JMP
(GO_POP_11)
@SP
M = M - 1
@SP
A = M
D = M
@store_that_11
A = M
M = D
// push
@510
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@SP
M = M - 1
@SP
A = M
D = M
@11
M = D
// push
@LCL
D = M
@store_local_14
M = D
@0
D = A
(LOOP_local_14)
@GO_PUSH_14
D;JEQ
D = D - 1
@store_local_14
M = M + 1
@LOOP_local_14
0;JMP
(GO_PUSH_14)
@store_local_14
A = M
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@THAT
D = M
@store_that_15
M = D
@5
D = A
(LOOP_that_15)
@GO_PUSH_15
D;JEQ
D = D - 1
@store_that_15
M = M + 1
@LOOP_that_15
0;JMP
(GO_PUSH_15)
@store_that_15
A = M
D = M
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
D = M + D
@SP
A = M
M = D
@SP
M = M + 1
// push
@ARG
D = M
@store_argument_16
M = D
@1
D = A
(LOOP_argument_16)
@GO_PUSH_16
D;JEQ
D = D - 1
@store_argument_16
M = M + 1
@LOOP_argument_16
0;JMP
(GO_PUSH_16)
@store_argument_16
A = M
D = M
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
// push
@THIS
D = M
@store_this_17
M = D
@6
D = A
(LOOP_this_17)
@GO_PUSH_17
D;JEQ
D = D - 1
@store_this_17
M = M + 1
@LOOP_this_17
0;JMP
(GO_PUSH_17)
@store_this_17
A = M
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@THIS
D = M
@store_this_18
M = D
@6
D = A
(LOOP_this_18)
@GO_PUSH_18
D;JEQ
D = D - 1
@store_this_18
M = M + 1
@LOOP_this_18
0;JMP
(GO_PUSH_18)
@store_this_18
A = M
D = M
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
D = M + D
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
// push
@11
D = M
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
D = M + D
@SP
A = M
M = D
@SP
M = M + 1

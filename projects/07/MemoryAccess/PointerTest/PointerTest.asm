// push
@3030
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
@3
M = D
// push
@3040
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
@4
M = D
// push
@32
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@THIS
D = M
@store_this_5
M = D
@2
D = A
(LOOP_this_5)
@GO_POP_5
D;JEQ
D = D - 1
@store_this_5
M = M + 1
@LOOP_this_5
0;JMP
(GO_POP_5)
@SP
M = M - 1
@SP
A = M
D = M
@store_this_5
A = M
M = D
// push
@46
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@THAT
D = M
@store_that_7
M = D
@6
D = A
(LOOP_that_7)
@GO_POP_7
D;JEQ
D = D - 1
@store_that_7
M = M + 1
@LOOP_that_7
0;JMP
(GO_POP_7)
@SP
M = M - 1
@SP
A = M
D = M
@store_that_7
A = M
M = D
// push
@3
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@4
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
@THIS
D = M
@store_this_10
M = D
@2
D = A
(LOOP_this_10)
@GO_PUSH_10
D;JEQ
D = D - 1
@store_this_10
M = M + 1
@LOOP_this_10
0;JMP
(GO_PUSH_10)
@store_this_10
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
@THAT
D = M
@store_that_11
M = D
@6
D = A
(LOOP_that_11)
@GO_PUSH_11
D;JEQ
D = D - 1
@store_that_11
M = M + 1
@LOOP_that_11
0;JMP
(GO_PUSH_11)
@store_that_11
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

@256
D = A
@SP
M = D
// push
@0
D = A
@SP
A = M
M = D
@SP
M = M + 1
// pop
@LCL
D = M
@0
D = D + A
@5
M = D
@SP
M = M - 1
@SP
A = M
D = M
@5
A = M
M = D
(label_LOOP_START_1)
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
@LCL
D = M
@0
A = D + A
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
// pop
@LCL
D = M
@0
D = D + A
@5
M = D
@SP
M = M - 1
@SP
A = M
D = M
@5
A = M
M = D
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
// pop
@ARG
D = M
@0
D = D + A
@5
M = D
@SP
M = M - 1
@SP
A = M
D = M
@5
A = M
M = D
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
@SP
M = M - 1
@SP
A = M
D = M
@label_LOOP_START_1
D;JNE
// push
@LCL
D = M
@0
A = D + A
D = M
@SP
A = M
M = D
@SP
M = M + 1

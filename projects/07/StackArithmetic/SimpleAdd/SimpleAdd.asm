// push
@7
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@8
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
D = M + D
@SP
A = M
M = D
@SP
M = M + 1

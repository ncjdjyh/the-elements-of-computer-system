// push
@111
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@333
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@888
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
@static___8
M = D
// pop
@SP
M = M - 1
@SP
A = M
D = M
@static___3
M = D
// pop
@SP
M = M - 1
@SP
A = M
D = M
@static___1
M = D
// push
@static___3
D = M
@SP
A = M
M = D
@SP
M = M + 1
// push
@static___1
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
@static___8
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

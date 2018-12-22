// push
@17
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@17
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
@EQ_correct_0
D;JEQ
(EQ_fault_0)
@0
D = A
@SP
A = M
M = D
@EQ_end_0
0;JMP
(EQ_correct_0)
@SP
A = M
M = -1
@EQ_end_0
0;JMP
(EQ_end_0)
@SP
M = M + 1
// push
@17
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@16
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
@EQ_correct_1
D;JEQ
(EQ_fault_1)
@0
D = A
@SP
A = M
M = D
@EQ_end_1
0;JMP
(EQ_correct_1)
@SP
A = M
M = -1
@EQ_end_1
0;JMP
(EQ_end_1)
@SP
M = M + 1
// push
@16
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@17
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
@EQ_correct_2
D;JEQ
(EQ_fault_2)
@0
D = A
@SP
A = M
M = D
@EQ_end_2
0;JMP
(EQ_correct_2)
@SP
A = M
M = -1
@EQ_end_2
0;JMP
(EQ_end_2)
@SP
M = M + 1
// push
@892
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@891
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
@LT_correct_3
D;JLT
(LT_fault_3)
@0
D = A
@SP
A = M
M = D
@LT_end_3
0;JMP
(LT_correct_3)
@SP
A = M
M = -1
@LT_end_3
0;JMP
(LT_end_3)
@SP
M = M + 1
// push
@891
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@892
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
@LT_correct_4
D;JLT
(LT_fault_4)
@0
D = A
@SP
A = M
M = D
@LT_end_4
0;JMP
(LT_correct_4)
@SP
A = M
M = -1
@LT_end_4
0;JMP
(LT_end_4)
@SP
M = M + 1
// push
@891
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@891
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
@LT_correct_5
D;JLT
(LT_fault_5)
@0
D = A
@SP
A = M
M = D
@LT_end_5
0;JMP
(LT_correct_5)
@SP
A = M
M = -1
@LT_end_5
0;JMP
(LT_end_5)
@SP
M = M + 1
// push
@32767
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@32766
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
@GT_correct_6
D;JGT
(GT_fault_6)
@0
D = A
@SP
A = M
M = D
@GT_end_6
0;JMP
(GT_correct_6)
@SP
A = M
M = -1
@GT_end_6
0;JMP
(GT_end_6)
@SP
M = M + 1
// push
@32766
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@32767
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
@GT_correct_7
D;JGT
(GT_fault_7)
@0
D = A
@SP
A = M
M = D
@GT_end_7
0;JMP
(GT_correct_7)
@SP
A = M
M = -1
@GT_end_7
0;JMP
(GT_end_7)
@SP
M = M + 1
// push
@32766
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@32766
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
@GT_correct_8
D;JGT
(GT_fault_8)
@0
D = A
@SP
A = M
M = D
@GT_end_8
0;JMP
(GT_correct_8)
@SP
A = M
M = -1
@GT_end_8
0;JMP
(GT_end_8)
@SP
M = M + 1
// push
@57
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@31
D = A
@SP
A = M
M = D
@SP
M = M + 1
// push
@53
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
// push
@112
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

// OPERATION
@SP
M = M - 1
@SP
A = M
D = M
M = -D
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
D = M & D
@SP
A = M
M = D
@SP
M = M + 1
// push
@82
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
D = M | D
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
M = !D
@SP
M = M + 1

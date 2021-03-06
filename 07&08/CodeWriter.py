import Parser

global GUA_END_SUFFIX
GUA_END_SUFFIX = 0


# SP 指针后移
def next_sp():
    write_line('@SP')
    write_line('M = M + 1')


# SP 指针前移
def previous_sp():
    write_line('@SP')
    write_line('M = M - 1')


def push_true():
    write_line('@SP')
    write_line('A = M')
    write_line('M = -1')


# 入栈数字
def push_number_in_stack(value):
    write_line('@%s' % value)
    write_line('D = A')
    write_line('@SP')
    write_line('A = M')
    write_line('M = D')


# 将 D 寄存器中的值存到栈顶
def push_d_in_stack():
    write_line('@SP')
    write_line('A = M')
    write_line('M = D')


def push_x_in_stack(x):
    write_line('@%s' % x)
    write_line('D = M')
    write_line('@SP')
    write_line('A = M')
    write_line('M = D')
    next_sp()


# 保存栈中数据到 holder 中
def pop(holder):
    previous_sp()
    write_line('@SP')
    write_line('A = M')
    write_line('%s = M' % holder)


def 出栈一个数运算后放回结果(operator):
    write_line('\n// OPERATION')
    pop('D')
    write_line('M = %sD' % operator)


# 根据运算符类型跳转
def jump_by_condition(operator):
    write_format_a_location(operator, 'correct', GUA_END_SUFFIX)
    write_line('D;J%s' % operator)


def write_format_a_location(operator_type, decorate_name, suffix):
    write_line('@%s_%s_%s' % (operator_type, decorate_name, suffix))


def write_format_jump_location(operator_type, decorate_name, suffix):
    write_line('(%s_%s_%s)' % (operator_type, decorate_name, suffix))


# 处理需要判断真假的计算操作
def write_gua(operator):
    global GUA_END_SUFFIX
    write_line('\n// JumpByCondition')
    write_line('D = M - D')
    jump_by_condition(operator)
    # 失败跳转地址
    write_format_jump_location(operator, 'fault', GUA_END_SUFFIX)
    push_number_in_stack('0')
    jump_end(operator)
    # 成立跳转地址
    write_format_jump_location(operator, 'correct', GUA_END_SUFFIX)
    push_true()
    jump_end(operator)
    # 结束跳转地址
    write_format_jump_location(operator, 'end', GUA_END_SUFFIX)
    GUA_END_SUFFIX += 1


def jump_end(operator):
    write_format_a_location(operator, 'end', GUA_END_SUFFIX)
    write_line('0;JMP')


def 出栈两个数运算后放回结果到第一个位置(operator):
    write_line('\n// OPERATION')
    pop('D')
    pop('M')
    # 是否需要判断真假的操作
    if operator in Parser.瓜operator:
        write_gua(operator)
    else:
        write_normal(operator)


# 处理普通计算操作
def write_normal(operator):
    write_line('D = M %s D' % operator)
    push_d_in_stack()


def write_arithmetic(command):
    operation_type = command.arg1
    operator = Parser.ArithMap[operation_type]
    if operation_type in Parser.DualArith:
        出栈两个数运算后放回结果到第一个位置(operator)
        next_sp()
    elif operation_type in Parser.UnitArith:
        出栈一个数运算后放回结果(operator)
        next_sp()
    else:
        raise ValueError('没有这样的运算')


def pop_util(offset, base):
    # 获取真实的变量地址
    write_line('@%s' % base)
    write_line('D = M')
    write_line('@%s' % offset)
    write_line('D = D + A')
    # 把地址存到 RAM[5] 里
    write_line('@5')
    write_line('M = D')
    # pop
    pop('D')
    write_line("@5")
    write_line('A = M')
    write_line('M = D')


def push_temp_or_pointer_util(offset, base):
    number = str(int(base) + int(offset))
    write_line('@%s' % number)
    write_line('D = M')
    push_d_in_stack()
    next_sp()


# base 为 LCL ARG 等内存段的地址
def push_util(offset, base):
    # 获取真实的变量地址
    write_line('@%s' % base)
    write_line('D = M')
    write_line('@%s' % offset)
    write_line('A = D + A')
    # push
    write_line('D = M')
    push_d_in_stack()
    next_sp()


def push_static_util(value):
    write_format_a_location('static', '_', value)
    write_line('D = M')
    push_d_in_stack()
    next_sp()


def push_value_by_type(value, type):
    if type == 'constant':
        push_number_in_stack(value)
        next_sp()
    elif type == 'local':
        push_util(value, 'LCL')
    elif type == 'argument':
        push_util(value, 'ARG')
    elif type == 'static':
        push_static_util(value)
    elif type == 'this':
        push_util(value, 'THIS')
    elif type == 'that':
        push_util(value, 'THAT')
    elif type == 'pointer':
        push_temp_or_pointer_util(value, 3)
    elif type == 'temp':
        # TEMP 的初始地址为 5
        push_temp_or_pointer_util(value, 5)


def pop_temp_or_pointer_util(base, offset):
    pop('D')
    number = str(int(base) + int(offset))
    write_line('@%s' % number)
    write_line('M = D')


def pop_static_util(value):
    pop('D')
    write_format_a_location('static', '_', value)
    write_line('M = D')


def pop_value_by_type(value, type):
    if type == 'constant':
        raise ValueError('不存在这样的 pop 操作')
    elif type == 'local':
        pop_util(value, 'LCL')
    elif type == 'argument':
        pop_util(value, 'ARG')
    elif type == 'static':
        pop_static_util(value)
    elif type == 'this':
        pop_util(value, 'THIS')
    elif type == 'that':
        pop_util(value, 'THAT')
    elif type == 'pointer':
        pop_temp_or_pointer_util(value, 3)
    elif type == 'temp':
        pop_temp_or_pointer_util(value, 5)


def write_push_pop(command):
    type = command.arg1
    value = command.arg2
    if command.type == 'C_PUSH':
        write_line('// push')
        push_value_by_type(value, type)
    else:
        write_line('// pop')
        pop_value_by_type(value, type)


def write_init():
    # 初始化堆栈指针
    write_line('// init')
    write_line('@256')
    write_line('D = A')
    write_line('@SP')
    write_line('M = D')
    write_line('@Sys.init')
    write_line('0;JMP')


def write_label(command):
    write_line('// label')
    dest = command.arg1
    write_line('(%s)' % dest)


def write_if(command):
    write_line('// if')
    dest = command.arg1
    pop('D')
    write_line('@%s' % dest)
    write_line('D;JNE')


def write_goto(command):
    write_line('// go')
    dest = command.arg1
    write_line('@%s' % dest)
    write_line('0;JMP')


global CALL_SUFFIX
CALL_SUFFIX = 0


def write_call(command):
    write_line('// call')
    global CALL_SUFFIX
    func_name = command.arg1
    func_arg_num = int(command.arg2)
    # push return_address
    write_format_a_location('returnAddress', func_name, CALL_SUFFIX)
    write_line('D = A')
    push_d_in_stack()
    next_sp()
    push_x_in_stack('LCL')
    push_x_in_stack('ARG')
    push_x_in_stack('THIS')
    push_x_in_stack('THAT')
    # ARG = SP - n - 5
    write_line('@%s' % func_arg_num)
    write_line('D = A')
    write_line('@5')
    write_line('D = D + A')
    write_line('@SP')
    write_line('D = M - D')
    write_line('@ARG')
    write_line('M = D')
    # LCL = SP
    write_line('@SP')
    write_line('D = M')
    write_line('@LCL')
    write_line('M = D')
    # go F()
    write_line('@%s' % func_name)
    write_line('0;JMP')
    write_format_jump_location('returnAddress', func_name, CALL_SUFFIX)
    CALL_SUFFIX += 1


def write_return():
    write_line('// return')
    write_line('@LCL')
    write_line('D = M')
    # RAM[6] 存放 local 段地址, 用它当成基地址返回调用者的帧栈
    write_line('@6')
    write_line('M = D')
    # 调用者获取返回值
    pop('D')
    write_line('@ARG')
    write_line('A = M')
    write_line('M = D')
    # 恢复调用者 SP
    write_line('@ARG')
    write_line('D = M + 1')
    write_line('@SP')
    write_line('M = D')
    # 恢复调用者寄存器值
    recover_frame(1, 'THAT')
    recover_frame(2, 'THIS')
    recover_frame(3, 'ARG')
    recover_frame(4, 'LCL')
    # 返回
    write_line('@5')
    write_line('D = A')
    write_line('@6')
    write_line('A = M - D')
    write_line('0;JMP')


def recover_frame(offset, name):
    write_line('@%s' % offset)
    write_line('D = A')
    write_line('@6')
    write_line('A = M - D')
    write_line('D = M')
    write_line('@%s' % name)
    write_line('M = D')


def write_function(command):
    write_line('// function')
    func_name = command.arg1
    func_lcl_num = int(command.arg2)
    write_line('(%s)' % func_name)
    for n in range(func_lcl_num):
        write_line('@LCL')
        write_line('D = M')
        write_line('@%s' % n)
        write_line('A = A + D')
        write_line('M = 0')


def write_line(content):
    f = open('resources/prog.asm', 'a')
    f.write(content + '\n')

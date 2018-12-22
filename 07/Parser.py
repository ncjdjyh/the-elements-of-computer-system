# 计算指令
UnitArith = ('neg', 'not')
DualArith = ('add', 'sub', 'eq', 'gt', 'lt', 'and', 'or')
ArithMap = {'add': '+', 'sub': '-', 'neg': '-', 'eq': 'EQ', 'gt': 'GT', 'lt': 'LT', 'and': '&', 'or': '|', 'not': '!'}
# 要将真假放回栈中的操作 真: 0xFFFF 假: 0x0000
瓜operator = ('EQ', 'GT', 'LT')


# 命令类
class Command(object):
    def __init__(self, content, type):
        self.content = str(content)
        self.type = type
        self.arg1 = ''
        self.arg2 = ''
        self.set_args_by_type()

    def set_args_by_type(self):
        if not self.type is 'C_RETURN':
            self.set_arg1()
        if self.type in ('C_PUSH', 'C_POP', 'C_FUNCTION', 'C_CALL'):
            self.set_arg2()

    def set_arg1(self):
        if self.type is 'C_ARITHMATIC':
            self.arg1 = self.content
        else:
            self.arg1 = self.content.split(' ', 2)[1].strip()

    def set_arg2(self):
        self.arg2 = self.content.split(' ', 2)[2].strip()


# 判断命令类型
def command_type(line):
    if line.find('push') >= 0:
        return 'C_PUSH'
    elif line.find('pop') >= 0:
        return 'C_POP'
    elif line.strip() in ArithMap:
        return 'C_ARITHMATIC'

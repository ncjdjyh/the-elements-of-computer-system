# 计算指令
UnitArith = ('neg', 'not')
DualArith = ('add', 'sub', 'eq', 'gt', 'lt', 'and', 'or')
ArithMap = {'add': '+', 'sub': '-', 'neg': '-', 'eq': 'EQ', 'gt': 'GT', 'lt': 'LT', 'and': '&', 'or': '|', 'not': '!'}
# 要将真假放回栈中的操作 真: 0xFFFF(-1) 假: 0x0000(0)
瓜operator = ('EQ', 'GT', 'LT')


# 命令类
class Command(object):
    def __init__(self, command_line):
        self.content = command_line
        self.type = self.command_type()
        self.arg1 = ''
        self.arg2 = ''
        self.set_args_by_type()

    def set_args_by_type(self):
        if self.type is not 'C_RETURN':
            self.set_arg1()
        if self.type in ('C_PUSH', 'C_POP', 'C_FUNCTION', 'C_CALL'):
            self.set_arg2()

    def set_arg1(self):
        if self.type is 'C_ARITHMETIC':
            self.arg1 = self.content
        else:
            self.arg1 = self.content.split(' ', 2)[1].strip()

    def set_arg2(self):
        self.arg2 = self.content.split(' ', 2)[2].strip()

    # 判断命令类型
    def command_type(self):
        if self.content.find('push') >= 0:
            return 'C_PUSH'
        elif self.content.find('pop') >= 0:
            return 'C_POP'
        elif self.content.strip() in ArithMap:
            return 'C_ARITHMETIC'
        elif self.content.find('label') >= 0:
            return 'C_LABEL'
        elif self.content.find('if-goto') >= 0:
            return 'C_IF'
        elif self.content.find('goto') >= 0:
            return 'C_GOTO'
        elif self.content.find('function') >= 0:
            return 'C_FUNCTION'
        elif self.content.find('call') >= 0:
            return 'C_CALL'
        elif self.content.find('return') >= 0:
            return 'C_RETURN'




import Parser
import CodeWriter


def extract_asm_code(command_line):
    command_line = trim_command(command_line)
    command_type = Parser.command_type(command_line)
    command = Parser.Command(command_line, command_type)
    if command.type in ('C_PUSH', 'C_POP'):
        CodeWriter.write_push_pop(command)
    elif command.type is 'C_ARITHMATIC':
        CodeWriter.write_arithmetic(command)
    print('arg1:' + command.arg1 + '-----arg2:' + command.arg2)


# 去除行内无意义的空格和注释
def trim_command(command):
    if command.find('//'):
        command = command.split('//')[0]
    return command.strip()


def __lunch():
    with open('resources/BasicTest.vm', 'r') as f:
        command = f.readline()
        while command:
            blank_or_comment = command.startswith('//') or not command.strip()
            if not blank_or_comment:
                extract_asm_code(command)
            command = f.readline()
    print('done')


__lunch()
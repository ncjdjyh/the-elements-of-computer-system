import Parser
import CodeWriter


def extract_asm_code(command):
    if command.type in ('C_PUSH', 'C_POP'):
        CodeWriter.write_push_pop(command)
    elif command.type is 'C_ARITHMETIC':
        CodeWriter.write_arithmetic(command)
    elif command.type is 'C_LABEL':
        CodeWriter.write_label(command)
    elif command.type is 'C_GOTO':
        CodeWriter.write_goto(command)
    elif command.type is 'C_IF':
        CodeWriter.write_if(command)
    elif command.type is 'C_CALL':
        CodeWriter.write_call(command)
    elif command.type is 'C_RETURN':
        CodeWriter.write_return(command)
    elif command.type is 'FUNCTION':
        CodeWriter.write_function(command)
    print('arg1:' + command.arg1 + '-----arg2:' + command.arg2)


# 去除行内无意义的空格和注释
def trim_command(command):
    if command.find('//'):
        command = command.split('//')[0]
    return command.strip()


def __lunch():
    with open('resources/BasicTest.vm', 'r') as f:
        command = f.readline()
        CodeWriter.write_init()
        while command:
            blank_or_comment = command.startswith('//') or not command.strip()
            if not blank_or_comment:
                command = trim_command(command)
                command_obj = Parser.Command(command)
                extract_asm_code(command_obj)
            command = f.readline()
    print('done')


__lunch()
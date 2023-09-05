import os
import obscure

def extract_comment_lines(content: str):
    content = content.replace("\r\n", "\n")
    content = content.replace("\r", "\n")
    lines = content.split("\n")
    comment_lines = []
    for i, line in enumerate(lines):
        if line.strip().startswith("//"):
            comment_lines.append(i + 1)
            continue
        if line.strip().startswith("/*"):
            comment_lines.append(i + 1)
            in_comment = True
        if line.strip().endswith("*/"):
            comment_lines.append(i + 1)
            in_comment = False
            continue
        if in_comment:
            comment_lines.append(i + 1)

    return comment_lines

def is_line_in_comment(content, line):
    comment_lines = extract_comment_lines(content)
    return line in comment_lines

def extract_test(bug, test):
    test_path = os.path.join(os.path.dirname(__file__), '..', 'data/failing_tests', bug['project'], str(bug['bugId']), test['className'].strip().replace('.', '/') + '.java')

    with open(test_path) as f:
        content = f.read()
        index = 0
        while True:
            i = content[index:].index(test['methodName'] + "(")
            line = content[:index + i].count("\n") + 1
            if is_line_in_comment(content, line):
                index += i + 1
                continue
            return obscure.methodAtLine(test_path, line)

def extract_tests(bug):
    tests = []
    for test in bug['failingTests']:
        tests.append(extract_test(bug, test))
        break
    return tests
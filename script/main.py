import argparse
import os
import json
from tasks.generateTests import process_bug as process_bug_tests
from tasks.fixCode import process_bug as process_bug_fix_bug


def main(args):
    with open(os.path.join(os.path.dirname(__file__), '..', 'data/defects4j-bugs.json')) as json_file:
        data = json.load(json_file)
        data.sort(key=lambda x: x['project'] + str(x['bugId']))
        for bug in data:
            if args.project is not None and bug['project'] != args.project:
                continue
            if args.bugId is not None and bug['bugId'] != args.bugId:
                continue
            if bug['metrics']['methods'] > 1:
                continue
            path_buggy_files = os.path.join(os.path.dirname(__file__), '..', 'data/buggy_files', bug['project'], str(bug['bugId']))

            print(f"[PROGRESS] Processing {bug['project']} {bug['bugId']}")
            if args.task == 'test':
                process_bug_tests(bug, path_buggy_files)
            elif args.task == 'patch':
                process_bug_fix_bug(bug, path_buggy_files)
            
            

if __name__ == '__main__':
    parser = argparse.ArgumentParser(
                    prog='main',
                    description='Generate tasks for chatgpt')
    parser.add_argument('-t', '--task', choices=['test', 'patch'], required=True)
    parser.add_argument('-p', '--project', choices=['Chart', 'Closure', 'Lang', 'Math', 'Mockito', 'Time'])
    parser.add_argument('-b', '--bugId', type=int)
    args = parser.parse_args()
    main(args)
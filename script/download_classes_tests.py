import os
import json
import requests

def downloadTestFiles(bug):
	test_classes = set()
	for test in bug['failingTests']:
		test_classes.add(test['className'].strip())
	for test in test_classes:
		output_path = os.path.join(os.path.dirname(__file__), '..', 'data/failing_tests', bug['project'], str(bug['bugId']), test.replace('.', '/') + '.java')
		if os.path.exists(output_path):
			continue
		source_root = ""
		if "/src/main/java" in bug['diff']:
			source_root = "/src/test/java/"
		elif "src/java/" in bug['diff']:
			source_root = "/src/test"
		elif "src/" in bug['diff']:
			source_root = "/test"
		else:
			source_root = "/tests"
		url_file = f"https://raw.githubusercontent.com/Spirals-Team/defects4j-repair/{bug['project']}{bug['bugId']}{source_root}/{test.replace('.', '/') + '.java'}"
		res = requests.get(url_file)
		if res.status_code != 200:
			print(url_file)
			continue
		if not os.path.exists(os.path.dirname(output_path)):
			os.makedirs(os.path.dirname(output_path))
		
		with open(output_path, 'w') as f:
			f.write(res.text)
	

def downloadBuggyFiles(bug):
	for (file_path) in bug['changedFiles']:
		output_path = os.path.join(os.path.dirname(__file__), '..', 'data/buggy_files', bug['project'], str(bug['bugId']), file_path)
		if os.path.exists(output_path):
			continue
		source_root = "/src/main/java"
		if "/src/main/java" in bug['diff']:
			source_root = "/src/main/java"
		elif "src/java/" in bug['diff']:
			source_root = "/src/java"
		elif "src/" in bug['diff']:
			source_root = "/src"
		else:
			source_root = "/source"
		url_file = f"https://raw.githubusercontent.com/Spirals-Team/defects4j-repair/{bug['project']}{bug['bugId']}{source_root}/{file_path}"
		res = requests.get(url_file)
		if res.status_code != 200:
			print(url_file)
			continue
		if not os.path.exists(os.path.dirname(output_path)):
			os.makedirs(os.path.dirname(output_path))
		
		with open(output_path, 'w') as f:
			f.write(res.text)
	
def main():
    with open(os.path.join(os.path.dirname(__file__), '..', 'data/defects4j-bugs.json')) as json_file:
        data = json.load(json_file)
        for bug in data:
            if bug['metrics']['methods'] > 1:
                continue
            downloadBuggyFiles(bug)
            downloadTestFiles(bug)
            

if __name__ == '__main__':
    main()
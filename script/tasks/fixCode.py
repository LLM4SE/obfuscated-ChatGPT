import traceback
import chatgpt
import obscure
import os
import datetime
import time
import json

from util import extract_test

def generate_prompt(buggy_code, bug):
  prompt = f"""[no prose] The following tests are failing because of the provided buggy code. Provide a patch for the source code without changing the structure. Output exclusively code without explanation.

## Buggy Code:

```java
{buggy_code}
```
## Failing Tests:

"""
  for test in bug['failingTests']:
    test_code = extract_test(bug, test)
    prompt += f"```java\n{test_code}```\n"
    if test['message'] != "":
      prompt += f"Output: {test['message']}\n"
  return prompt

def process_bug(bug, path_buggy_files):
  for file_path in bug['changedFiles']:
    for type_change in bug['changedFiles'][file_path]:
        line = bug['changedFiles'][file_path][type_change][0][0]
        try:
            obfuscated = obscure.obscure(path_buggy_files, path_buggy_files, line, "synonym")
            if obfuscated == "":
                continue
            original = obscure.methodAtLine(path_buggy_files, line)

            out = os.path.join(os.path.dirname(__file__), '..', '..', 'data/tasks/patch/chatgpt', bug['project'], str(bug['bugId']))

            if not os.path.exists(out):
                os.makedirs(out)

            # Generate prompt
            original_prompt = generate_prompt(original, bug)
            obfuscated_prompt = generate_prompt(obfuscated, bug)
            
            # Execute prompt
            original_res = chatgpt.execute(original_prompt)
            obfuscated_res = chatgpt.execute(obfuscated_prompt)

            o = {
                'date': datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S"),
                'bug': bug,
                'original_prompt': original_prompt,
                'obfuscated_prompt': obfuscated_prompt,
                'original_res': original_res,
                'obfuscated_res': obfuscated_res,
            }

            with open(os.path.join(out, f"info_{time.time()}.json"), 'w') as f:
                json.dump(o, f, indent=2)
        except Exception as e:
          # print stack trace
          traceback.print_exc()
          pass
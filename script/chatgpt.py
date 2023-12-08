from dotenv import load_dotenv
load_dotenv()

import os
from webdriver_manager.chrome import ChromeDriverManager
ChromeDriverManager().install()
from UnlimitedGPT import ChatGPT

api_instance = ChatGPT(os.getenv('chatgpt_session'))

def api():
	global api_instance
	return api_instance

def extractCodeBlockFromMd(md):
	if md is None:
		return ""
	codeBlock = ""
	codeBlockStart = False
	for line in md.split("\n"):
		if line.strip().startswith("```"):
			codeBlockStart = not codeBlockStart
		elif codeBlockStart:
			codeBlock += line + "\n"
	return codeBlock

def execute(prompt):
	resp = api().send_message(prompt)
	codes = extractCodeBlockFromMd(resp.response)
	api().reset_conversation()
	# time.sleep(15 * random.random() + 5)
	return {"response": resp.response, "codes": codes}
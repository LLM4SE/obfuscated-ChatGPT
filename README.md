# Obfuscated ChatGPT

The purpose of this software is to provide a execution framework to make repetitable ChatGPT query in the context of Defects4J.
This respository also provide a obfuscation mechnism for Java code which allows to rename variable, classes and parameters, as well as removing the comments.
Multiple renaming strategies exsist: 1. Permute 2. Random name 3. Synonym.

## Usage

Edit your chatgpt session cookie in `.env`. Follow the instructions from https://github.com/terry3041/pyChatGPT?tab=readme-ov-file#obtaining-session_token

```
python3 main.py -t patch -p Chart -b 11
```

or 

```
python3 main.py -t test -p Chart -b 11
```

## Output

The results are saved in `data/tasks/fixBug/chatgpt/<project>/<bugID>/info_<timestamp>.json`
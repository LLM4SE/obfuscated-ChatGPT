import os
import subprocess

def methodAtLine(path, line):
  return obscure(path, path, line, "random", False, False, False, False, False, False)

def obscure(path, outputPath, line = None, renameStrategy="random", removeComments=True, renameTypes=False, renameMethods=False, renameFields=True, renameVariables=True, renameParameters=True):
  cmd = f"{os.getenv('java20_home')}/bin/java -jar {os.path.join(os.path.dirname(__file__), '..', 'target/obscure-openai-0.1-SNAPSHOT-jar-with-dependencies.jar')} -p {path} -o {outputPath} -r {renameStrategy} "

  if line:
    cmd += f" -l {line}"
  if removeComments:
    cmd += " -c"
  if renameTypes:
    cmd += " -t"
  if renameMethods:
    cmd += " -m"
  if renameFields:
    cmd += " -f"
  if renameVariables:
    cmd += " -v"
  if renameParameters:
    cmd += " -a"
    

  return subprocess.check_output(cmd, shell=True).decode("utf-8")
import subprocess
import os

DEFECTS4J_HOME = os.path.join(os.path.dirname(__file__), '..', 'defects4j')


def getProjects():
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j pids"
  return subprocess.check_output(cmd, shell=True).decode("utf-8").split()

def getBugs(project):
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j bids -p " + project
  return subprocess.check_output(cmd, shell=True).decode("utf-8").split()

def checkout(project, bug, workdir):
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j checkout -p " + project + " -v " + bug + "b -w " + workdir
  return subprocess.check_output(cmd, shell=True).decode("utf-8")

def compile(project, workdir):
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j compile -w " + workdir
  return subprocess.check_output(cmd, shell=True).decode("utf-8")

def test(project, workdir, relevantTests = False):
  if relevantTests:
    cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j test -r -w " + workdir + ""
  else:
    cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j test -w " + workdir
  return subprocess.check_output(cmd, shell=True).decode("utf-8")

def pathToSource(project, workdir):
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j export -p dir.src.classes -w " + workdir
  return subprocess.check_output(cmd, shell=True).decode("utf-8").strip()

def buggyClasses(project, workdir):
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j export -p classes.modified -w " + workdir
  return subprocess.check_output(cmd, shell=True).decode("utf-8").strip().split()

def coverage(project, workdir):
  cmd = f"{DEFECTS4J_HOME}/framework/bin/defects4j coverage -w " + workdir
  return subprocess.check_output(cmd, shell=True).decode("utf-8")
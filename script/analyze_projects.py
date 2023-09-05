import os
import Defects4j
import obscure
def main():
    projects = Defects4j.getProjects()
    for project in projects:
        bugs = Defects4j.getBugs(project)
        for bug in bugs:
            workdir = "/tmp/" + project + "_" + bug
            Defects4j.checkout(project, bug, workdir)
            sourcePath = Defects4j.pathToSource(project, workdir)
            classes = Defects4j.buggyClasses(project, workdir)
            for cl in classes:
              pathClass = os.path.join(workdir, sourcePath, cl.replace(".", "/") + ".java")
              
              # print(open(pathClass, 'r').read())
              print(obscure.obscure(pathClass, os.path.join(workdir, sourcePath)))
              # os.unlink(pathClass)
              # print(open(pathClass, 'r').read())

            # os.system("rm -rf " + workdir)
            # break
        break

if __name__ == '__main__':
    main()
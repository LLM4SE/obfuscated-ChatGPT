package nl.tudelft.obscure_openai;

import org.apache.commons.cli.*;

import nl.tudelft.obscure_openai.rename_strategy.GenerateStragegy;
import nl.tudelft.obscure_openai.rename_strategy.PermuteStragegy;
import nl.tudelft.obscure_openai.rename_strategy.RandomStragegy;
import nl.tudelft.obscure_openai.rename_strategy.SynonymStragegy;
import nl.tudelft.obscure_openai.transformation.RemoveComments;
import nl.tudelft.obscure_openai.transformation.RenameFields;
import nl.tudelft.obscure_openai.transformation.RenameLocalVariables;
import nl.tudelft.obscure_openai.transformation.RenameMethods;
import nl.tudelft.obscure_openai.transformation.RenameParameters;
import nl.tudelft.obscure_openai.transformation.RenameTypes;
import spoon.Launcher;
import spoon.compiler.Environment.PRETTY_PRINTING_MODE;
import spoon.reflect.cu.position.NoSourcePosition;
import spoon.reflect.declaration.CtConstructor;
import spoon.reflect.declaration.CtExecutable;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtTypeMember;
import spoon.support.sniper.SniperJavaPrettyPrinter;

public class Main {
  public static void main(String[] args) {
    Options options = new Options();

    Option alpha = new Option("c", "comments", false, "Remove comments");
    options.addOption(alpha);
    Option fields = new Option("f", "fields", false, "Rename fields");
    options.addOption(fields);
    Option variables = new Option("v", "variables", false, "Rename variables");
    options.addOption(variables);
    Option types = new Option("t", "types", false, "Rename types");
    options.addOption(types);
    Option methods = new Option("m", "methods", false, "Rename methods");
    options.addOption(methods);
    Option parameters = new Option("a", "parameters", false, "Rename parameters");
    options.addOption(parameters);

    Option config = Option.builder("r").longOpt("rename")
        .argName("rename")
        .hasArg()
        .required(false)
        .desc("Define ther rename strategy [random,permute,generate]").build();
    options.addOption(config);

    Option projectPath = Option.builder("p").longOpt("path")
        .argName("path")
        .hasArg()
        .required(true)
        .desc("Path to the class that you want to alterate").build();
    options.addOption(projectPath);
    Option lineOpt = Option.builder("l").longOpt("line")
        .argName("line")
        .hasArg()
        .required(false)
        .desc("Line of the method to return").build();
    options.addOption(lineOpt);
    Option outputPath = Option.builder("o").longOpt("output")
        .argName("ouput")
        .hasArg()
        .required(true)
        .desc("Path to the class that you want to alterate").build();
    options.addOption(outputPath);

    // define parser
    CommandLine cmd;
    CommandLineParser parser = new DefaultParser();
    HelpFormatter helper = new HelpFormatter();

    try {
      cmd = parser.parse(options, args);

      Launcher launcher = new Launcher();
      launcher.addInputResource(cmd.getOptionValue("p"));
      launcher.getEnvironment().setPrettyPrintingMode(PRETTY_PRINTING_MODE.AUTOIMPORT);
      launcher.getEnvironment().setCommentEnabled(true);
      launcher.getEnvironment().setNoClasspath(true);
      launcher.getEnvironment().setCopyResources(false);
      launcher.getEnvironment().setIgnoreSyntaxErrors(true);
      if (!cmd.hasOption("l")) {
        launcher.setSourceOutputDirectory(cmd.getOptionValue("o"));
      }

      launcher.buildModel();

      Context context = new Context();
      context.setModel(launcher.getModel());
      context.setRenameStrategy(new RandomStragegy(context));

      if (cmd.hasOption("r")) {
        if (cmd.getOptionValue("r").equals("random")) {
        } else if (cmd.getOptionValue("r").equals("permute")) {
          context.setRenameStrategy(new PermuteStragegy(context));
        } else if (cmd.getOptionValue("r").equals("generate")) {
          context.setRenameStrategy(new GenerateStragegy(context));
        } else if (cmd.getOptionValue("r").equals("synonym")) {
          context.setRenameStrategy(new SynonymStragegy(context));
        } else {
          System.out.println("Invalid rename strategy");
          System.exit(0);
        }
      }

      if (cmd.hasOption("c")) {
        launcher.getModel().processWith(new RemoveComments(context));
      }
      if (cmd.hasOption("f")) {
        launcher.getModel().processWith(new RenameFields(context));
      }
      if (cmd.hasOption("v")) {
        launcher.getModel().processWith(new RenameLocalVariables(context));
      }
      if (cmd.hasOption("t")) {
        launcher.getModel().processWith(new RenameTypes(context));
      }
      if (cmd.hasOption("m")) {
        launcher.getModel().processWith(new RenameMethods(context));
      }
      if (cmd.hasOption("p")) {
        launcher.getModel().processWith(new RenameParameters(context));
      }

      if (cmd.hasOption("l")) {
        int line = Integer.parseInt(cmd.getOptionValue("l"));
        launcher.getModel()
            .filterChildren(c -> c instanceof CtExecutable && !(c.getPosition() instanceof NoSourcePosition)
                && c.getPosition().getLine() <= line
                && c.getPosition().getEndLine() >= line)
            .forEach(c -> {
              System.out.println(c);
            });
      } else {
        // reprint the code
        launcher.prettyprint();
      }
    } catch (ParseException e) {
      System.out.println(e.getMessage());
      helper.printHelp("Usage:", options);
      System.exit(0);
    }
  }
}
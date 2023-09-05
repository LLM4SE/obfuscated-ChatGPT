package nl.tudelft.obscure_openai.rename_strategy;

import java.util.Random;

import nl.tudelft.obscure_openai.Context;

public class GenerateStragegy implements RenameStrategy {

  private Context context;
  private Random generator;

  public GenerateStragegy(Context context) {
    super();
    this.context = context;
    this.generator = new Random(this.context.getRandomSeed());
  }

  @Override
  public String rename(String name) {
    return name;
  }

}

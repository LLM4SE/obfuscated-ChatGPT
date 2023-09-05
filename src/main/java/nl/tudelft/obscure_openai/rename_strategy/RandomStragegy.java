package nl.tudelft.obscure_openai.rename_strategy;

import java.util.Random;

import nl.tudelft.obscure_openai.Context;

public class RandomStragegy implements RenameStrategy {

  private Context context;
  private Random generator;

  public RandomStragegy(Context context) {
    super();
    this.context = context;
    this.generator = new Random(this.context.getRandomSeed());
  }

  private String gererateString(int length) {
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
      int index = generator.nextInt(alphabet.length());
      sb.append(alphabet.charAt(index));
    }
    return sb.toString();
  }

  @Override
  public String rename(String name) {
    return gererateString(name.length());
  }

}

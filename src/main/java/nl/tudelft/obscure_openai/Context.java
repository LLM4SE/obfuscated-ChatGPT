package nl.tudelft.obscure_openai;

import nl.tudelft.obscure_openai.rename_strategy.RenameStrategy;
import spoon.reflect.CtModel;

public class Context {
  private RenameStrategy renameStrategy;
  private CtModel model;

  public RenameStrategy getRenameStrategy() {
    return renameStrategy;
  }

  public void setRenameStrategy(RenameStrategy renameStrategy) {
    this.renameStrategy = renameStrategy;
  }

  public void setModel(CtModel model) {
    this.model = model;
  }

  public CtModel getModel() {
    return model;
  }

  public int getRandomSeed() {
    return 42;
  }
}

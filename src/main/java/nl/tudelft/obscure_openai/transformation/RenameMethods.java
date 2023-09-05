package nl.tudelft.obscure_openai.transformation;

import nl.tudelft.obscure_openai.Context;
import spoon.processing.AbstractProcessor;
import spoon.refactoring.Refactoring;
import spoon.reflect.declaration.CtMethod;

/**
 * Rename method name.
 */
public class RenameMethods extends AbstractProcessor<CtMethod<?>> {

  private Context context;

  public RenameMethods(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void process(CtMethod<?> element) {
    Refactoring.changeMethodName(element, context.getRenameStrategy().rename(element.getSimpleName()));
  }
}

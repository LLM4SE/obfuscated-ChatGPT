package nl.tudelft.obscure_openai.transformation;

import nl.tudelft.obscure_openai.Context;
import spoon.processing.AbstractProcessor;
import spoon.refactoring.Refactoring;
import spoon.reflect.declaration.CtType;

/**
 * Rename type name.
 */
public class RenameTypes extends AbstractProcessor<CtType<?>> {

  private Context context;

  public RenameTypes(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void process(CtType<?> element) {
    Refactoring.changeTypeName(element, context.getRenameStrategy().rename(element.getSimpleName()));
  }
}

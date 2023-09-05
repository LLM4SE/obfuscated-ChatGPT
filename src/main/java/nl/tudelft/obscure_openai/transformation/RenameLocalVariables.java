package nl.tudelft.obscure_openai.transformation;

import nl.tudelft.obscure_openai.Context;
import spoon.processing.AbstractProcessor;
import spoon.refactoring.Refactoring;
import spoon.reflect.code.CtLocalVariable;

/**
 * Rename local variable name.
 */
public class RenameLocalVariables extends AbstractProcessor<CtLocalVariable<?>> {

  private Context context;

  public RenameLocalVariables(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void process(CtLocalVariable<?> element) {
    try {
      Refactoring.changeLocalVariableName(element, context.getRenameStrategy().rename(element.getSimpleName()));
    } catch (Exception e) {
      try {
        Refactoring.changeLocalVariableName(element, context.getRenameStrategy().rename(element.getSimpleName()));
      } catch (Exception e1) {
        
      }
    }
  }
}

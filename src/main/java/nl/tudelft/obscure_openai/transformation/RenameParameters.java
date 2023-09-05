package nl.tudelft.obscure_openai.transformation;

import nl.tudelft.obscure_openai.Context;
import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtField;
import spoon.reflect.declaration.CtParameter;
import spoon.reflect.reference.CtReference;
import spoon.reflect.visitor.chain.CtConsumer;
import spoon.reflect.visitor.filter.ParameterReferenceFunction;

/**
 * Rename parameters name.
 */
public class RenameParameters extends AbstractProcessor<CtParameter<?>> {

  private Context context;

  public RenameParameters(Context context) {
    super();
    this.context = context;
  }

  @Override
  public void process(CtParameter<?> element) {
    String newName = context.getRenameStrategy().rename(element.getSimpleName());
    element.map(new ParameterReferenceFunction()).forEach(new CtConsumer<CtReference>() {
      @Override
      public void accept(CtReference t) {
        t.setSimpleName(newName);
      }
    });
    element.setSimpleName(newName);
  }
}
